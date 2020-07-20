package test719;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;

public class Test {

    public static void main(String[] args) {
        int ret = 0;
        TScriptGenerator scriptGenerator = new TScriptGenerator();
        TGSqlParser sqlparser = new TGSqlParser(EDbVendor.dbvmssql);

        sqlparser.sqltext = "CREATE TABLE [echeckinmedicaldata] (  \n" +
                "   [id] int NOT NULL\n" +
                "IDENTITY(1,1) ,\n" +
                "   [uid]            int     NOT NULL  CONSTRAINT [DF_echeckinmedicaldata_uid]   DEFAULT  ((0)) ,\n" +
                "   [encid]          int     NOT NULL  CONSTRAINT [DF_echeckinmedicaldata_encid]   DEFAULT  ((0)) ,\n" +
                "   [surglist]       text    NULL  CONSTRAINT [DF_echeckinmedicaldata_surglist]   DEFAULT  NULL ,\n" +
                "   [hosplist]       text    NULL  CONSTRAINT [DF_echeckinmedicaldata_hosplist]   DEFAULT  NULL ,\n" +
                "   [medlist]        text    NULL  CONSTRAINT [DF_echeckinmedicaldata_medlist]   DEFAULT  NULL ,\n" +
                "   [allergylist]    text    NULL  CONSTRAINT [DF_echeckinmedicaldata_allergylist]   DEFAULT  NULL ,\n" +
                "   [datetime]       varchar (50) NULL  CONSTRAINT [DF_echeckinmedicaldata_datetime]   DEFAULT  NULL ,\n" +
                "   [delflag]        tinyint NOT NULL   DEFAULT  ((0)),\n" +
                "  INDEX idx_echeckinmedicaldata_uid(uid),\n" +
                "  INDEX idx_echeckinmedicaldata_encid(encid),\n" +
                "   CONSTRAINT PK_echeckinmedicaldata PRIMARY KEY CLUSTERED ( id ASC ) WITH (IGNORE_DUP_KEY = OFF)\n" +
                "   ON [PRIMARY]) ON [PRIMARY];";
        ret = sqlparser.parse();

        for (int j = 0; j < sqlparser.sqlstatements.size(); j++) {
            System.out.println("sql type: " + sqlparser.sqlstatements.get(j).sqlstatementtype + "(" + sqlparser.sqlstatements.get(j).getClass().toString() + ")" + ", " + sqlparser.sqlstatements.get(j).getStartToken().toString() + "(" + sqlparser.sqlstatements.get(j).getStartToken().lineNo + "," + sqlparser.sqlstatements.get(j).getStartToken().columnNo + ")");
            System.out.println(scriptGenerator.generateScript(sqlparser.sqlstatements.get(j)));

        }

        if (ret == 0) {
            System.out.println("sqls: " + sqlparser.sqlstatements.size());
        } else {
            System.out.println("error: " + sqlparser.getErrormessage());
        }
    }
}
