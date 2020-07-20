package test;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;


public class SelectParse {

    private StringBuffer resultSql = new StringBuffer();

    public String getResultSql() {
        return resultSql.toString();
    }


    public boolean parse(String sql, EDbVendor dbVendor) {
        boolean flag = false;
        TGSqlParser sqlparser = new TGSqlParser(dbVendor);
        sqlparser.sqltext = sql;
        int ret = sqlparser.parse();

        if (ret == 0) {
            SelectVisitor selectVisitor = new SelectVisitor();
            sqlparser.sqlstatements.forEachRemaining(sqlStatement -> sqlStatement.accept(selectVisitor));
            flag = true;

            selectVisitor.getMap().forEach((k, v) -> resultSql.append("\n")
                    .append(k)
                    .append("->>>>")
                    .append(v));
        }
        return flag;
    }

}

