package test;

import gudusoft.gsqlparser.EDbVendor;
import junit.framework.TestCase;

public class SelectPostgresSQLTest extends TestCase {

    public void testPostgresSQL1() {
        String sql = "SELECT *  \n" +
                "FROM EMPLOYEES  \n" +
                "WHERE (NAME = 'Minsu' AND ADDRESS = 'Delhi')  \n" +
                "OR (ID>= 8)";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>EMPLOYEES,EMPLOYEES\n" +
                                "Expression->>>>*,NAME,'Minsu',NAME = 'Minsu',ADDRESS,'Delhi',ADDRESS = 'Delhi',NAME = 'Minsu' AND ADDRESS = 'Delhi',(NAME = 'Minsu' AND ADDRESS = 'Delhi'),ID,8,ID>= 8,(ID>= 8),(NAME = 'Minsu' AND ADDRESS = 'Delhi')  \n" +
                                "OR (ID>= 8)\n" +
                                "ResultColumn->>>>*\n" +
                                "WhereClause->>>>WHERE (NAME = 'Minsu' AND ADDRESS = 'Delhi')  \n" +
                                "OR (ID>= 8)\n" +
                                "Join->>>>EMPLOYEES\n" +
                                "ObjectName->>>>*,EMPLOYEES,EMPLOYEES,NAME,ADDRESS,ID"
                ));
    }

    public void testPostgresSQL2() {
        String sql = "SELECT *  \n" +
                "FROM EMPLOYEES  \n" +
                "WHERE (NAME = 'minsu' AND ADDRESS = 'Paris')  \n" +
                "OR (ID>= 2)";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>EMPLOYEES,EMPLOYEES\n" +
                                "Expression->>>>*,NAME,'minsu',NAME = 'minsu',ADDRESS,'Paris',ADDRESS = 'Paris',NAME = 'minsu' AND ADDRESS = 'Paris',(NAME = 'minsu' AND ADDRESS = 'Paris'),ID,2,ID>= 2,(ID>= 2),(NAME = 'minsu' AND ADDRESS = 'Paris')  \n" +
                                "OR (ID>= 2)\n" +
                                "ResultColumn->>>>*\n" +
                                "WhereClause->>>>WHERE (NAME = 'minsu' AND ADDRESS = 'Paris')  \n" +
                                "OR (ID>= 2)\n" +
                                "Join->>>>EMPLOYEES\n" +
                                "ObjectName->>>>*,EMPLOYEES,EMPLOYEES,NAME,ADDRESS,ID"
                ));
    }

    public void testPostgresSQL3() {
        String sql = "SELECT *   \n" +
                "FROM EMPLOYEES   \n" +
                "WHERE NAME LIKE '%sha'";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>EMPLOYEES,EMPLOYEES\n" +
                                "Expression->>>>*,NAME,'%sha',NAME LIKE '%sha'\n" +
                                "ResultColumn->>>>*\n" +
                                "WhereClause->>>>WHERE NAME LIKE '%sha'\n" +
                                "Join->>>>EMPLOYEES\n" +
                                "ObjectName->>>>*,EMPLOYEES,EMPLOYEES,NAME"
                ));
    }

    public void testPostgresSQL4() {
        String sql = " select * \n" +
                "from employees \n" +
                "where name like 'm%'";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>employees,employees\n" +
                                "Expression->>>>*,name,'m%',name like 'm%'\n" +
                                "ResultColumn->>>>*\n" +
                                "WhereClause->>>>where name like 'm%'\n" +
                                "Join->>>>employees\n" +
                                "ObjectName->>>>*,employees,employees,name"
                ));
    }

    public void testPostgresSQL5() {
        String sql = "select f_price from fruit where f_name='banana'";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>fruit,fruit\n" +
                                "Expression->>>>f_price,f_name,'banana',f_name='banana'\n" +
                                "ResultColumn->>>>f_price\n" +
                                "WhereClause->>>>where f_name='banana'\n" +
                                "Join->>>>fruit\n" +
                                "ObjectName->>>>f_price,fruit,fruit,f_name"
                ));
    }

    public void testPostgresSQL6() {
        String sql = "select s_id,f_name,f_price from fruit where s_id in (102,103)";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>fruit,fruit\n" +
                                "Expression->>>>s_id,f_name,f_price,s_id,102,103,(102,103),s_id in (102,103)\n" +
                                "ResultColumn->>>>s_id,f_name,f_price\n" +
                                "WhereClause->>>>where s_id in (102,103)\n" +
                                "Join->>>>fruit\n" +
                                "ObjectName->>>>s_id,f_name,f_price,fruit,fruit,s_id"
                ));
    }

    public void testPostgresSQL7() {
        String sql = "select s_id,f_name,f_price from fruit where s_id not in (102,103)";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>fruit,fruit\n" +
                                "Expression->>>>s_id,f_name,f_price,s_id,102,103,(102,103),s_id not in (102,103)\n" +
                                "ResultColumn->>>>s_id,f_name,f_price\n" +
                                "WhereClause->>>>where s_id not in (102,103)\n" +
                                "Join->>>>fruit\n" +
                                "ObjectName->>>>s_id,f_name,f_price,fruit,fruit,s_id"
                ));
    }

    public void testPostgresSQL8() {
        String sql = "select s_id,f_name,f_price from fruit where f_price between 5.2 and 8.2";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>fruit,fruit\n" +
                                "Expression->>>>s_id,f_name,f_price,5.2,8.2,f_price,f_price between 5.2 and 8.2\n" +
                                "ResultColumn->>>>s_id,f_name,f_price\n" +
                                "WhereClause->>>>where f_price between 5.2 and 8.2\n" +
                                "Join->>>>fruit\n" +
                                "ObjectName->>>>s_id,f_name,f_price,fruit,fruit,f_price"
                ));
    }

    public void testPostgresSQL9() {
        String sql = "select s_id,f_name,f_price from fruit where f_price not between 5.2 and 8.2";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>fruit,fruit\n" +
                                "Expression->>>>s_id,f_name,f_price,5.2,8.2,f_price,f_price not between 5.2 and 8.2\n" +
                                "ResultColumn->>>>s_id,f_name,f_price\n" +
                                "WhereClause->>>>where f_price not between 5.2 and 8.2\n" +
                                "Join->>>>fruit\n" +
                                "ObjectName->>>>s_id,f_name,f_price,fruit,fruit,f_price"
                ));
    }

    public void testPostgresSQL10() {
        String sql = "select s_id,f_name from fruit where f_name LIKE 'a%'";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>fruit,fruit\n" +
                                "Expression->>>>s_id,f_name,f_name,'a%',f_name LIKE 'a%'\n" +
                                "ResultColumn->>>>s_id,f_name\n" +
                                "WhereClause->>>>where f_name LIKE 'a%'\n" +
                                "Join->>>>fruit\n" +
                                "ObjectName->>>>s_id,f_name,fruit,fruit,f_name"
                ));
    }

    public void testPostgresSQL11() {
        String sql = "select ename,job,(sal+nvl(comm,0))*12 from emp";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp\n" +
                                "Expression->>>>ename,job,sal,comm,0,nvl(comm,0),sal+nvl(comm,0),(sal+nvl(comm,0)),12,(sal+nvl(comm,0))*12\n" +
                                "ResultColumn->>>>ename,job,(sal+nvl(comm,0))*12\n" +
                                "Join->>>>emp\n" +
                                "FunctionCall->>>>nvl(comm,0)\n" +
                                "ObjectName->>>>ename,job,sal,nvl,comm,emp,emp"
                ));
    }

    public void testPostgresSQL12() {
        String sql = "select s_id,f_name from fruit where f_name LIKE '%b%'";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>fruit,fruit\n" +
                                "Expression->>>>s_id,f_name,f_name,'%b%',f_name LIKE '%b%'\n" +
                                "ResultColumn->>>>s_id,f_name\n" +
                                "WhereClause->>>>where f_name LIKE '%b%'\n" +
                                "Join->>>>fruit\n" +
                                "ObjectName->>>>s_id,f_name,fruit,fruit,f_name"
                ));
    }

    public void testPostgresSQL13() {
        String sql = "select id,name ,email from customers where email is not null";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>customers,customers\n" +
                                "Expression->>>>id,name,email,email,email is not null\n" +
                                "ResultColumn->>>>id,name,email\n" +
                                "WhereClause->>>>where email is not null\n" +
                                "Join->>>>customers\n" +
                                "ObjectName->>>>id,name,email,customers,customers,email"
                ));
    }

    public void testPostgresSQL14() {
        String sql = "select  s_id , count(*) as total  from fruit group by s_id ";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>fruit,fruit\n" +
                                "GroupBy->>>>group by s_id\n" +
                                "Expression->>>>s_id,*,count(*),s_id\n" +
                                "ResultColumn->>>>s_id,count(*)\n" +
                                "Join->>>>fruit\n" +
                                "FunctionCall->>>>count(*)\n" +
                                "AliasClause->>>>total\n" +
                                "ObjectName->>>>s_id,count,*,total,fruit,fruit,s_id"
                ));
    }

    public void testPostgresSQL15() {
        String sql = "select  s_id , count(f_name) from fruit group by s_id having count(f_name)>90";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>fruit,fruit\n" +
                                "GroupBy->>>>group by s_id having count(f_name)>90\n" +
                                "Expression->>>>s_id,f_name,count(f_name),s_id,f_name,count(f_name),90,count(f_name)>90\n" +
                                "ResultColumn->>>>s_id,count(f_name)\n" +
                                "Join->>>>fruit\n" +
                                "FunctionCall->>>>count(f_name),count(f_name)\n" +
                                "ObjectName->>>>s_id,count,f_name,fruit,fruit,s_id,count,f_name"
                ));
    }

    public void testPostgresSQL16() {
        String sql = "select num1 from tb11 where num1 > all(select num2 from tb12)";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>tb11,tb11,tb12,tb12\n" +
                                "Expression->>>>num1,num1,num2,(select num2 from tb12),num1 > all(select num2 from tb12)\n" +
                                "ResultColumn->>>>num1,num2\n" +
                                "WhereClause->>>>where num1 > all(select num2 from tb12)\n" +
                                "Join->>>>tb11,tb12\n" +
                                "ObjectName->>>>num1,tb11,tb11,num1,num2,tb12,tb12"
                ));
    }

    public void testPostgresSQL17() {
        String sql = "select * from fruit where exists(select f_name from customers where s_id=102)";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>fruit,fruit,customers,customers\n" +
                                "Expression->>>>*,f_name,s_id,102,s_id=102,exists(select f_name from customers where s_id=102)\n" +
                                "ResultColumn->>>>*,f_name\n" +
                                "WhereClause->>>>where s_id=102,where exists(select f_name from customers where s_id=102)\n" +
                                "Join->>>>fruit,customers\n" +
                                "ObjectName->>>>*,fruit,fruit,f_name,customers,customers,s_id"
                ));
    }

    public void testPostgresSQL18() {
        String sql = "select * from pg_tables where schemaname = ‘public’";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>pg_tables,pg_tables\n" +
                                "Expression->>>>*,schemaname,‘public’,schemaname = ‘public’\n" +
                                "ResultColumn->>>>*\n" +
                                "WhereClause->>>>where schemaname = ‘public’\n" +
                                "Join->>>>pg_tables\n" +
                                "ObjectName->>>>*,pg_tables,pg_tables,schemaname,‘public’"
                ));
    }

    public void testPostgresSQL19() {
        String sql = "SELECT\n" +
                " column_1,\n" +
                " column_2\n" +
                "FROM\n" +
                "   tbl_name\n" +
                "ORDER BY\n" +
                "   column_1 ASC,\n" +
                "   column_2 DESC;";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "OrderBy->>>>ORDER BY\n" +
                                "   column_1 ASC,\n" +
                                "   column_2 DESC\n" +
                                "Table->>>>tbl_name,tbl_name\n" +
                                "Expression->>>>column_1,column_2,column_1,column_2\n" +
                                "ResultColumn->>>>column_1,column_2\n" +
                                "Join->>>>tbl_name\n" +
                                "ObjectName->>>>column_1,column_2,tbl_name,tbl_name,column_1,column_2"
                ));
    }

    public void testPostgresSQL20() {
        String sql = "SELECT\n" +
                " DISTINCT ON\n" +
                " (bcolor) bcolor,\n" +
                " fcolor\n" +
                "FROM\n" +
                " t1\n" +
                "ORDER BY\n" +
                " bcolor,\n" +
                " fcolor;";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvpostgresql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "OrderBy->>>>ORDER BY\n" +
                                " bcolor,\n" +
                                " fcolor\n" +
                                "Table->>>>t1,t1\n" +
                                "Expression->>>>bcolor,fcolor,bcolor,fcolor\n" +
                                "SelectDistinct->>>>DISTINCT ON\n" +
                                " (bcolor)\n" +
                                "ResultColumn->>>>bcolor,fcolor\n" +
                                "Join->>>>t1\n" +
                                "ObjectName->>>>bcolor,fcolor,t1,t1,bcolor,fcolor"
                ));
    }


}
