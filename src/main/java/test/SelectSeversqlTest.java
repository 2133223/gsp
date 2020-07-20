package test;

import gudusoft.gsqlparser.EDbVendor;
import junit.framework.TestCase;

public class SelectSeversqlTest extends TestCase {

    public void testSeversql1() {
        String sql = "select u_name,u_age,u_score from T_USER";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>T_USER,T_USER\n" +
                                "Expression->>>>u_name,u_age,u_score\n" +
                                "ResultColumn->>>>u_name,u_age,u_score\n" +
                                "Join->>>>T_USER\n" +
                                "ObjectName->>>>u_name,u_age,u_score,T_USER,T_USER"
                ));
    }

    public void testSeversql2() {
        String sql = "select u_name , u_score , u_score+10 , u_score-10 , u_score*2 , u_score/2 \n" +
                "from T_USER";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>T_USER,T_USER\n" +
                                "Expression->>>>u_name,u_score,u_score,10,u_score+10,u_score,10,u_score-10,u_score,2,u_score*2,u_score,2,u_score/2\n" +
                                "ResultColumn->>>>u_name,u_score,u_score+10,u_score-10,u_score*2,u_score/2\n" +
                                "Join->>>>T_USER\n" +
                                "ObjectName->>>>u_name,u_score,u_score,u_score,u_score,u_score,T_USER,T_USER"
                ));
    }

    public void testSeversql3() {
        String sql = "select u_name , u_score\n" +
                "from T_USER \n" +
                "where u_score >= 60 ;";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>T_USER,T_USER\n" +
                                "Expression->>>>u_name,u_score,u_score,60,u_score >= 60\n" +
                                "ResultColumn->>>>u_name,u_score\n" +
                                "WhereClause->>>>where u_score >= 60\n" +
                                "Join->>>>T_USER\n" +
                                "ObjectName->>>>u_name,u_score,T_USER,T_USER,u_score"
                ));
    }

    public void testSeversql4() {
        String sql = "select u_name , u_score\n" +
                "from T_USER \n" +
                "where u_score between 60 and 80 ;";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>T_USER,T_USER\n" +
                                "Expression->>>>u_name,u_score,60,80,u_score,u_score between 60 and 80\n" +
                                "ResultColumn->>>>u_name,u_score\n" +
                                "WhereClause->>>>where u_score between 60 and 80\n" +
                                "Join->>>>T_USER\n" +
                                "ObjectName->>>>u_name,u_score,T_USER,T_USER,u_score"
                ));
    }

    public void testSeversql5() {
        String sql = "select u_name , u_score\n" +
                "from T_USER \n" +
                "where u_score >90 or u_score <60 ;";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>T_USER,T_USER\n" +
                                "Expression->>>>u_name,u_score,u_score,90,u_score >90,u_score,60,u_score <60,u_score >90 or u_score <60\n" +
                                "ResultColumn->>>>u_name,u_score\n" +
                                "WhereClause->>>>where u_score >90 or u_score <60\n" +
                                "Join->>>>T_USER\n" +
                                "ObjectName->>>>u_name,u_score,T_USER,T_USER,u_score,u_score"
                ));
    }

    public void testSeversql6() {
        String sql = "select *\n" +
                "from T_USER \n" +
                "where u_name like '%sss%' ;";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>T_USER,T_USER\n" +
                                "Expression->>>>*,u_name,'%sss%',u_name like '%sss%'\n" +
                                "ResultColumn->>>>*\n" +
                                "WhereClause->>>>where u_name like '%sss%'\n" +
                                "Join->>>>T_USER\n" +
                                "ObjectName->>>>*,T_USER,T_USER,u_name"
                ));
    }

    public void testSeversql7() {
        String sql = "select *\n" +
                "from T_USER \n" +
                "order by u_score desc";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "OrderBy->>>>order by u_score desc\n" +
                                "Table->>>>T_USER,T_USER\n" +
                                "Expression->>>>*,u_score\n" +
                                "ResultColumn->>>>*\n" +
                                "Join->>>>T_USER\n" +
                                "ObjectName->>>>*,T_USER,T_USER,u_score"
                ));
    }

    public void testSeversql8() {
        String sql = "INSERT INTO Table (col1, col2, col3)SELECT col1, col2, col3 \n" +
                "FROM other_table \n" +
                "WHERE sql = 'cool'";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>Table,other_table,other_table\n" +
                                "Expression->>>>col1,col2,col3,sql,'cool',sql = 'cool'\n" +
                                "ResultColumn->>>>col1,col2,col3\n" +
                                "WhereClause->>>>WHERE sql = 'cool'\n" +
                                "Join->>>>other_table\n" +
                                "ObjectName->>>>Table,col1,col2,col3,col1,col2,col3,other_table,other_table,sql"
                ));
    }

    public void testSeversql9() {
        String sql = "SELECT country, name FROM Websites\n" +
                "WHERE country='CN'\n" +
                "UNION ALL\n" +
                "SELECT country, app_name FROM apps\n" +
                "WHERE country='CN'\n" +
                "ORDER BY country";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "OrderBy->>>>ORDER BY country,ORDER BY country\n" +
                                "Table->>>>Websites,Websites,apps,apps\n" +
                                "Expression->>>>country,name,country,'CN',country='CN',country,app_name,country,'CN',country='CN',country,country\n" +
                                "ResultColumn->>>>country,name,country,app_name\n" +
                                "WhereClause->>>>WHERE country='CN',WHERE country='CN'\n" +
                                "Join->>>>Websites,apps\n" +
                                "ObjectName->>>>country,name,Websites,Websites,country,country,app_name,apps,apps,country,country,country"
                ));
    }

    public void testSeversql10() {
        String sql = "SELECT MIN(alexa) AS min_alexa FROM Websites";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>Websites,Websites\n" +
                                "Expression->>>>alexa,MIN(alexa)\n" +
                                "ResultColumn->>>>MIN(alexa)\n" +
                                "Join->>>>Websites\n" +
                                "FunctionCall->>>>MIN(alexa)\n" +
                                "AliasClause->>>>min_alexa\n" +
                                "ObjectName->>>>MIN,alexa,min_alexa,Websites,Websites"
                ));
    }

    public void testSeversql11() {
        String sql = "SELECT DISTINCT Company FROM Orders ";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>Orders,Orders\n" +
                                "Expression->>>>Company\n" +
                                "SelectDistinct->>>>DISTINCT\n" +
                                "ResultColumn->>>>Company\n" +
                                "Join->>>>Orders\n" +
                                "ObjectName->>>>Company,Orders,Orders"
                ));
    }

    public void testSeversql12() {
        String sql = "select sname,sage from student where sage<20;";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>student,student\n" +
                                "Expression->>>>sname,sage,sage,20,sage<20\n" +
                                "ResultColumn->>>>sname,sage\n" +
                                "WhereClause->>>>where sage<20\n" +
                                "Join->>>>student\n" +
                                "ObjectName->>>>sname,sage,student,student,sage"
                ));
    }

    public void testSeversql13() {
        String sql = "select sname,sno from student where sname like '%k%'";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>student,student\n" +
                                "Expression->>>>sname,sno,sname,'%k%',sname like '%k%'\n" +
                                "ResultColumn->>>>sname,sno\n" +
                                "WhereClause->>>>where sname like '%k%'\n" +
                                "Join->>>>student\n" +
                                "ObjectName->>>>sname,sno,student,student,sname"
                ));
    }

    public void testSeversql14() {
        String sql = "select cno,cname from course where credit >=3 and cpno is not null";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>course,course\n" +
                                "Expression->>>>cno,cname,credit,3,credit >=3,cpno,cpno is not null,credit >=3 and cpno is not null\n" +
                                "ResultColumn->>>>cno,cname\n" +
                                "WhereClause->>>>where credit >=3 and cpno is not null\n" +
                                "Join->>>>course\n" +
                                "ObjectName->>>>cno,cname,course,course,credit,cpno"
                ));
    }

    public void testSeversql15() {
        String sql = "select max(grade) as 'ss' from sc where cno=3";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>sc,sc\n" +
                                "Expression->>>>grade,max(grade),cno,3,cno=3\n" +
                                "ResultColumn->>>>max(grade)\n" +
                                "WhereClause->>>>where cno=3\n" +
                                "Join->>>>sc\n" +
                                "FunctionCall->>>>max(grade)\n" +
                                "AliasClause->>>>'ss'\n" +
                                "ObjectName->>>>max,grade,'ss',sc,sc,cno"
                ));
    }

    public void testSeversql16() {
        String sql = "select * where st1.sdept=st2.sdept and st1.sname != st2.sname";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Expression->>>>*,st1.sdept,st2.sdept,st1.sdept=st2.sdept,st1.sname,st2.sname,st1.sname != st2.sname,st1.sdept=st2.sdept and st1.sname != st2.sname\n" +
                                "ResultColumn->>>>*\n" +
                                "WhereClause->>>>where st1.sdept=st2.sdept and st1.sname != st2.sname\n" +
                                "ObjectName->>>>*,st1.sdept,st2.sdept,st1.sname,st2.sname"
                ));
    }

    public void testSeversql17() {
        String sql = "select playerno \n" +
                "from players \n" +
                "where (sex, town) = (\n" +
                "    select sex, town \n" +
                "    from players \n" +
                "    where playerno = 100)";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>players,players,players,players\n" +
                                "Expression->>>>playerno,sex,town,(sex, town),sex,town,playerno,100,playerno = 100,(\n" +
                                "    select sex, town \n" +
                                "    from players \n" +
                                "    where playerno = 100),(sex, town) = (\n" +
                                "    select sex, town \n" +
                                "    from players \n" +
                                "    where playerno = 100)\n" +
                                "ResultColumn->>>>playerno,sex,town\n" +
                                "WhereClause->>>>where playerno = 100,where (sex, town) = (\n" +
                                "    select sex, town \n" +
                                "    from players \n" +
                                "    where playerno = 100)\n" +
                                "Join->>>>players,players\n" +
                                "ObjectName->>>>playerno,players,players,sex,town,sex,town,players,players,playerno"
                ));
    }

    public void testSeversql18() {
        String sql = " select playerno \n" +
                "from players \n" +
                "where year(birth_date) = \n" +
                "    (select year(birth_date) \n" +
                "    from players \n" +
                "    where playerno = 27)";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>players,players,players,players\n" +
                                "Expression->>>>playerno,birth_date,year(birth_date),birth_date,year(birth_date),playerno,27,playerno = 27,(select year(birth_date) \n" +
                                "    from players \n" +
                                "    where playerno = 27),year(birth_date) = \n" +
                                "    (select year(birth_date) \n" +
                                "    from players \n" +
                                "    where playerno = 27)\n" +
                                "ResultColumn->>>>playerno,year(birth_date)\n" +
                                "WhereClause->>>>where playerno = 27,where year(birth_date) = \n" +
                                "    (select year(birth_date) \n" +
                                "    from players \n" +
                                "    where playerno = 27)\n" +
                                "Join->>>>players,players\n" +
                                "FunctionCall->>>>year(birth_date),year(birth_date)\n" +
                                "ObjectName->>>>playerno,players,players,year,birth_date,year,birth_date,players,players,playerno"
                ));
    }

    public void testSeversql19() {
        String sql = "SELECT * FROM EMP WHERE SAL >(SELECT SAL FROM EMP WHERE ENAME = 'SMITH')";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>EMP,EMP,EMP,EMP\n" +
                                "Expression->>>>*,SAL,SAL,ENAME,'SMITH',ENAME = 'SMITH',(SELECT SAL FROM EMP WHERE ENAME = 'SMITH'),SAL >(SELECT SAL FROM EMP WHERE ENAME = 'SMITH')\n" +
                                "ResultColumn->>>>*,SAL\n" +
                                "WhereClause->>>>WHERE ENAME = 'SMITH',WHERE SAL >(SELECT SAL FROM EMP WHERE ENAME = 'SMITH')\n" +
                                "Join->>>>EMP,EMP\n" +
                                "ObjectName->>>>*,EMP,EMP,SAL,SAL,EMP,EMP,ENAME"
                ));
    }

    public void testSeversql20() {
        String sql = "select playerno from players where year(birth_date) = 1964 and playerno <> 27";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmssql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>players,players\n" +
                                "Expression->>>>playerno,birth_date,year(birth_date),1964,year(birth_date) = 1964,playerno,27,playerno <> 27,year(birth_date) = 1964 and playerno <> 27\n" +
                                "ResultColumn->>>>playerno\n" +
                                "WhereClause->>>>where year(birth_date) = 1964 and playerno <> 27\n" +
                                "Join->>>>players\n" +
                                "FunctionCall->>>>year(birth_date)\n" +
                                "ObjectName->>>>playerno,players,players,year,birth_date,playerno"
                ));
    }


}
