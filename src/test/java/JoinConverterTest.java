//import gudusoft.gsqlparser.*;
//import gudusoft.gsqlparser.nodes.*;
//import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
//import junit.framework.TestCase;
//
//import java.io.*;
//import java.util.*;
//
//public class JoinConverterTest extends TestCase {
//
//
//    private String ErrorMessage = "";
//    private int ErrorNo;
//    private String query;
//    private EDbVendor vendor;
//
//    public JoinConverterTest(String sql, EDbVendor vendor) {
//        this.query = sql;
//        this.vendor = vendor;
//    }
//
//    public static void testOracleSql2() {
//        EDbVendor vendor = EDbVendor.dbvoracle;
//        String sql = "SELECT COUNT(1)\n" +
//                "  FROM (SELECT PRODID\n" +
//                "          FROM (SELECT T.ID PRODID, CD.ID CID\n" +
//                "                  FROM PRODUCT          T,                      \n" +
//                "                  CD            CD\n" +
//                "                 WHERE T.ID=CD.ID)\n" +
//                "         GROUP BY PRODID\n" +
//                "        HAVING COUNT(*) > 1);\n";
//        JoinConverterTest joinConverter = new JoinConverterTest(sql, vendor);
//        assertTrue(joinConverter.convert() == 0);
//    }
//
//    public static void testOracleSql3() {
//        EDbVendor vendor = EDbVendor.dbvoracle;
//        String sql = "SELECT\n" +
//                " *\n" +
//                "FROM\n" +
//                " ai_course a\n" +
//                "WHERE\n" +
//                " a.algorithmt(+) = (\n" +
//                "  SELECT\n" +
//                "   b.coverUrl\n" +
//                "  FROM\n" +
//                "   ai_course_dataset_relation b, ai_course a\n" +
//                "  WHERE\n" +
//                "   b.course_id = a.id\n" +
//                "   and a.id=?\n" +
//                " )";
//        JoinConverterTest joinConverter = new JoinConverterTest(sql, vendor);
//        assertTrue(joinConverter.convert() == 0);
//    }
//
//    public static void testOracleSql4() {
//        EDbVendor vendor = EDbVendor.dbvoracle;
//        String sql = "SELECT e.employee_id,\n" +
//                "       e.last_name,\n" +
//                "       e.department_id\n" +
//                "FROM   employees e,\n" +
//                "       departments d\n" +
//                "WHERE  e.department_id = d.department_id \n";
//        JoinConverterTest joinConverter = new JoinConverterTest(sql, vendor);
//        assertTrue(joinConverter.convert() == 0);
//    }
//
//    public static void testOracleSql5() {
//        EDbVendor vendor = EDbVendor.dbvoracle;
//        String sql = "CREATE\n" +
//                "OR REPLACE PROCEDURE myDemo01 IS aa number (10);\n" +
//                "BEGIN\n" +
//                "\tSELECT e.employee_id INTO aa FROM employees e, departments d WHERE e.department_id = d.department_id;\n" +
//                "\tdbms_output.put_line (aa);\n" +
//                "END myDemo01;";
//        JoinConverterTest joinConverter = new JoinConverterTest(sql, vendor);
//        assertTrue(joinConverter.convert() == 0);
//    }
//
//    public static void testOracleSql6() {
//        EDbVendor vendor = EDbVendor.dbvoracle;
//        String sql = "SELECT\n" +
//                " *\n" +
//                "FROM\n" +
//                " ai_course a\n" +
//                "WHERE\n" +
//                " a.algorithmt(+) = (\n" +
//                "  SELECT\n" +
//                "   b.coverUrl\n" +
//                "  FROM\n" +
//                "   ai_course_dataset_relation b, ai_course a\n" +
//                "  WHERE\n" +
//                "   b.course_id(+) = a.id\n" +
//                "   and \n" +
//                "   a.id=?\n" +
//                " )\n";
//        JoinConverterTest joinConverter = new JoinConverterTest(sql, vendor);
//        assertTrue(joinConverter.convert() == 0);
//    }
//
//    public static void testSqlServerSql1() {
//        EDbVendor vendor = EDbVendor.dbvmssql;
//        String sql = "SELECT  *\n" +
//                "FROM    TableA AS A, TableB AS b\n" +
//                "WHERE   A.ColA *= B.ColB;";
//        JoinConverterTest joinConverter = new JoinConverterTest(sql, vendor);
//        assertTrue(joinConverter.convert() == 0);
//    }
//
//    public static void testSqlServerSql2() {
//        EDbVendor vendor = EDbVendor.dbvmssql;
//        String sql = "SELECT m.*, \n" +
//                "       altname.last_name  last_name_student, \n" +
//                "       altname.first_name first_name_student, \n" +
//                "       ccu.date_joined, \n" +
//                "       ccu.last_login, \n" +
//                "       ccu.photo_id, \n" +
//                "       ccu.last_updated \n" +
//                "FROM   summit.mstr m, \n" +
//                "       summit.alt_name altname, \n" +
//                "       smmtccon.ccn_user ccu \n" +
//                "WHERE  m.id =?\n" +
//                "       AND m.id *= altname.id \n" +
//                "       AND m.id =* ccu.id \n" +
//                "       AND altname.grad_name_ind *= ?\n";
//        JoinConverterTest joinConverter = new JoinConverterTest(sql, vendor);
//        assertTrue(joinConverter.convert() == 0);
//    }
//
//    public static void testSqlServerSql3() {
//        EDbVendor vendor = EDbVendor.dbvmssql;
//        String sql = "if (exists (select * from sys.objects where name = 'GetUser')) drop proc GetUser  \n" +
//                "go\n" +
//                "create proc GetUser  \n" +
//                "@Id int \n" +
//                "as \n" +
//                "set nocount on;  \n" +
//                "begin \n" +
//                "\tSELECT e.employee_id,\n" +
//                "\t       e.last_name,\n" +
//                "\t       e.department_id\n" +
//                "\tFROM   employees e,\n" +
//                "\t       departments d\n" +
//                "\tWHERE  e.department_id *= d.department_id and e.Id=@Id;\n" +
//                "end;\n";
//        JoinConverterTest joinConverter = new JoinConverterTest(sql, vendor);
//        assertTrue(joinConverter.convert() == 0);
//    }
//
//    public static void testSqlServerSql4() {
//        EDbVendor vendor = EDbVendor.dbvmssql;
//        String sql = "SELECT\n" +
//                " *\n" +
//                "FROM\n" +
//                " ai_course a\n" +
//                "WHERE\n" +
//                " a.algorithmt(+) = (\n" +
//                "  SELECT\n" +
//                "   b.coverUrl\n" +
//                "  FROM\n" +
//                "   ai_course_dataset_relation b, ai_course a\n" +
//                "  WHERE\n" +
//                "   b.course_id *= a.id\n" +
//                "   and \n" +
//                "   a.id=?\n" +
//                " )\n";
//        JoinConverterTest joinConverter = new JoinConverterTest(sql, vendor);
//        assertTrue(joinConverter.convert() == 0);
//    }
//
//    public static void testSqlServerSql5() {
//        EDbVendor vendor = EDbVendor.dbvmssql;
//        String sql = "INSERT INTO T select  \n" +
//                "    b.Amount\n" +
//                "from \n" +
//                "    TableA a, TableB b\n" +
//                "where \n" +
//                "    a.inv_no *= b.inv_no";
//        JoinConverterTest joinConverter = new JoinConverterTest(sql, vendor);
//        assertTrue(joinConverter.convert() == 0);
//    }
//
//    public static String getFileContent(File file) {
//        try {
//            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
//            byte[] tmp = new byte[4096];
//            InputStream is = new BufferedInputStream(new FileInputStream(file));
//            while (true) {
//                int r = is.read(tmp);
//                if (r == -1)
//                    break;
//                out.write(tmp, 0, r);
//            }
//            byte[] bytes = out.toByteArray();
//            is.close();
//            out.close();
//            String content = new String(bytes);
//            return content.trim();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    public void testOracleSql1() {
//        EDbVendor vendor = EDbVendor.dbvoracle;
//        String sql = "INSERT INTO T SELECT e.employee_id,\n" +
//                "       e.last_name,\n" +
//                "       e.department_id\n" +
//                "FROM   employees e,\n" +
//                "       departments d\n" +
//                "WHERE  e.department_id(+) = d.department_id\n";
//        JoinConverterTest joinConverter = new JoinConverterTest(sql, vendor);
//        assertTrue(joinConverter.convert() == 0);
//    }
//
//    public String getErrorMessage() {
//        return ErrorMessage;
//    }
//
//    public String getQuery() {
//        // remove blank line from query
//        return this.query.replaceAll("(?m)^[ \t]*\r?\n", "");
//    }
//
//    public int convert() {
//
//        TGSqlParser sqlparser = new TGSqlParser(vendor);
//        sqlparser.sqltext = this.query;
//        ErrorNo = sqlparser.parse();
//        if (ErrorNo != 0) {
//            ErrorMessage = sqlparser.getErrormessage();
//            return ErrorNo;
//        }
//
//        TCustomSqlStatement stmt = sqlparser.sqlstatements.get(0);
//        analyzeSelect(stmt);
//        this.query = stmt.toString();
//        return ErrorNo;
//    }
//
//    private boolean isNameOfTable(TTable table, String name) {
//        return (name == null) ? false : table.getName()
//                .equalsIgnoreCase(name);
//    }
//
//    private boolean isAliasOfTable(TTable table, String alias) {
//        if (table.getAliasClause() == null) {
//            return false;
//        } else
//            return (alias == null) ? false : table.getAliasClause()
//                    .toString()
//                    .equalsIgnoreCase(alias);
//    }
//
//    private boolean isNameOrAliasOfTable(TTable table, String str) {
//        return isAliasOfTable(table, str) || isNameOfTable(table, str);
//    }
//
//    private boolean areTableJoined(TTable lefttable, TTable righttable,
//                                   ArrayList<JoinCondition> jrs) {
//
//        boolean ret = false;
//
//        for (int i = 0; i < jrs.size(); i++) {
//            JoinCondition jc = jrs.get(i);
//            if (jc.used) {
//                continue;
//            }
//            ret = isNameOrAliasOfTable(lefttable, jc.lefttable)
//                    && isNameOrAliasOfTable(righttable, jc.righttable);
//            if (ret)
//                break;
//            ret = isNameOrAliasOfTable(lefttable, jc.righttable)
//                    && isNameOrAliasOfTable(righttable, jc.lefttable);
//            if (ret)
//                break;
//        }
//
//        return ret;
//    }
//
//    private String getJoinType(ArrayList<JoinCondition> jrs) {
//        String str = "inner join";
//        for (int i = 0; i < jrs.size(); i++) {
//            if (jrs.get(i).jt == jointype.left) {
//                str = "left outer join";
//                break;
//            } else if (jrs.get(i).jt == jointype.right) {
//                str = "right outer join";
//                break;
//            } else if (jrs.get(i).jt == jointype.full) {
//                str = "full outer join";
//                break;
//            } else if (jrs.get(i).jt == jointype.cross) {
//                str = "cross join";
//                break;
//            } else if (jrs.get(i).jt == jointype.join) {
//                str = "join";
//                break;
//            }
//        }
//
//        return str;
//    }
//
//    private ArrayList<JoinCondition> getJoinCondition(TTable lefttable,
//                                                      TTable righttable, ArrayList<JoinCondition> jrs) {
//        ArrayList<JoinCondition> lcjrs = new ArrayList<JoinCondition>();
//        for (int i = 0; i < jrs.size(); i++) {
//            JoinCondition jc = jrs.get(i);
//            if (jc.used) {
//                continue;
//            }
//
//            if (isNameOrAliasOfTable(lefttable, jc.lefttable)
//                    && isNameOrAliasOfTable(righttable, jc.righttable)) {
//                lcjrs.add(jc);
//                jc.used = true;
//            } else if (isNameOrAliasOfTable(lefttable, jc.righttable)
//                    && isNameOrAliasOfTable(righttable, jc.lefttable)) {
//                if (jc.jt == jointype.left)
//                    jc.jt = jointype.right;
//                else if (jc.jt == jointype.right)
//                    jc.jt = jointype.left;
//
//                lcjrs.add(jc);
//                jc.used = true;
//            } else if ((jc.lefttable == null)
//                    && (isNameOrAliasOfTable(lefttable, jc.righttable) || isNameOrAliasOfTable(righttable,
//                    jc.righttable))) {
//                // 'Y' = righttable.c1(+)
//                lcjrs.add(jc);
//                jc.used = true;
//            } else if ((jc.righttable == null)
//                    && (isNameOrAliasOfTable(lefttable, jc.lefttable) || isNameOrAliasOfTable(righttable,
//                    jc.lefttable))) {
//                // lefttable.c1(+) = 'Y'
//                if (jc.jt == jointype.left)
//                    jc.jt = jointype.right;
//                else if (jc.jt == jointype.right)
//                    jc.jt = jointype.left;
//                lcjrs.add(jc);
//                jc.used = true;
//            }
//        }
//        return lcjrs;
//    }
//
//    private void analyzeSelect(final TCustomSqlStatement stmt) {
//        if (stmt instanceof TSelectSqlStatement) {
//            final TSelectSqlStatement select = (TSelectSqlStatement) stmt;
//            if (!select.isCombinedQuery()) {
//                for (int i = 0; i < select.getStatements().size(); i++) {
//                    if (select.getStatements().get(i) instanceof TSelectSqlStatement) {
//                        analyzeSelect((TSelectSqlStatement) select.getStatements()
//                                .get(i));
//                    }
//                }
//
//                if (select.tables.size() == 1)
//                    return;
//
//                if (select.getWhereClause() == null) {
//                    if (select.tables.size() > 1) {
//                        if (!hasJoin(select.joins)) {
//                            // cross join
//                            String str = getFullNameWithAliasString(select.tables.getTable(0));
//                            for (int i = 1; i < select.tables.size(); i++) {
//                                str = str
//                                        + "\ncross join "
//                                        + getFullNameWithAliasString(select.tables.getTable(i));
//                            }
//
//                            for (int k = select.joins.size() - 1; k > 0; k--) {
//                                select.joins.removeJoin(k);
//                            }
//                            select.joins.getJoin(0).setString(str);
//                        }
//                    }
//                } else {
//
//                    getJoinConditionVisitor v = new getJoinConditionVisitor();
//
//                    // get join conditions
//                    select.getWhereClause()
//                            .getCondition()
//                            .preOrderTraverse(v);
//                    ArrayList<JoinCondition> jrs = v.getJrs();
//
//                    if (select.joins != null && select.joins.size() > 0) {
//                        for (int i = 0; i < select.joins.size(); i++) {
//                            TJoin join = select.joins.getJoin(i);
//                            for (int j = 0; j < join.getJoinItems().size(); j++) {
//                                TJoinItem item = join.getJoinItems()
//                                        .getJoinItem(j);
//                                JoinCondition jr = new JoinCondition();
//                                jr.expr = item.getOnCondition();
//                                jr.used = false;
//                                jr.lexpr = jr.expr.getLeftOperand();
//                                jr.rexpr = jr.expr.getRightOperand();
//                                jr.lefttable = getExpressionTable(jr.lexpr);
//                                jr.righttable = getExpressionTable(jr.rexpr);
//                                if (item.getJoinType() == EJoinType.inner) {
//                                    jr.jt = jointype.inner;
//                                    jrs.add(jr);
//                                }
//                                if (item.getJoinType() == EJoinType.left
//                                        || item.getJoinType() == EJoinType.leftouter) {
//                                    jr.jt = jointype.left;
//                                    jrs.add(jr);
//                                }
//                                if (item.getJoinType() == EJoinType.right
//                                        || item.getJoinType() == EJoinType.rightouter) {
//                                    jr.jt = jointype.right;
//                                    jrs.add(jr);
//                                }
//                                if (item.getJoinType() == EJoinType.full
//                                        || item.getJoinType() == EJoinType.fullouter) {
//                                    jr.jt = jointype.full;
//                                    jrs.add(jr);
//                                }
//                                if (item.getJoinType() == EJoinType.join) {
//                                    jr.jt = jointype.join;
//                                    jrs.add(jr);
//                                }
//                                if (item.getJoinType() == EJoinType.cross) {
//                                    jr.jt = jointype.cross;
//                                    jrs.add(jr);
//                                }
//                            }
//                        }
//                    }
//
//                    List<TTable> tables = new ArrayList<TTable>();
//                    for (int i = 0; i < select.tables.size(); i++) {
//                        tables.add(select.tables.getTable(i));
//                    }
//
//                    TCustomSqlStatement parentStmt = select;
//                    while (parentStmt.getParentStmt() != null) {
//                        parentStmt = select.getParentStmt();
//                        if (parentStmt instanceof TSelectSqlStatement) {
//                            TSelectSqlStatement temp = (TSelectSqlStatement) parentStmt;
//                            for (int i = 0; i < temp.tables.size(); i++) {
//                                tables.add(temp.tables.getTable(i));
//                            }
//                        }
//                    }
//
//                    // Console.WriteLine(jrs.Count);
//                    boolean tableUsed[] = new boolean[tables.size()];
//                    for (int i = 0; i < tables.size(); i++) {
//                        tableUsed[i] = false;
//                    }
//
//                    // make first table to be the left most joined table
//                    String fromclause = getFullNameWithAliasString(tables.get(0));
//
//                    tableUsed[0] = true;
//                    boolean foundTableJoined;
//                    final ArrayList<FromClause> fromClauses = new ArrayList<FromClause>();
//                    for (; ; ) {
//                        foundTableJoined = false;
//
//                        for (int i = 0; i < tables.size(); i++) {
//                            TTable lcTable1 = tables.get(i);
//
//                            TTable leftTable = null, rightTable = null;
//                            for (int j = i + 1; j < tables.size(); j++) {
//                                TTable lcTable2 = tables.get(j);
//                                if (areTableJoined(lcTable1, lcTable2, jrs)) {
//                                    if (tableUsed[i] && (!tableUsed[j])) {
//                                        leftTable = lcTable1;
//                                        rightTable = lcTable2;
//                                    } else if ((!tableUsed[i]) && tableUsed[j]) {
//                                        leftTable = lcTable2;
//                                        rightTable = lcTable1;
//                                    }
////									System.out.println("leftTable:" + leftTable + ", rightTable:" + rightTable);
//
//                                    if ((leftTable != null)
//                                            && (rightTable != null)) {
//                                        ArrayList<JoinCondition> lcjrs = getJoinCondition(leftTable,
//                                                rightTable,
//                                                jrs);
//                                        if (lcjrs.isEmpty())
//                                            continue;
//                                        FromClause fc = new FromClause();
//                                        fc.table = leftTable;
//                                        fc.joinTable = rightTable;
//                                        fc.joinClause = getJoinType(lcjrs);
//                                        String condition = "";
//                                        for (int k = 0; k < lcjrs.size(); k++) {
//                                            condition += lcjrs.get(k).expr.toString();
//                                            if (k != lcjrs.size() - 1) {
//                                                condition += " and ";
//                                            }
//                                            TExpression lc_expr = lcjrs.get(k).expr;
//                                            lc_expr.remove2();
//                                        }
//                                        fc.condition = condition;
//
//                                        fromClauses.add(fc);
//                                        tableUsed[i] = true;
//                                        tableUsed[j] = true;
//
//                                        foundTableJoined = true;
//                                    }
//                                }
//                            }
//                        }
//
//                        if (!foundTableJoined) {
//                            break;
//                        }
//                    }
//
//                    // are all join conditions used?
//                    for (int i = 0; i < jrs.size(); i++) {
//                        JoinCondition jc = jrs.get(i);
//                        if (!jc.used) {
//                            for (int j = fromClauses.size() - 1; j >= 0; j--) {
//                                if (isNameOrAliasOfTable(fromClauses.get(j).joinTable,
//                                        jc.lefttable)
//                                        || isNameOrAliasOfTable(fromClauses.get(j).joinTable,
//                                        jc.righttable)) {
//                                    fromClauses.get(j).condition += " and "
//                                            + jc.expr.toString();
//                                    jc.used = true;
//                                    jc.expr.remove2();
//                                    break;
//                                }
//                            }
//                        }
//                    }
//
//                    for (int i = 0; i < select.tables.size(); i++) {
//                        if (!tableUsed[i]) {
//                            ErrorNo++;
//                            ErrorMessage += String.format("%sError %d, Message: %s",
//                                    System.getProperty("line.separator"),
//                                    ErrorNo,
//                                    "This table has no join condition: "
//                                            + select.tables.getTable(i)
//                                            .getFullName());
//                        }
//                    }
//
//                    Collections.sort(fromClauses,
//                            new Comparator<FromClause>() {
//
//                                public int compare(FromClause o1, FromClause o2) {
//                                    return indexOf(select, o1.joinTable)
//                                            - indexOf(select, o2.joinTable);
//                                }
//
//                                private int indexOf(
//                                        TSelectSqlStatement select,
//                                        TTable joinTable) {
//                                    TTableList tables = select.tables;
//                                    for (int i = 0; i < tables.size(); i++) {
//                                        if (joinTable != null
//                                                && tables.getTable(i)
//                                                .equals(joinTable))
//                                            return i;
//                                    }
//                                    return -1;
//                                }
//                            });
//
//                    Collections.sort(fromClauses,
//                            new Comparator<FromClause>() {
//
//                                public int compare(FromClause o1, FromClause o2) {
//                                    if (o1.table.equals(o2.joinTable))
//                                        return 1;
//                                    else if (o2.table.equals(o1.joinTable))
//                                        return -1;
//                                    else
//                                        return fromClauses.indexOf(o1)
//                                                - fromClauses.indexOf(o2);
//                                }
//                            });
//
//                    // add other join tables
//                    for (int i = 0; i < fromClauses.size(); i++) {
//                        FromClause fc = fromClauses.get(i);
//                        TTable leftTable = fc.table;
//                        TTable joinTable = fc.joinTable;
//                        String condition = fc.condition;
//                        fc.joinTableOthers = new HashSet();
//                        String[] conditionArray = condition.replace(".", ".,_,").replace(" ", ",_,").split(",_,");
//                        for (String conditionStr : conditionArray) {
//                            if (conditionStr.endsWith(".") && conditionStr.length() > 1) {
//                                String tableName = conditionStr.substring(0, conditionStr.length() - 1);
//                                if (!isNameOrAliasOfTable(leftTable, tableName) && !isNameOrAliasOfTable(joinTable, tableName)) {
//                                    for (TTable table : tables) {
//                                        if (isNameOrAliasOfTable(table, tableName)) {
//                                            fc.joinTableOthers.add(table);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    // sort by other join tables
//                    Collections.sort(fromClauses,
//                            new Comparator<FromClause>() {
//
//                                public int compare(FromClause o1, FromClause o2) {
//                                    if (o1.joinTableOthers.contains(o2.table) || o1.joinTableOthers.contains(o2.joinTable))
//                                        return 1;
//                                    else if (o2.joinTableOthers.contains(o1.table) || o2.joinTableOthers.contains(o1.joinTable))
//                                        return -1;
//                                    else
//                                        return fromClauses.indexOf(o1)
//                                                - fromClauses.indexOf(o2);
//                                }
//                            });
//                    // link all join clause
//                    for (int i = 0; i < fromClauses.size(); i++) {
//                        FromClause fc = fromClauses.get(i);
//                        fromclause += "\n"
//                                + fc.joinClause
//                                + " "
//                                + getFullNameWithAliasString(fc.joinTable)
//                                + " on "
//                                + fc.condition;
//                    }
//
//                    for (int k = select.joins.size() - 1; k > 0; k--) {
//                        select.joins.removeJoin(k);
//                    }
//
//                    select.joins.getJoin(0).setString(fromclause);
//
//                    if ((select.getWhereClause()
//                            .getCondition()
//                            .getStartToken() == null)
//                            || (select.getWhereClause()
//                            .getCondition()
//                            .toString()
//                            .trim()
//                            .length() == 0)) {
//                        // no where condition, remove WHERE keyword
//                        select.getWhereClause().fastSetString(" ");
//
//                    } else {
//                        select.getWhereClause().getCondition().fastSetString(select.getWhereClause()
//                                .getCondition()
//                                .toString()
//                                .trim());
//                    }
//                }
//            } else {
//                analyzeSelect(select.getLeftStmt());
//                analyzeSelect(select.getRightStmt());
//            }
//        } else if (stmt.getStatements() != null) {
//            for (int i = 0; i < stmt.getStatements().size(); i++) {
//                analyzeSelect(stmt.getStatements().get(i));
//            }
//        }
//    }
//
//    private boolean hasJoin(TJoinList joins) {
//        if (joins == null)
//            return false;
//        for (int i = 0; i < joins.size(); i++) {
//            if (joins.getJoin(i).getJoinItems() != null
//                    && joins.getJoin(i).getJoinItems().size() > 0)
//                return true;
//        }
//        return false;
//    }
//
//    private String getFullNameWithAliasString(TTable table) {
//        if (table.getSubquery() != null) {
//            if (table.getAliasClause() != null) {
//                return table.getSubquery()
//                        + " "
//                        + table.getAliasClause().toString();
//            } else {
//                return table.getSubquery().toString();
//            }
//        } else if (table.getFullName() != null)
//            return table.getFullNameWithAliasString();
//        else
//            return table.toString();
//    }
//
//    private String getExpressionTable(TExpression expr) {
//        if (expr.getExpressionType() == EExpressionType.function_t) {
//            for (int i = 0; i < expr.getFunctionCall().getArgs().size(); i++) {
//                String table = getExpressionTable(expr.getFunctionCall()
//                        .getArgs()
//                        .getExpression(i));
//                if (table != null)
//                    return table;
//            }
//        } else if (expr.getObjectOperand() != null)
//            return expr.getObjectOperand().getObjectString();
//        else if (expr.getLeftOperand() != null
//                && expr.getLeftOperand().getObjectOperand() != null)
//            return expr.getLeftOperand().getObjectOperand().getObjectString();
//        else if (expr.getRightOperand() != null
//                && expr.getRightOperand().getObjectOperand() != null)
//            return expr.getRightOperand()
//                    .getObjectOperand()
//                    .getObjectString();
//
//        return null;
//    }
//
//    enum jointype {
//        inner, left, right, cross, join, full
//    }
//
//    class FromClause {
//
//        TTable table;
//        TTable joinTable;
//        Set<TTable> joinTableOthers;
//        String joinClause;
//        String condition;
//    }
//
//    class JoinCondition {
//
//        public String lefttable, righttable, leftcolumn, rightcolumn;
//        public jointype jt;
//        public Boolean used;
//        public TExpression lexpr, rexpr, expr;
//    }
//
//    class getJoinConditionVisitor implements IExpressionVisitor {
//
//        Boolean isFirstExpr = true;
//        ArrayList<JoinCondition> jrs = new ArrayList<JoinCondition>();
//
//        public ArrayList<JoinCondition> getJrs() {
//            return jrs;
//        }
//
//        boolean is_compare_condition(EExpressionType t) {
//            return ((t == EExpressionType.simple_comparison_t)
//                    || (t == EExpressionType.group_comparison_t)
//                    || (t == EExpressionType.in_t) || (t == EExpressionType.pattern_matching_t));
//        }
//
//        TExpression getCompareCondition(TExpression expr) {
//            if (is_compare_condition(expr.getExpressionType()))
//                return expr;
//            TExpression parentExpr = expr.getParentExpr();
//            if (parentExpr == null)
//                return null;
//            return getCompareCondition(parentExpr);
//        }
//
//        private void analyzeJoinCondition(TExpression expr,
//                                          TExpression parent_expr) {
//            TExpression slexpr, srexpr, lc_expr = expr;
//
//            if (lc_expr.getGsqlparser().getDbVendor() == EDbVendor.dbvmssql) {
//                if (lc_expr.getExpressionType() == EExpressionType.left_join_t
//                        || lc_expr.getExpressionType() == EExpressionType.right_join_t) {
//                    analyzeMssqlJoinCondition(lc_expr);
//                }
//            }
//
//            slexpr = lc_expr.getLeftOperand();
//            srexpr = lc_expr.getRightOperand();
//
//            if (is_compare_condition(lc_expr.getExpressionType())) {
//
//                if (slexpr.isOracleOuterJoin() || srexpr.isOracleOuterJoin()) {
//                    JoinCondition jr = new JoinCondition();
//                    jr.used = false;
//                    jr.lexpr = slexpr;
//                    jr.rexpr = srexpr;
//                    jr.expr = expr;
//                    if (slexpr.isOracleOuterJoin()) {
//                        // If the plus is on the left, the join type is right
//                        // out join.
//                        jr.jt = jointype.right;
//                        // remove (+)
//                        slexpr.getEndToken().setString("");
//                    }
//                    if (srexpr.isOracleOuterJoin()) {
//                        // If the plus is on the right, the join type is left
//                        // out join.
//                        jr.jt = jointype.left;
//                        srexpr.getEndToken().setString("");
//                    }
//
//                    jr.lefttable = getExpressionTable(slexpr);
//                    jr.righttable = getExpressionTable(srexpr);
//
//                    jrs.add(jr);
//                    // System.out.printf( "join condition: %s\n", expr.toString(
//                    // ) );
//                } else if ((slexpr.getExpressionType() == EExpressionType.simple_object_name_t)
//                        && (!slexpr.toString().startsWith(":"))
//                        && (!slexpr.toString().startsWith("?"))
//                        && (srexpr.getExpressionType() == EExpressionType.simple_object_name_t)
//                        && (!srexpr.toString().startsWith(":"))
//                        && (!srexpr.toString().startsWith("?"))) {
//                    JoinCondition jr = new JoinCondition();
//                    jr.used = false;
//                    jr.lexpr = slexpr;
//                    jr.rexpr = srexpr;
//                    jr.expr = expr;
//                    jr.jt = jointype.inner;
//                    jr.lefttable = getExpressionTable(slexpr);
//                    jr.righttable = getExpressionTable(srexpr);
//                    jrs.add(jr);
//                    // System.out.printf(
//                    // "join condition: %s, %s:%d, %s:%d, %s\n",
//                    // expr.toString( ),
//                    // slexpr.toString( ),
//                    // slexpr.getExpressionType( ),
//                    // srexpr.toString( ),
//                    // srexpr.getExpressionType( ),
//                    // srexpr.getObjectOperand( ).getObjectType( ) );
//                } else {
//                    // not a join condition
//                }
//
//            } else if (slexpr != null
//                    && slexpr.isOracleOuterJoin()
//                    && srexpr == null) {
//                JoinCondition jr = new JoinCondition();
//                jr.used = false;
//                jr.lexpr = slexpr;
//                jr.rexpr = srexpr;
//                jr.expr = expr;
//
//                jr.jt = jointype.right;
//                // remove (+)
//                slexpr.getEndToken().setString("");
//
//                jr.lefttable = getExpressionTable(slexpr);
//                jr.righttable = null;
//
//                jrs.add(jr);
//            } else if (lc_expr.isOracleOuterJoin()
//                    && parent_expr != null
//                    && !is_compare_condition(parent_expr.getExpressionType())) {
//                TExpression expression = getCompareCondition(parent_expr);
//                if (expression != null) {
//                    slexpr = expression.getLeftOperand();
//                    srexpr = expression.getRightOperand();
//
//                    JoinCondition jr = new JoinCondition();
//                    jr.used = false;
//                    jr.lexpr = slexpr;
//                    jr.rexpr = srexpr;
//                    jr.expr = expression;
//                    lc_expr.getEndToken().setString("");
//                    if (slexpr.getEndToken().posinlist >= lc_expr.getStartToken().posinlist) {
//                        jr.jt = jointype.right;
//                    } else {
//                        jr.jt = jointype.left;
//                    }
//
//                    jr.lefttable = getExpressionTable(slexpr);
//                    jr.righttable = getExpressionTable(srexpr);
//
//                    jrs.add(jr);
//                }
//            }
//        }
//
//        private void analyzeMssqlJoinCondition(TExpression expr) {
//            TExpression slexpr = expr.getLeftOperand();
//            TExpression srexpr = expr.getRightOperand();
//
//            JoinCondition jr = new JoinCondition();
//            jr.used = false;
//            jr.lexpr = slexpr;
//            jr.rexpr = srexpr;
//            jr.expr = expr;
//            expr.getOperatorToken().setString("=");
//            if (expr.getExpressionType() == EExpressionType.left_join_t) {
//                // If the plus is on the left, the join type is right
//                // out join.
//                jr.jt = jointype.left;
//                // remove (+)
//                // slexpr.getEndToken( ).setString( "" );
//            }
//            if (expr.getExpressionType() == EExpressionType.right_join_t) {
//                // If the plus is on the right, the join type is left
//                // out join.
//                jr.jt = jointype.right;
//                // srexpr.getEndToken( ).setString( "" );
//            }
//
//            jr.lefttable = getExpressionTable(slexpr);
//            jr.righttable = getExpressionTable(srexpr);
//
//            jrs.add(jr);
//
//        }
//
//        public boolean exprVisit(TParseTreeNode pNode, boolean isLeafNode) {
//            TExpression expr = (TExpression) pNode;
//            if (expr.getExpressionType() == EExpressionType.function_t) {
//                for (int i = 0; i < expr.getFunctionCall().getArgs().size(); i++) {
//                    analyzeJoinCondition(expr.getFunctionCall()
//                            .getArgs()
//                            .getExpression(i), expr);
//                    if (isLeafNode) {
//                        exprVisit(expr.getFunctionCall()
//                                .getArgs()
//                                .getExpression(i), isLeafNode);
//                    }
//                }
//            } else {
//                analyzeJoinCondition(expr, null);
//            }
//            return true;
//
//        }
//
//    }
//}