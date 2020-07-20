package test;

import gudusoft.gsqlparser.EDbVendor;
import junit.framework.TestCase;

public class SelectOracleTest extends TestCase {

    public void testOracle1() {
        String sql = "\n" +
                "select d.* from dept d,\n" +
                "(select e.deptno, count(1) n from emp e group by e.deptno) t\n" +
                "where d.deptno=t.deptno and t.n>=3";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>dept,dept,emp,emp,(select e.deptno, count(1) n from emp e group by e.deptno),emp,emp,(select e.deptno, count(1) n from emp e group by e.deptno)\n" +
                                "GroupBy->>>>group by e.deptno,group by e.deptno\n" +
                                "Expression->>>>d.*,e.deptno,1,count(1),e.deptno,e.deptno,1,count(1),e.deptno,d.deptno,t.deptno,d.deptno=t.deptno,t.n,3,t.n>=3,d.deptno=t.deptno and t.n>=3,d.deptno,t.deptno,d.deptno=t.deptno,t.n,3,t.n>=3,d.deptno=t.deptno and t.n>=3\n" +
                                "ResultColumn->>>>d.*,e.deptno,count(1),e.deptno,count(1)\n" +
                                "WhereClause->>>>where d.deptno=t.deptno and t.n>=3,where d.deptno=t.deptno and t.n>=3\n" +
                                "Join->>>>dept d,emp e,emp e,(select e.deptno, count(1) n from emp e group by e.deptno) t\n" +
                                "FunctionCall->>>>count(1),count(1)\n" +
                                "AliasClause->>>>d,d,n,e,e,t,n,e,e,t\n" +
                                "ObjectName->>>>d.*,dept,d,dept,d,e.deptno,count,n,emp,e,emp,e,e.deptno,t,e.deptno,count,n,emp,e,emp,e,e.deptno,t,d.deptno,t.deptno,t.n,d.deptno,t.deptno,t.n"
                ));
    }

    public void testOracle2() {
        String sql = "select e.*, d.dname\n" +
                "from emp e, dept d\n" +
                "where e.job=(select job from emp where ename='SCOTT') \n" +
                "      and e.deptno=d.deptno";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp,dept,dept,emp,emp,emp,emp\n" +
                                "Expression->>>>e.*,d.dname,e.job,job,ename,'SCOTT',ename='SCOTT',(select job from emp where ename='SCOTT'),e.job=(select job from emp where ename='SCOTT'),e.deptno,d.deptno,e.deptno=d.deptno,e.job=(select job from emp where ename='SCOTT') \n" +
                                "      and e.deptno=d.deptno,e.job,job,ename,'SCOTT',ename='SCOTT',(select job from emp where ename='SCOTT'),e.job=(select job from emp where ename='SCOTT'),e.deptno,d.deptno,e.deptno=d.deptno,e.job=(select job from emp where ename='SCOTT') \n" +
                                "      and e.deptno=d.deptno\n" +
                                "ResultColumn->>>>e.*,d.dname,job,job\n" +
                                "WhereClause->>>>where ename='SCOTT',where e.job=(select job from emp where ename='SCOTT') \n" +
                                "      and e.deptno=d.deptno,where ename='SCOTT',where e.job=(select job from emp where ename='SCOTT') \n" +
                                "      and e.deptno=d.deptno\n" +
                                "Join->>>>emp e,dept d,emp,emp\n" +
                                "AliasClause->>>>e,e,d,d\n" +
                                "ObjectName->>>>e.*,d.dname,emp,e,emp,e,dept,d,dept,d,e.job,job,emp,emp,ename,e.deptno,d.deptno,e.job,job,emp,emp,ename,e.deptno,d.deptno"
                ));
    }

    public void testOracle3() {
        String sql = "SELECT DISTINCT oeb04,oeb06 FROM oeb_file";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>oeb_file,oeb_file\n" +
                                "Expression->>>>oeb04,oeb06\n" +
                                "SelectDistinct->>>>DISTINCT\n" +
                                "ResultColumn->>>>oeb04,oeb06\n" +
                                "Join->>>>oeb_file\n" +
                                "ObjectName->>>>oeb04,oeb06,oeb_file,oeb_file"
                ));
    }

    public void testOracle4() {
        String sql = " select e.deptno from emp e group by e.deptno";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp\n" +
                                "GroupBy->>>>group by e.deptno\n" +
                                "Expression->>>>e.deptno,e.deptno\n" +
                                "ResultColumn->>>>e.deptno\n" +
                                "Join->>>>emp e\n" +
                                "AliasClause->>>>e,e\n" +
                                "ObjectName->>>>e.deptno,emp,e,emp,e,e.deptno"
                ));
    }

    public void testOracle5() {
        String sql = "select max(sal)  from emp";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp\n" +
                                "Expression->>>>sal,max(sal)\n" +
                                "ResultColumn->>>>max(sal)\n" +
                                "Join->>>>emp\n" +
                                "FunctionCall->>>>max(sal)\n" +
                                "ObjectName->>>>max,sal,emp,emp"
                ));
    }

    public void testOracle6() {
        String sql = "select min(sal) from emp";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp\n" +
                                "Expression->>>>sal,min(sal)\n" +
                                "ResultColumn->>>>min(sal)\n" +
                                "Join->>>>emp\n" +
                                "FunctionCall->>>>min(sal)\n" +
                                "ObjectName->>>>min,sal,emp,emp"
                ));
    }

    public void testOracle7() {
        String sql = " select avg(sal) from emp where deptno = (select deptno from emp where sal = ( select min(sal) from emp))";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp,emp,emp,emp,emp\n" +
                                "Expression->>>>sal,avg(sal),deptno,deptno,sal,sal,min(sal),( select min(sal) from emp),sal = ( select min(sal) from emp),(select deptno from emp where sal = ( select min(sal) from emp)),deptno = (select deptno from emp where sal = ( select min(sal) from emp))\n" +
                                "ResultColumn->>>>avg(sal),deptno,min(sal)\n" +
                                "WhereClause->>>>where sal = ( select min(sal) from emp),where deptno = (select deptno from emp where sal = ( select min(sal) from emp))\n" +
                                "Join->>>>emp,emp,emp\n" +
                                "FunctionCall->>>>avg(sal),min(sal)\n" +
                                "ObjectName->>>>avg,sal,emp,emp,deptno,deptno,emp,emp,sal,min,sal,emp,emp"
                ));
    }

    public void testOracle8() {
        String sql = "SELECT country FROM Websites\n" +
                "UNION\n" +
                "SELECT country FROM apps\n" +
                "ORDER BY country";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
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

    public void testOracle9() {
        String sql = "select empno,1*2 as count,'cmj' as name from emp";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp\n" +
                                "Expression->>>>empno,1,2,1*2,'cmj'\n" +
                                "ResultColumn->>>>empno,1*2,'cmj'\n" +
                                "Join->>>>emp\n" +
                                "AliasClause->>>>count,name\n" +
                                "ObjectName->>>>empno,count,name,emp,emp"
                ));
    }

    public void testOracle10() {
        String sql = "select ename,sal*12+nvl(comm,0) from emp";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp\n" +
                                "Expression->>>>ename,sal,12,sal*12,comm,0,nvl(comm,0),sal*12+nvl(comm,0)\n" +
                                "ResultColumn->>>>ename,sal*12+nvl(comm,0)\n" +
                                "Join->>>>emp\n" +
                                "FunctionCall->>>>nvl(comm,0)\n" +
                                "ObjectName->>>>ename,sal,nvl,comm,emp,emp"
                ));
    }

    public void testOracle11() {
        String sql = "select ename,job,(sal+nvl(comm,0))*12 from emp";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
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

    public void testOracle12() {
        String sql = "select ename from emp where sal>1500\n" +
                "union all\n" +
                "select ename from emp where comm is not null";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp,emp,emp\n" +
                                "Expression->>>>ename,sal,1500,sal>1500,ename,comm,comm is not null\n" +
                                "ResultColumn->>>>ename,ename\n" +
                                "WhereClause->>>>where sal>1500,where comm is not null\n" +
                                "Join->>>>emp,emp\n" +
                                "ObjectName->>>>ename,emp,emp,sal,ename,emp,emp,comm"
                ));
    }

    public void testOracle13() {
        String sql = "select ename,sal,comm from emp where sal>1500\n" +
                "intersect\n" +
                "select ename,sal,comm from emp where comm is not null";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp,emp,emp\n" +
                                "Expression->>>>ename,sal,comm,sal,1500,sal>1500,ename,sal,comm,comm,comm is not null\n" +
                                "ResultColumn->>>>ename,sal,comm,ename,sal,comm\n" +
                                "WhereClause->>>>where sal>1500,where comm is not null\n" +
                                "Join->>>>emp,emp\n" +
                                "ObjectName->>>>ename,sal,comm,emp,emp,sal,ename,sal,comm,emp,emp,comm"
                ));
    }

    public void testOracle14() {
        String sql = "select deptno from dept where dname in('SALES','ACCOUNTING')";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>dept,dept\n" +
                                "Expression->>>>deptno,dname,'SALES','ACCOUNTING',('SALES','ACCOUNTING'),dname in('SALES','ACCOUNTING')\n" +
                                "ResultColumn->>>>deptno\n" +
                                "WhereClause->>>>where dname in('SALES','ACCOUNTING')\n" +
                                "Join->>>>dept\n" +
                                "ObjectName->>>>deptno,dept,dept,dname"
                ));
    }

    public void testOracle15() {
        String sql = "select empno, ename, sal,comm\n" +
                "  from emp e1\n" +
                " where exists (select empno, ename, sal, comm\n" +
                "from emp e2\n" +
                " where comm is not null\n" +
                "and e1.empno = e2.empno)";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp,emp,emp\n" +
                                "Expression->>>>empno,ename,sal,comm,empno,ename,sal,comm,comm,comm is not null,e1.empno,e2.empno,e1.empno = e2.empno,comm is not null\n" +
                                "and e1.empno = e2.empno,exists (select empno, ename, sal, comm\n" +
                                "from emp e2\n" +
                                " where comm is not null\n" +
                                "and e1.empno = e2.empno)\n" +
                                "ResultColumn->>>>empno,ename,sal,comm,empno,ename,sal,comm\n" +
                                "WhereClause->>>>where comm is not null\n" +
                                "and e1.empno = e2.empno,where exists (select empno, ename, sal, comm\n" +
                                "from emp e2\n" +
                                " where comm is not null\n" +
                                "and e1.empno = e2.empno)\n" +
                                "Join->>>>emp e1,emp e2\n" +
                                "AliasClause->>>>e1,e1,e2,e2\n" +
                                "ObjectName->>>>empno,ename,sal,comm,emp,e1,emp,e1,empno,ename,sal,comm,emp,e2,emp,e2,comm,e1.empno,e2.empno"
                ));
    }

    public void testOracle16() {
        String sql = "select empno, ename, sal,comm\n" +
                "  from emp e1\n" +
                " where exists (select empno, ename, sal, comm\n" +
                "          from emp e2\n" +
                "         where comm is not null\n" +
                "           and e1.empno = e2.empno)    ";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp,emp,emp\n" +
                                "Expression->>>>empno,ename,sal,comm,empno,ename,sal,comm,comm,comm is not null,e1.empno,e2.empno,e1.empno = e2.empno,comm is not null\n" +
                                "           and e1.empno = e2.empno,exists (select empno, ename, sal, comm\n" +
                                "          from emp e2\n" +
                                "         where comm is not null\n" +
                                "           and e1.empno = e2.empno)\n" +
                                "ResultColumn->>>>empno,ename,sal,comm,empno,ename,sal,comm\n" +
                                "WhereClause->>>>where comm is not null\n" +
                                "           and e1.empno = e2.empno,where exists (select empno, ename, sal, comm\n" +
                                "          from emp e2\n" +
                                "         where comm is not null\n" +
                                "           and e1.empno = e2.empno)\n" +
                                "Join->>>>emp e1,emp e2\n" +
                                "AliasClause->>>>e1,e1,e2,e2\n" +
                                "ObjectName->>>>empno,ename,sal,comm,emp,e1,emp,e1,empno,ename,sal,comm,emp,e2,emp,e2,comm,e1.empno,e2.empno"
                ));
    }

    public void testOracle17() {
        String sql = "select empno, ename, sal, comm ,deptno\n" +
                "  from emp e1\n" +
                " where exists (select empno, ename, sal, comm,deptno\n" +
                "          from emp e2\n" +
                "         where comm is not null\n" +
                "           and e1.deptno = e2.deptno)";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>emp,emp,emp,emp\n" +
                                "Expression->>>>empno,ename,sal,comm,deptno,empno,ename,sal,comm,deptno,comm,comm is not null,e1.deptno,e2.deptno,e1.deptno = e2.deptno,comm is not null\n" +
                                "           and e1.deptno = e2.deptno,exists (select empno, ename, sal, comm,deptno\n" +
                                "          from emp e2\n" +
                                "         where comm is not null\n" +
                                "           and e1.deptno = e2.deptno)\n" +
                                "ResultColumn->>>>empno,ename,sal,comm,deptno,empno,ename,sal,comm,deptno\n" +
                                "WhereClause->>>>where comm is not null\n" +
                                "           and e1.deptno = e2.deptno,where exists (select empno, ename, sal, comm,deptno\n" +
                                "          from emp e2\n" +
                                "         where comm is not null\n" +
                                "           and e1.deptno = e2.deptno)\n" +
                                "Join->>>>emp e1,emp e2\n" +
                                "AliasClause->>>>e1,e1,e2,e2\n" +
                                "ObjectName->>>>empno,ename,sal,comm,deptno,emp,e1,emp,e1,empno,ename,sal,comm,deptno,emp,e2,emp,e2,comm,e1.deptno,e2.deptno"
                ));
    }

    public void testOracle18() {
        String sql = "SELECT Customer,OrderDate,SUM(OrderPrice) FROM Orders\n" +
                "GROUP BY Customer,OrderDate";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
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

    public void testOracle19() {
        String sql = "select * from emp order by nvl(comm,0),comm desc;";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "OrderBy->>>>order by nvl(comm,0),comm desc\n" +
                                "Table->>>>emp,emp\n" +
                                "Expression->>>>*,comm,0,nvl(comm,0),comm\n" +
                                "ResultColumn->>>>*\n" +
                                "Join->>>>emp\n" +
                                "FunctionCall->>>>nvl(comm,0)\n" +
                                "ObjectName->>>>*,emp,emp,nvl,comm,comm"
                ));
    }

    public void testOracle20() {
        String sql = "select current_date from dual where 1=1;";

        SelectParse selectParse = new SelectParse();
        boolean isSuccess = selectParse.parse(sql, EDbVendor.dbvoracle);
       
        assertTrue(isSuccess &&
                selectParse.getResultSql().trim().equalsIgnoreCase(
                        "Table->>>>dual,dual\n" +
                                "Expression->>>>current_date,1,1,1=1\n" +
                                "ResultColumn->>>>current_date\n" +
                                "WhereClause->>>>where 1=1\n" +
                                "Join->>>>dual\n" +
                                "ObjectName->>>>current_date,dual,dual"
                ));
    }


}
