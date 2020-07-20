package test;

import gudusoft.gsqlparser.EDbVendor;
import junit.framework.TestCase;

public class SelectMysqlTest extends TestCase {

    public void testMysql1() {
        String sql = "SELECT boss_name, salary FROM Bosses\n" +
                "UNION\n" +
                "SELECT employee_name, salary FROM Employees;\n";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>Bosses,Bosses,Employees,Employees\n" +
                                "Expression->>>>boss_name,salary,employee_name,salary\n" +
                                "ResultColumn->>>>boss_name,salary,employee_name,salary\n" +
                                "Join->>>>Bosses,Employees\n" +
                                "ObjectName->>>>boss_name,salary,Bosses,Bosses,employee_name,salary,Employees,Employees"
                ));
    }

    public void testMysql2() {
        String sql = "SELECT column_name(s) FROM table_name1\n" +
                "UNION ALL\n" +
                "SELECT column_name(s) FROM table_name2";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>table_name1,table_name1,table_name2,table_name2\n" +
                                "Expression->>>>s,column_name(s),s,column_name(s)\n" +
                                "ResultColumn->>>>column_name(s),column_name(s)\n" +
                                "Join->>>>table_name1,table_name2\n" +
                                "FunctionCall->>>>column_name(s),column_name(s)\n" +
                                "ObjectName->>>>column_name,s,table_name1,table_name1,column_name,s,table_name2,table_name2"
                ));
    }

    public void testMysql3() {
        String sql = "SELECT E_Name FROM Employees_China\n" +
                "UNION ALL\n" +
                "SELECT E_Name FROM Employees_USA";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>Employees_China,Employees_China,Employees_USA,Employees_USA\n" +
                                "Expression->>>>E_Name,E_Name\n" +
                                "ResultColumn->>>>E_Name,E_Name\n" +
                                "Join->>>>Employees_China,Employees_USA\n" +
                                "ObjectName->>>>E_Name,Employees_China,Employees_China,E_Name,Employees_USA,Employees_USA"
                ));
    }

    public void testMysql4() {
        String sql = "SELECT Company, OrderNumber FROM Orders ORDER BY Company";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "OrderBy->>>>ORDER BY Company\n" +
                                "Table->>>>Orders,Orders\n" +
                                "Expression->>>>Company,OrderNumber,Company\n" +
                                "ResultColumn->>>>Company,OrderNumber\n" +
                                "Join->>>>Orders\n" +
                                "ObjectName->>>>Company,OrderNumber,Orders,Orders,Company"
                ));
    }

    public void testMysql5() {
        String sql = "SELECT site_id, SUM(access_log.count) AS nums\n" +
                "FROM access_log GROUP BY site_id";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>access_log,access_log\n" +
                                "GroupBy->>>>GROUP BY site_id\n" +
                                "Expression->>>>site_id,access_log.count,SUM(access_log.count),site_id\n" +
                                "ResultColumn->>>>site_id,SUM(access_log.count)\n" +
                                "Join->>>>access_log\n" +
                                "FunctionCall->>>>SUM(access_log.count)\n" +
                                "AliasClause->>>>nums\n" +
                                "ObjectName->>>>site_id,SUM,access_log.count,nums,access_log,access_log,site_id"
                ));
    }

    public void testMysql6() {
        String sql = "SELECT Websites.name, Websites.url, SUM(access_log.count) AS nums FROM (access_log\n" +
                "INNER JOIN Websites\n" +
                "ON access_log.site_id=Websites.id)\n" +
                "GROUP BY Websites.name\n" +
                "HAVING SUM(access_log.count) > 200";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "GroupBy->>>>GROUP BY Websites.name\n" +
                                "HAVING SUM(access_log.count) > 200\n" +
                                "Expression->>>>Websites.name,Websites.url,access_log.count,SUM(access_log.count),Websites.name,access_log.count,SUM(access_log.count),200,SUM(access_log.count) > 200\n" +
                                "ResultColumn->>>>Websites.name,Websites.url,SUM(access_log.count)\n" +
                                "Join->>>>(access_log\n" +
                                "INNER JOIN Websites\n" +
                                "ON access_log.site_id=Websites.id)\n" +
                                "FunctionCall->>>>SUM(access_log.count),SUM(access_log.count)\n" +
                                "AliasClause->>>>nums\n" +
                                "ObjectName->>>>Websites.name,Websites.url,SUM,access_log.count,nums,Websites.name,SUM,access_log.count"
                ));
    }

    public void testMysql7() {
        String sql = "\n" +
                "SELECT Websites.name, Websites.url \n" +
                "FROM Websites \n" +
                "WHERE EXISTS (SELECT count FROM access_log WHERE Websites.id = access_log.site_id AND count > 200)";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>Websites,Websites,access_log,access_log\n" +
                                "Expression->>>>Websites.name,Websites.url,count,Websites.id,access_log.site_id,Websites.id = access_log.site_id,count,200,count > 200,Websites.id = access_log.site_id AND count > 200,EXISTS (SELECT count FROM access_log WHERE Websites.id = access_log.site_id AND count > 200)\n" +
                                "ResultColumn->>>>Websites.name,Websites.url,count\n" +
                                "WhereClause->>>>WHERE Websites.id = access_log.site_id AND count > 200,WHERE EXISTS (SELECT count FROM access_log WHERE Websites.id = access_log.site_id AND count > 200)\n" +
                                "Join->>>>Websites,access_log\n" +
                                "ObjectName->>>>Websites.name,Websites.url,Websites,Websites,count,access_log,access_log,Websites.id,access_log.site_id,count"
                ));
    }

    public void testMysql8() {
        String sql = "SELECT country FROM Websites\n" +
                "UNION\n" +
                "SELECT country FROM apps\n" +
                "ORDER BY country";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "OrderBy->>>>ORDER BY country,ORDER BY country\n" +
                                "Table->>>>Websites,Websites,apps,apps\n" +
                                "Expression->>>>country,country,country,country\n" +
                                "ResultColumn->>>>country,country\n" +
                                "Join->>>>Websites,apps\n" +
                                "ObjectName->>>>country,Websites,Websites,country,apps,apps,country,country"
                ));
    }

    public void testMysql9() {
        String sql = "SELECT country, name FROM Websites\n" +
                "WHERE country='CN'\n" +
                "UNION ALL\n" +
                "SELECT country, app_name FROM apps\n" +
                "WHERE country='CN'\n" +
                "ORDER BY country";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
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

    public void testMysql10() {
        String sql = "SELECT MIN(alexa) AS min_alexa FROM Websites";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
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

    public void testMysql11() {
        String sql = "SELECT Websites.name, access_log.count, access_log.date\n" +
                "FROM Websites\n" +
                "INNER JOIN access_log\n" +
                "ON Websites.id=access_log.site_id\n" +
                "ORDER BY access_log.count";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "OrderBy->>>>ORDER BY access_log.count\n" +
                                "Table->>>>Websites,access_log\n" +
                                "Expression->>>>Websites.name,access_log.count,access_log.date,Websites.id,access_log.site_id,Websites.id=access_log.site_id,access_log.count\n" +
                                "ResultColumn->>>>Websites.name,access_log.count,access_log.date\n" +
                                "Join->>>>Websites\n" +
                                "INNER JOIN access_log\n" +
                                "ON Websites.id=access_log.site_id\n" +
                                "ObjectName->>>>Websites.name,access_log.count,access_log.date,Websites,access_log,Websites.id,access_log.site_id,access_log.count"
                ));
    }

    public void testMysql12() {
        String sql = "SELECT column_name(s) FROM table_name1\n" +
                "UNION ALL\n" +
                "SELECT column_name(s) FROM table_name2";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>table_name1,table_name1,table_name2,table_name2\n" +
                                "Expression->>>>s,column_name(s),s,column_name(s)\n" +
                                "ResultColumn->>>>column_name(s),column_name(s)\n" +
                                "Join->>>>table_name1,table_name2\n" +
                                "FunctionCall->>>>column_name(s),column_name(s)\n" +
                                "ObjectName->>>>column_name,s,table_name1,table_name1,column_name,s,table_name2,table_name2"
                ));
    }

    public void testMysql13() {
        String sql = "SELECT SUM(count) AS nums FROM access_log";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>access_log,access_log\n" +
                                "Expression->>>>count,SUM(count)\n" +
                                "ResultColumn->>>>SUM(count)\n" +
                                "Join->>>>access_log\n" +
                                "FunctionCall->>>>SUM(count)\n" +
                                "AliasClause->>>>nums\n" +
                                "ObjectName->>>>SUM,count,nums,access_log,access_log"
                ));
    }

    public void testMysql14() {
        String sql = "SELECT Websites.name, access_log.count, access_log.date\n" +
                "FROM access_log\n" +
                "RIGHT JOIN Websites\n" +
                "ON access_log.site_id=Websites.id\n" +
                "ORDER BY access_log.count DESC";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "OrderBy->>>>ORDER BY access_log.count DESC\n" +
                                "Table->>>>access_log,Websites\n" +
                                "Expression->>>>Websites.name,access_log.count,access_log.date,access_log.site_id,Websites.id,access_log.site_id=Websites.id,access_log.count\n" +
                                "ResultColumn->>>>Websites.name,access_log.count,access_log.date\n" +
                                "Join->>>>access_log\n" +
                                "RIGHT JOIN Websites\n" +
                                "ON access_log.site_id=Websites.id\n" +
                                "ObjectName->>>>Websites.name,access_log.count,access_log.date,access_log,Websites,access_log.site_id,Websites.id,access_log.count"
                ));
    }

    public void testMysql15() {
        String sql = "SELECT * FROM Websites\n" +
                "WHERE name LIKE 'G%'";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>Websites,Websites\n" +
                                "Expression->>>>*,name,'G%',name LIKE 'G%'\n" +
                                "ResultColumn->>>>*\n" +
                                "WhereClause->>>>WHERE name LIKE 'G%'\n" +
                                "Join->>>>Websites\n" +
                                "ObjectName->>>>*,Websites,Websites,name"
                ));
    }

    public void testMysql16() {
        String sql = "SELECT * FROM Websites\n" +
                "WHERE country='CN'\n" +
                "AND alexa > 50";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>Websites,Websites\n" +
                                "Expression->>>>*,country,'CN',country='CN',alexa,50,alexa > 50,country='CN'\n" +
                                "AND alexa > 50\n" +
                                "ResultColumn->>>>*\n" +
                                "WhereClause->>>>WHERE country='CN'\n" +
                                "AND alexa > 50\n" +
                                "Join->>>>Websites\n" +
                                "ObjectName->>>>*,Websites,Websites,country,alexa"
                ));
    }

    public void testMysql17() {
        String sql = "SELECT Customer,SUM(OrderPrice) FROM Orders\n" +
                "GROUP BY Customer";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>Orders,Orders\n" +
                                "GroupBy->>>>GROUP BY Customer\n" +
                                "Expression->>>>Customer,OrderPrice,SUM(OrderPrice),Customer\n" +
                                "ResultColumn->>>>Customer,SUM(OrderPrice)\n" +
                                "Join->>>>Orders\n" +
                                "FunctionCall->>>>SUM(OrderPrice)\n" +
                                "ObjectName->>>>Customer,SUM,OrderPrice,Orders,Orders,Customer"
                ));
    }

    public void testMysql18() {
        String sql = "SELECT Customer,OrderDate,SUM(OrderPrice) FROM Orders\n" +
                "GROUP BY Customer,OrderDate";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>Orders,Orders\n" +
                                "GroupBy->>>>GROUP BY Customer,OrderDate\n" +
                                "Expression->>>>Customer,OrderDate,OrderPrice,SUM(OrderPrice),Customer,OrderDate\n" +
                                "ResultColumn->>>>Customer,OrderDate,SUM(OrderPrice)\n" +
                                "Join->>>>Orders\n" +
                                "FunctionCall->>>>SUM(OrderPrice)\n" +
                                "ObjectName->>>>Customer,OrderDate,SUM,OrderPrice,Orders,Orders,Customer,OrderDate"
                ));
    }

    public void testMysql19() {
        String sql = "SELECT * FROM EMP WHERE SAL >(SELECT SAL FROM EMP WHERE ENAME = 'SMITH')";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
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

    public void testMysql20() {
        String sql = "SELECT JOB\n" +
                "FROM EMP\n" +
                "GROUP BY JOB\n" +
                "HAVING MIN(SAL)>1500";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvmysql);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>EMP,EMP\n" +
                                "GroupBy->>>>GROUP BY JOB\n" +
                                "HAVING MIN(SAL)>1500\n" +
                                "Expression->>>>JOB,JOB,SAL,MIN(SAL),1500,MIN(SAL)>1500\n" +
                                "ResultColumn->>>>JOB\n" +
                                "Join->>>>EMP\n" +
                                "FunctionCall->>>>MIN(SAL)\n" +
                                "ObjectName->>>>JOB,EMP,EMP,JOB,MIN,SAL"
                ));
    }


}
