import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.EExpressionType;
import gudusoft.gsqlparser.EJoinType;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.*;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JoinConverter {

    private String ErrorMessage = "";
    private int ErrorNo;
    private String query;
    private EDbVendor vendor;

    public JoinConverter(String sql, EDbVendor vendor) {
        this.query = sql;
        this.vendor = vendor;
    }

    public static void main(String args[]) {

        if (args.length == 0) {
            System.out.println("Usage: java JoinConverter scriptfile [/t <database type>]");
            System.out.println("/t: Option, set the database type. Support oracle, mssql, the default type is oracle");
            return;
        }

        List<String> argList = Arrays.asList(args);

        EDbVendor vendor = EDbVendor.dbvmssql;

        int index = argList.indexOf("/t");

        if (index != -1 && args.length > index + 1) {
            vendor = TGSqlParser.getDBVendorByName(args[index + 1]);
        }

        String vendorString = EDbVendor.dbvmssql == vendor ? "SQL Server"
                : "Oracle";
        System.out.println("SQL with " + vendorString + " propriety joins");

        String sqltext = getFileContent(new File(args[0]));
        System.out.println(sqltext);
        JoinConverter converter = new JoinConverter(sqltext, vendor);
        if (converter.convert() != 0) {
            System.out.println(converter.getErrorMessage());
        } else {
            System.out.println("\nSQL in ANSI joins");
            System.out.println(converter.getQuery());
        }
    }

    public static String getFileContent(File file) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] tmp = new byte[4096];
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            while (true) {
                int r = is.read(tmp);
                if (r == -1)
                    break;
                out.write(tmp, 0, r);
            }
            byte[] bytes = out.toByteArray();
            is.close();
            out.close();
            String content = new String(bytes);
            return content.trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public String getQuery() {
        // remove blank line from query
        return this.query.replaceAll("(?m)^[ \t]*\r?\n", "");
    }

    public int convert() {
        TGSqlParser sqlparser = new TGSqlParser(vendor);
        sqlparser.sqltext = this.query;
        ErrorNo = sqlparser.parse();
        if (ErrorNo != 0) {
            ErrorMessage = sqlparser.getErrormessage();
            return ErrorNo;
        }

        TCustomSqlStatement stmt = sqlparser.sqlstatements.get(0);
        analyzeSelect(stmt);
        this.query = stmt.toString();
        return ErrorNo;
    }

    private boolean isNameOfTable(TTable table, String name) {
        return (name != null) && table.getName().equalsIgnoreCase(name);
    }

    private boolean isAliasOfTable(TTable table, String alias) {
        return table.getAliasClause() != null && (alias != null) && table.getAliasClause().toString().equalsIgnoreCase(alias);
    }

    private boolean isNameOrAliasOfTable(TTable table, String str) {
        return isAliasOfTable(table, str) || isNameOfTable(table, str);
    }

    private boolean areTableJoined(TTable leftTable, TTable rightTable,
                                   List<JoinCondition> jrs) {

        boolean ret = false;
        for (int i = 0; i < jrs.size(); i++) {
            JoinCondition jc = jrs.get(i);
            if (jc.used) {
                continue;
            }
            /**
             * jungle this table is joined
             */
            ret = (isNameOrAliasOfTable(leftTable, jc.lefttable)
                    && isNameOrAliasOfTable(rightTable, jc.righttable))
                    || (isNameOrAliasOfTable(leftTable, jc.righttable)
                    && isNameOrAliasOfTable(rightTable, jc.lefttable));
            if (ret)
                break;
        }
        return ret;
    }

    private String getJoinType(List<JoinCondition> jrs) {
        String str = "inner join";
        for (int i = 0; i < jrs.size(); i++) {
            if (jrs.get(i).jt == jointype.left) {
                str = "left outer join";
                break;
            } else if (jrs.get(i).jt == jointype.right) {
                str = "right outer join";
                break;
            } else if (jrs.get(i).jt == jointype.full) {
                str = "full outer join";
                break;
            } else if (jrs.get(i).jt == jointype.cross) {
                str = "cross join";
                break;
            } else if (jrs.get(i).jt == jointype.join) {
                str = "join";
                break;
            }
        }

        return str;
    }

    private List<JoinCondition> getJoinCondition(TTable leftTable,
                                                 TTable rightTable, List<JoinCondition> jrs) {
        List<JoinCondition> lcjrs = new ArrayList<>();
        for (int i = 0; i < jrs.size(); i++) {
            JoinCondition jc = jrs.get(i);
            if (jc.used) {
                continue;
            }

            if (isNameOrAliasOfTable(leftTable, jc.lefttable)
                    && isNameOrAliasOfTable(rightTable, jc.righttable)) {
                lcjrs.add(jc);
                jc.used = true;
            } else if (isNameOrAliasOfTable(leftTable, jc.righttable)
                    && isNameOrAliasOfTable(rightTable, jc.lefttable)) {
                if (jc.jt == jointype.left)
                    jc.jt = jointype.right;
                else if (jc.jt == jointype.right)
                    jc.jt = jointype.left;

                lcjrs.add(jc);
                jc.used = true;
            } else if ((jc.lefttable == null)
                    && (isNameOrAliasOfTable(leftTable, jc.righttable) || isNameOrAliasOfTable(rightTable,
                    jc.righttable))) {
                // 'Y' = righttable.c1(+)
                lcjrs.add(jc);
                jc.used = true;
            } else if ((jc.righttable == null)
                    && (isNameOrAliasOfTable(leftTable, jc.lefttable) || isNameOrAliasOfTable(rightTable,
                    jc.lefttable))) {
                // lefttable.c1(+) = 'Y'
                if (jc.jt == jointype.left)
                    jc.jt = jointype.right;
                else if (jc.jt == jointype.right)
                    jc.jt = jointype.left;
                lcjrs.add(jc);
                jc.used = true;
            }
        }
        return lcjrs;
    }

    private void analyzeSelect(final TCustomSqlStatement stmt) {
        if (stmt instanceof TSelectSqlStatement) {
            final TSelectSqlStatement select = (TSelectSqlStatement) stmt;
            if (!select.isCombinedQuery()) {
                select.getStatements().forEachRemaining(it -> {
                    if (it instanceof TSelectSqlStatement) {
                        analyzeSelect(it);
                    }
                });

                if (select.tables.size() == 1)
                    return;

                int size = select.joins.size();
                if (select.getWhereClause() == null) {
                    if (select.tables.size() > 1) {
                        if (!hasJoin(select.joins)) {
                            /**
                             * cross join
                             */
                            StringBuffer parseSql = new StringBuffer();
                            parseSql.append(getFullNameWithAliasString(select.tables.getTable(0)));

                            for (int i = 1; i < select.tables.size(); i++) {
                                parseSql.append("\ncross join ")
                                        .append(getFullNameWithAliasString(select.tables.getTable(i)));
                            }

                            for (int k = size - 1; k > 0; k--) {
                                select.joins.removeJoin(k);
                            }
                            select.joins.getJoin(0).setString(parseSql.toString());
                        }
                    }
                } else {

                    getJoinConditionVisitor v = new getJoinConditionVisitor();

                    /**
                     * get join conditions
                     */
                    select.getWhereClause()
                            .getCondition()
                            .preOrderTraverse(v);
                    List<JoinCondition> jrs = v.getJrs();
                    TJoinItem item;
                    JoinCondition jr;
                    TJoin join;
                    if (select.joins != null && size > 0) {
                        for (int i = 0; i < size; i++) {
                            join = select.joins.getJoin(i);
                            for (int j = 0; j < join.getJoinItems().size(); j++) {
                                item = join.getJoinItems()
                                        .getJoinItem(j);
                                jr = new JoinCondition();

                                jr.expr = item.getOnCondition();
                                jr.used = false;
                                jr.lexpr = jr.expr.getLeftOperand();
                                jr.rexpr = jr.expr.getRightOperand();
                                jr.lefttable = getExpressionTable(jr.lexpr);
                                jr.righttable = getExpressionTable(jr.rexpr);

                                if (item.getJoinType() == EJoinType.inner) {
                                    jr.jt = jointype.inner;
                                } else if (item.getJoinType() == EJoinType.left
                                        || item.getJoinType() == EJoinType.leftouter) {
                                    jr.jt = jointype.left;
                                } else if (item.getJoinType() == EJoinType.right
                                        || item.getJoinType() == EJoinType.rightouter) {
                                    jr.jt = jointype.right;
                                } else if (item.getJoinType() == EJoinType.full
                                        || item.getJoinType() == EJoinType.fullouter) {
                                    jr.jt = jointype.full;
                                } else if (item.getJoinType() == EJoinType.join) {
                                    jr.jt = jointype.join;
                                } else if (item.getJoinType() == EJoinType.cross) {
                                    jr.jt = jointype.cross;
                                }
                                jrs.add(jr);
                            }
                        }
                    }

                    List<TTable> tables = new ArrayList<>();
                    for (int i = 0; i < select.tables.size(); i++) {
                        tables.add(select.tables.getTable(i));
                    }

                    //TODO 死循环
                    TCustomSqlStatement parentStmt = select.getParentStmt();
                    while (parentStmt != null) {
                        parentStmt = parentStmt.getParentStmt();
                        if (parentStmt instanceof TSelectSqlStatement) {
                            TSelectSqlStatement temp = (TSelectSqlStatement) parentStmt;
                            for (int i = 0; i < temp.tables.size(); i++) {
                                tables.add(temp.tables.getTable(i));
                            }
                        }
                    }

                    // Console.WriteLine(jrs.Count);
                    AtomicInteger index = new AtomicInteger(0);
                    Boolean tableUsed[] = new Boolean[tables.size()];
                    tables.parallelStream().forEach(table -> tableUsed[index.getAndIncrement()] = false);

                    // make first table to be the left most joined table
                    StringBuilder fromClause = new StringBuilder(getFullNameWithAliasString(tables.get(0)));

                    tableUsed[0] = true;
                    final List<FromClause> fromClauses = new ArrayList<>();

                    for (; ; ) {
                        for (int i = 0; i < tables.size(); i++) {
                            TTable lcTable1 = tables.get(i);

                            TTable leftTable = null, rightTable = null;
                            for (int j = i + 1; j < tables.size(); j++) {
                                TTable lcTable2 = tables.get(j);
                                if (areTableJoined(lcTable1, lcTable2, jrs)) {
                                    if (tableUsed[i] && (!tableUsed[j])) {
                                        leftTable = lcTable1;
                                        rightTable = lcTable2;
                                    } else if ((!tableUsed[i]) && tableUsed[j]) {
                                        leftTable = lcTable2;
                                        rightTable = lcTable1;
                                    }

                                    if ((leftTable != null)
                                            && (rightTable != null)) {
                                        List<JoinCondition> lcjrs = getJoinCondition(leftTable,
                                                rightTable,
                                                jrs);
                                        if (lcjrs.isEmpty())
                                            continue;
                                        FromClause fc = new FromClause();
                                        fc.table = leftTable;
                                        fc.joinTable = rightTable;
                                        fc.joinClause = getJoinType(lcjrs);
                                        StringBuilder condition = new StringBuilder();
                                        for (int k = 0; k < lcjrs.size(); k++) {
                                            condition.append(lcjrs.get(k).expr.toString());
                                            if (k != lcjrs.size() - 1) {
                                                condition.append(" and ");
                                            }
                                            TExpression lc_expr = lcjrs.get(k).expr;
                                            lc_expr.remove2();
                                        }
                                        fc.condition = condition.toString();

                                        fromClauses.add(fc);
                                        tableUsed[i] = true;
                                        tableUsed[j] = true;
                                    }
                                }
                            }
                        }
                        break;
                    }

                    // are all join conditions used?
                    for (JoinCondition jc : jrs) {
                        if (!jc.used) {
                            for (int j = fromClauses.size() - 1; j >= 0; j--) {
                                if (isNameOrAliasOfTable(fromClauses.get(j).joinTable,
                                        jc.lefttable)
                                        || isNameOrAliasOfTable(fromClauses.get(j).joinTable,
                                        jc.righttable)) {
                                    fromClauses.get(j).condition += " and "
                                            + jc.expr.toString();
                                    jc.used = true;
                                    jc.expr.remove2();
                                    break;
                                }
                            }
                        }
                    }

                    for (int i = 0; i < select.tables.size(); i++) {
                        if (!tableUsed[i]) {
                            ErrorNo++;
                            ErrorMessage += String.format("%sError %d, Message: %s",
                                    System.getProperty("line.separator"),
                                    ErrorNo,
                                    "This table has no join condition: "
                                            + select.tables.getTable(i)
                                            .getFullName());
                        }
                    }

                    fromClauses.sort(new Comparator<FromClause>() {

                        public int compare(FromClause o1, FromClause o2) {
                            return indexOf(select, o1.joinTable)
                                    - indexOf(select, o2.joinTable);
                        }

                        private int indexOf(
                                TSelectSqlStatement select,
                                TTable joinTable) {
                            TTableList tables = select.tables;
                            for (int i = 0; i < tables.size(); i++) {
                                if (joinTable != null
                                        && tables.getTable(i)
                                        .equals(joinTable))
                                    return i;
                            }
                            return -1;
                        }
                    });

                    fromClauses.sort((o1, o2) -> {
                        if (o1.table.equals(o2.joinTable))
                            return 1;
                        else if (o2.table.equals(o1.joinTable))
                            return -1;
                        else
                            return fromClauses.indexOf(o1)
                                    - fromClauses.indexOf(o2);
                    });

                    // add other join tables
                    for (FromClause fc : fromClauses) {
                        TTable leftTable = fc.table;
                        TTable joinTable = fc.joinTable;
                        String condition = fc.condition;
                        fc.joinTableOthers = new HashSet();
                        String[] conditionArray = condition.replace(".", ".,_,").replace(" ", ",_,").split(",_,");
                        for (String conditionStr : conditionArray) {
                            if (conditionStr.endsWith(".") && conditionStr.length() > 1) {
                                String tableName = conditionStr.substring(0, conditionStr.length() - 1);
                                if (!isNameOrAliasOfTable(leftTable, tableName) && !isNameOrAliasOfTable(joinTable, tableName)) {
                                    for (TTable table : tables) {
                                        if (isNameOrAliasOfTable(table, tableName)) {
                                            fc.joinTableOthers.add(table);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // sort by other join tables
                    fromClauses.sort((o1, o2) -> {
                        if (o1.joinTableOthers.contains(o2.table) || o1.joinTableOthers.contains(o2.joinTable))
                            return 1;
                        else if (o2.joinTableOthers.contains(o1.table) || o2.joinTableOthers.contains(o1.joinTable))
                            return -1;
                        else
                            return fromClauses.indexOf(o1)
                                    - fromClauses.indexOf(o2);
                    });
                    // link all join clause
                    for (FromClause fc : fromClauses) {
                        fromClause
                                .append("\n")
                                .append(fc.joinClause)
                                .append(" ")
                                .append(getFullNameWithAliasString(fc.joinTable))
                                .append(" on ")
                                .append(fc.condition);
                    }

                    for (int k = size - 1; k > 0; k--) {
                        select.joins.removeJoin(k);
                    }

                    select.joins.getJoin(0).fastSetString(fromClause.toString());

                    if ((select.getWhereClause()
                            .getCondition()
                            .getStartToken() == null)
                            || (select.getWhereClause()
                            .getCondition()
                            .toString()
                            .trim()
                            .length() == 0)) {
                        // no where condition, remove WHERE keyword
                        select.getWhereClause().fastSetString(" ");

                    } else {
                        select.getWhereClause().getCondition().fastSetString(select.getWhereClause()
                                .getCondition()
                                .toString()
                                .trim());
                    }
                }
            } else {
                analyzeSelect(select.getLeftStmt());
                analyzeSelect(select.getRightStmt());
            }
        } else if (stmt.getStatements() != null) {
            for (int i = 0; i < stmt.getStatements().size(); i++) {
                analyzeSelect(stmt.getStatements().get(i));
            }
        }
    }

    private boolean hasJoin(TJoinList joins) {
        if (joins == null)
            return false;
        for (int i = 0; i < joins.size(); i++) {
            TJoinItemList joinItems = joins.getJoin(i).getJoinItems();
            //TODO 修改cross join不能识别的bug
//            if (joinItems != null && joinItems.size() > 0)
//                return true;
            if (null != joinItems && joinItems.size() > 0) {
                for (int j = 0; j < joinItems.size(); j++) {
                    if (null == joinItems.getJoinItem(j).toString()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * get full name of table
     *
     * @param table
     * @return
     */
    private String getFullNameWithAliasString(TTable table) {
        if (table.getSubquery() != null) {
            if (table.getAliasClause() != null) {
                return table.getSubquery()
                        + " "
                        + table.getAliasClause().toString();
            } else {
                return table.getSubquery().toString();
            }
        } else if (table.getFullName() != null)
            return table.getFullNameWithAliasString();
        else
            return table.toString();
    }

    private String getExpressionTable(TExpression expr) {
        if (expr.getExpressionType() == EExpressionType.function_t) {
            for (int i = 0; i < expr.getFunctionCall().getArgs().size(); i++) {
                String table = getExpressionTable(expr.getFunctionCall()
                        .getArgs()
                        .getExpression(i));
                if (table != null)
                    return table;
            }
        } else if (expr.getObjectOperand() != null)
            return expr.getObjectOperand().getObjectString();
        else if (expr.getLeftOperand() != null
                && expr.getLeftOperand().getObjectOperand() != null)
            return expr.getLeftOperand().getObjectOperand().getObjectString();
        else if (expr.getRightOperand() != null
                && expr.getRightOperand().getObjectOperand() != null)
            return expr.getRightOperand()
                    .getObjectOperand()
                    .getObjectString();

        return null;
    }

    enum jointype {
        inner, left, right, cross, join, full
    }

    class FromClause {

        TTable table;
        TTable joinTable;
        HashSet joinTableOthers;
        String joinClause;
        String condition;
    }

    class JoinCondition {
        public String lefttable, righttable, leftcolumn, rightcolumn;
        public jointype jt;
        public Boolean used;
        public TExpression lexpr, rexpr, expr;
    }

    class getJoinConditionVisitor implements IExpressionVisitor {

        Boolean isFirstExpr = true;
        List<JoinCondition> jrs = new ArrayList<>();

        List<JoinCondition> getJrs() {
            return jrs;
        }

        boolean is_compare_condition(EExpressionType t) {
            return ((t == EExpressionType.simple_comparison_t)
                    || (t == EExpressionType.group_comparison_t)
                    || (t == EExpressionType.in_t) || (t == EExpressionType.pattern_matching_t));
        }

        TExpression getCompareCondition(TExpression expr) {
            if (is_compare_condition(expr.getExpressionType()))
                return expr;
            TExpression parentExpr = expr.getParentExpr();
            if (parentExpr == null)
                return null;
            return getCompareCondition(parentExpr);
        }

        private void analyzeJoinCondition(TExpression expr,
                                          TExpression parent_expr) {
            TExpression slexpr, srexpr, lc_expr = expr;

            if (lc_expr.getGsqlparser().getDbVendor() == EDbVendor.dbvmssql) {
                if (lc_expr.getExpressionType() == EExpressionType.left_join_t
                        || lc_expr.getExpressionType() == EExpressionType.right_join_t) {
                    analyzeMssqlJoinCondition(lc_expr);
                }
            }

            slexpr = lc_expr.getLeftOperand();
            srexpr = lc_expr.getRightOperand();

            if (is_compare_condition(lc_expr.getExpressionType())) {

                if (slexpr.isOracleOuterJoin() || srexpr.isOracleOuterJoin()) {
                    JoinCondition jr = new JoinCondition();
                    jr.used = false;
                    jr.lexpr = slexpr;
                    jr.rexpr = srexpr;
                    jr.expr = expr;
                    if (slexpr.isOracleOuterJoin()) {
                        // If the plus is on the left, the join type is right
                        // out join.
                        jr.jt = jointype.right;
                        // remove (+)
                        slexpr.getEndToken().setString("");
                    }
                    if (srexpr.isOracleOuterJoin()) {
                        // If the plus is on the right, the join type is left
                        // out join.
                        jr.jt = jointype.left;
                        srexpr.getEndToken().setString("");
                    }

                    jr.lefttable = getExpressionTable(slexpr);
                    jr.righttable = getExpressionTable(srexpr);

                    jrs.add(jr);
                    // System.out.printf( "join condition: %s\n", expr.toString(
                    // ) );
                } else if ((slexpr.getExpressionType() == EExpressionType.simple_object_name_t)
                        && (!slexpr.toString().startsWith(":"))
                        && (!slexpr.toString().startsWith("?"))
                        && (srexpr.getExpressionType() == EExpressionType.simple_object_name_t)
                        && (!srexpr.toString().startsWith(":"))
                        && (!srexpr.toString().startsWith("?"))) {
                    JoinCondition jr = new JoinCondition();
                    jr.used = false;
                    jr.lexpr = slexpr;
                    jr.rexpr = srexpr;
                    jr.expr = expr;
                    jr.jt = jointype.inner;
                    jr.lefttable = getExpressionTable(slexpr);
                    jr.righttable = getExpressionTable(srexpr);
                    jrs.add(jr);
                    // System.out.printf(
                    // "join condition: %s, %s:%d, %s:%d, %s\n",
                    // expr.toString( ),
                    // slexpr.toString( ),
                    // slexpr.getExpressionType( ),
                    // srexpr.toString( ),
                    // srexpr.getExpressionType( ),
                    // srexpr.getObjectOperand( ).getObjectType( ) );
                } else {
                    // not a join condition
                }

            } else if (slexpr != null
                    && slexpr.isOracleOuterJoin()
                    && srexpr == null) {
                JoinCondition jr = new JoinCondition();
                jr.used = false;
                jr.lexpr = slexpr;
                jr.rexpr = srexpr;
                jr.expr = expr;

                jr.jt = jointype.right;
                // remove (+)
                slexpr.getEndToken().setString("");

                jr.lefttable = getExpressionTable(slexpr);
                jr.righttable = null;

                jrs.add(jr);
            } else if (lc_expr.isOracleOuterJoin()
                    && parent_expr != null
                    && !is_compare_condition(parent_expr.getExpressionType())) {
                TExpression expression = getCompareCondition(parent_expr);
                if (expression != null) {
                    slexpr = expression.getLeftOperand();
                    srexpr = expression.getRightOperand();

                    JoinCondition jr = new JoinCondition();
                    jr.used = false;
                    jr.lexpr = slexpr;
                    jr.rexpr = srexpr;
                    jr.expr = expression;
                    lc_expr.getEndToken().setString("");
                    if (slexpr.getEndToken().posinlist >= lc_expr.getStartToken().posinlist) {
                        jr.jt = jointype.right;
                    } else {
                        jr.jt = jointype.left;
                    }

                    jr.lefttable = getExpressionTable(slexpr);
                    jr.righttable = getExpressionTable(srexpr);

                    jrs.add(jr);
                }
            }
        }

        private void analyzeMssqlJoinCondition(TExpression expr) {
            TExpression slexpr = expr.getLeftOperand();
            TExpression srexpr = expr.getRightOperand();

            JoinCondition jr = new JoinCondition();
            jr.used = false;
            jr.lexpr = slexpr;
            jr.rexpr = srexpr;
            jr.expr = expr;
            expr.getOperatorToken().setString("=");
            if (expr.getExpressionType() == EExpressionType.left_join_t) {
                // If the plus is on the left, the join type is right
                // out join.
                jr.jt = jointype.left;
                // remove (+)
                // slexpr.getEndToken( ).setString( "" );
            }
            if (expr.getExpressionType() == EExpressionType.right_join_t) {
                // If the plus is on the right, the join type is left
                // out join.
                jr.jt = jointype.right;
                // srexpr.getEndToken( ).setString( "" );
            }

            jr.lefttable = getExpressionTable(slexpr);
            jr.righttable = getExpressionTable(srexpr);

            jrs.add(jr);

        }

        public boolean exprVisit(TParseTreeNode pNode, boolean isLeafNode) {
            TExpression expr = (TExpression) pNode;
            if (expr.getExpressionType() == EExpressionType.function_t) {
                for (int i = 0; i < expr.getFunctionCall().getArgs().size(); i++) {
                    analyzeJoinCondition(expr.getFunctionCall()
                            .getArgs()
                            .getExpression(i), expr);
                    if (isLeafNode) {
                        exprVisit(expr.getFunctionCall()
                                .getArgs()
                                .getExpression(i), isLeafNode);
                    }
                }
            } else {
                analyzeJoinCondition(expr, null);
            }
            return true;

        }

    }
}