import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TSourceTokenList;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TWhereClause;
import junit.framework.TestCase;

public class AAATest extends TestCase {

    public void testOracleSql1() {
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvoracle);
        sqlparser.sqltext = "SELECT e.emp_id,e.fname,e.lname,j.job_desc\n" +
                "FROM scott.employee AS e,jobs AS j";
        int ret = sqlparser.parse();
        if (ret == 0) {
            sqlparser.getSourcetokenlist().forEachRemaining(it->{
                TSourceTokenList sourcetokenlist = sqlparser.getSourcetokenlist();
                if(it.astext.equals("e")){
                    it.removeMeFromTokenList();
                }
                if(it.astext.equals(".")){
                    it.removeMeFromTokenList();
                }
                if(it.astext.equals("emp_id")){
                    it.removeMeFromTokenList();
                }
            });
            System.out.println("");
        }
    }

    public void test2() {
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvoracle);
        TWhereClause whereClause = new TWhereClause();
        whereClause.setGsqlparser(sqlparser);
        whereClause.setString("where a+b>c");
        System.out.println("");
    }
}
