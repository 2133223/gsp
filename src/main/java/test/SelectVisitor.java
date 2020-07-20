package test;

import gudusoft.gsqlparser.*;
import gudusoft.gsqlparser.nodes.*;
import gudusoft.gsqlparser.stmt.*;
import gudusoft.gsqlparser.stmt.mssql.*;
import gudusoft.gsqlparser.stmt.oracle.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SelectVisitor extends TParseTreeVisitor {

    private Map<String, String> map = new HashMap<>();

    public Map<String, String> getMap() {
        return map;
    }

    public void preVisit(TSelectSqlStatement node) {
        toAccpect(node.getCteList());

        if (node.isCombinedQuery()) {
            node.getLeftStmt().accept(this);
            node.getRightStmt().accept(this);

            toAccpect(node.getOrderbyClause());
            toAccpect(node.getLimitClause());
            toAccpect(node.getForUpdateClause());
            toAccpect(node.getComputeClause());
        }

        toAccpect(node.getValueClause());
        toAccpect(node.getTopClause());
        toAccpect(node.getSelectDistinct());

        if (null != node.getResultColumnList()) {
            for (int i = 0; i < node.getResultColumnList().size(); i++) {
                node.getResultColumnList().getElement(i).accept(this);
            }
        }

        toAccpect(node.getIntoClause());

        if (node.joins.size() > 0) {
            node.joins.accept(this);
        }

        toAccpect(node.getWhereClause());
        toAccpect(node.getHierarchicalClause());
        toAccpect(node.getGroupByClause());
        toAccpect(node.getQualifyClause());
        toAccpect(node.getOrderbyClause());
        toAccpect(node.getLimitClause());
        toAccpect(node.getForUpdateClause());
        toAccpect(node.getComputeClause());
    }


    public void preVisit(TIntoClause node) {
    }

    public void preVisit(TValueClause node) {
    }

    public void preVisit(TTopClause node) {
    }

    public void preVisit(TSelectDistinct node) {
    }


    public void preVisit(TJoinItem node) {
        if (node.getKind() == TBaseType.join_source_table) {
            node.getTable().accept(this);
        } else if (node.getKind() == TBaseType.join_source_join) {
            node.getJoin().accept(this);
        }
        toAccpect(node.getOnCondition());
        toAccpect(node.getUsingColumns());
    }

    public void preVisit(TJoin node) {
        if (node.getTable() != null) {
            node.getTable().accept(this);
        }
        switch (node.getKind()) {
            case TBaseType.join_source_fake:
                break;
            case TBaseType.join_source_table:
            case TBaseType.join_source_join:
                for (int i = node.getJoinItems().size() - 1; i >= 0; i--) {
                    TJoinItem joinItem = node.getJoinItems().getJoinItem(i);

                    toAccpect(joinItem.getTable());
                    toAccpect(joinItem.getJoin());
                    toAccpect(joinItem.getOnCondition());
                }
        }

    }

    public void preVisit(TTable node) {
        switch (node.getTableType()) {
            case objectname: {
                node.getTableName().accept(this);
                toAccpect(node.getAliasClause());
                break;
            }
            case tableExpr: {
                node.getTableExpr().accept(this);
                toAccpect(node.getAliasClause());
                break;
            }
            case subquery: {
                node.getSubquery().accept(this);
                toAccpect(node.getAliasClause());
                break;
            }
            case function: {
                node.getFuncCall().accept(this);
                toAccpect(node.getAliasClause());
                break;
            }
            case pivoted_table: {
                node.getPivotedTable().accept(this);
                break;
            }
            case output_merge: {
                node.getOutputMerge().accept(this);
                break;
            }
            case containsTable: {
                node.getContainsTable().accept(this);
                break;
            }
            case openrowset: {
                node.getOpenRowSet().accept(this);
                break;
            }
            case openxml: {
                node.getOpenXML().accept(this);
                break;
            }
            case opendatasource: {
                node.getOpenDatasource().accept(this);
                break;
            }
            case openquery: {
                node.getOpenquery().accept(this);
                break;
            }
            case datachangeTable: {
                node.getDatachangeTable().accept(this);
                break;
            }
            case rowList: {
                node.getRowList().accept(this);
                break;
            }
            case xmltable: {
                node.getXmlTable().accept(this);
                break;
            }
            case informixOuter: {
                preVisit(node.getOuterClause());
                break;
            }
            case table_ref_list: {
                node.getFromTableList().accept(this);
                break;
            }
            case hiveFromQuery: {
                node.getHiveFromQuery().accept(this);
                break;
            }
            default:
                break;

        }

        if (node.getTableHintList() != null) {
            for (int i = 0; i < node.getTableHintList().size(); i++) {
                TTableHint tableHint = node.getTableHintList().getElement(i);
                tableHint.accept(this);
            }
        }
    }

    public void preVisit(TInformixOuterClause node) {

    }

    public void preVisit(TResultColumn node) {
        node.getExpr().accept(this);
        toAccpect(node.getAliasClause());
    }

    public void preVisit(TExpressionList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getExpression(i).accept(this);
        }
    }

    public void preVisit(TCTE node) {
        node.getTableName().accept(this);

        toAccpect(node.getColumnList());
        if (node.getSubquery() != null) {
            node.getSubquery().accept(this);
        } else if (node.getUpdateStmt() != null) {
            node.getUpdateStmt().accept(this);
        } else if (node.getInsertStmt() != null) {
            node.getInsertStmt().accept(this);
        } else if (node.getDeleteStmt() != null) {
            node.getDeleteStmt().accept(this);
        }
    }

    public void preVisit(TObjectNameList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getObjectName(i).accept(this);
        }
    }

    public void preVisit(TObjectName node) {

    }

    public void preVisit(TLimitClause node) {
    }

    public void preVisit(TExpression expr) {
        toAccpect(expr.getLeftOperand());
        toAccpect(expr.getRightOperand());
        toAccpect(expr.getSubQuery());
        toAccpect(expr.getObjectOperand());
        toAccpect(expr.getExprList());
        toAccpect(expr.getLikeEscapeOperand());
        toAccpect(expr.getBetweenOperand());
        toAccpect(expr.getCaseExpression());
        toAccpect(expr.getFunctionCall());
    }


    public void preVisit(TUpdateSqlStatement stmt) {
        toAccpect(stmt.getCteList());
        toAccpect(stmt.getTopClause());
        toAccpect(stmt.getTargetTable());

        for (int i = 0; i < stmt.getResultColumnList().size(); i++) {
            toAccpect(stmt.getResultColumnList()
                    .getResultColumn(i)
                    .getExpr());
        }

        if (stmt.joins.size() > 0) {
            stmt.joins.accept(this);
        }
        toAccpect(stmt.getWhereClause());
        toAccpect(stmt.getOrderByClause());
        toAccpect(stmt.getLimitClause());
        toAccpect(stmt.getOutputClause());
        toAccpect(stmt.getReturningClause());
    }

    private void toAccpect(TParseTreeNode node) {
        if (null != node) {
            node.accept(this);
        }
    }

    public void preVisit(TOrderBy node) {
        node.getItems().accept(this);
    }

    public void preVisit(TOrderByItem node) {
        toAccpect(node.getSortKey());
    }

    public void preVisit(TOrderByItemList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getOrderByItem(i).accept(this);
        }
    }

    public void preVisit(TComputeClause node) {
        node.getItems().accept(this);
    }


    public void preVisit(TComputeClauseItem node) {
        toAccpect(node.getByExprlist());
    }

    public void preVisit(TComputeClauseItemList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getComputeClauseItem(i).accept(this);
        }
    }


    private void toMap(String key, TParseTreeNode node) {
        String v = map.get(key);
        String value = node.toString();
        if (null != v) {
            v += "," + value;
        } else {
            v = value;
        }
        map.put(key, v);
    }

    public void postVisit(TConstraint node) {
        toMap(Constant.TConstraintStr, node);
    }

    public void postVisit(TInformixOuterClause node) {
        toMap(Constant.TInformixOuterClause, node);
    }

    public void postVisit(TTable node) {
        toMap(Constant.TTable, node);
    }

    public void postVisit(TFromTableList node) {
        toMap(Constant.TFromTableList, node);
    }

    public void postVisit(TCTE node) {
        toMap(Constant.TCTE, node);
    }

    public void postVisit(TReturningClause node) {
        toMap(Constant.TReturningClause, node);
    }

    public void postVisit(TAliasClause node) {
        toMap(Constant.TAliasClause, node);
    }

    public void postVisit(TQualifyClause node) {
        toMap(Constant.TQualifyClause, node);
    }

    public void postVisit(TGroupBy node) {
        toMap(Constant.TGroupBy, node);
    }

    public void postVisit(THierarchical node) {
        toMap(Constant.THierarchical, node);
    }

    public void postVisit(TWhereClause node) {
        toMap(Constant.TWhereClause, node);
    }

    public void postVisit(TJoin node) {
        toMap(Constant.TJoin, node);
    }

    public void postVisit(TIntoClause node) {
        toMap(Constant.TIntoClause, node);
    }

    public void postVisit(TResultColumn node) {
        toMap(Constant.TResultColumn, node);
    }

    public void postVisit(TSelectDistinct node) {
        toMap(Constant.TSelectDistinct, node);
    }

    public void postVisit(TValueClause node) {
        toMap(Constant.TValueClause, node);
    }

    public void postVisit(TComputeClause node) {
        toMap(Constant.TComputeClause, node);
    }

    public void postVisit(TForUpdate node) {
        toMap(Constant.TForUpdate, node);
    }

    public void postVisit(TOrderBy node) {
        toMap(Constant.TOrderBy, node);
    }

    public void postVisit(TExpression expression) {
        toMap(Constant.TExpression, expression);
    }

    public void postVisit(TLimitClause limit) {
        toMap(Constant.TLimitClause, limit);
    }

    public void postVisit(TTopClause top) {
        toMap(Constant.TTopClause, top);
    }

    public void postVisit(TSelectSqlStatement select) {
        toAccpect(select.getIntoClause());

        if (select.getJoins().size() > 1
                && select.dbvendor == EDbVendor.dbvoracle
                && select.getJoins().getJoin(0).getJoinItems().size() == 0) {
            toAccpect(select.getWhereClause());
        }
    }

    public void postVisit(TCreateTableSqlStatement create) {
        toMap(Constant.TCreateTableSqlStatement, create);
        toAccpect(create.getSubQuery());
    }

    public void postVisit(TFunctionCall function) {
        toMap(Constant.TFunctionCall, function);
    }

    public void postVisit(TConstant constant) {
        toMap(Constant.TConstant, constant);
    }

    public void postVisit(TObjectName identifier) {
        toMap(Constant.TObjectName, identifier);
    }

    public void postVisit(TTypeName type) {
        toMap(Constant.TTypeName, type);
    }


    public void preVisit(TResultColumnList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getResultColumn(i).accept(this);
        }
    }


    public void preVisit(TAliasClause node) {
        node.getAliasName().accept(this);
    }

    public void preVisit(TInExpr node) {
        toAccpect(node.getSubQuery());
        toAccpect(node.getGroupingExpressionItemList());
    }


    public void preVisit(TGroupingExpressionItem node) {
        toAccpect(node.getExpr());
        toAccpect(node.getExprList());
    }


    public void preVisit(TUnpivotInClause node) {
        for (int i = 0; i < node.getItems().size(); i++) {
            node.getItems().getElement(i).accept(this);
        }
    }

    public void preVisit(TPivotInClause node) {
        toAccpect(node.getItems());
        toAccpect(node.getSubQuery());
    }

    public void preVisit(TPivotedTable node) {
        for (int i = node.getPivotClauseList().size() - 1; i >= 0; i--) {
            TPivotClause pivotClause = node.getPivotClauseList()
                    .getElement(i);
            toAccpect(pivotClause.getAliasClause());
            pivotClause.accept(this);

            if (i == 0) {
                node.getTableSource().accept(this);
            }
        }
    }

    public void preVisit(TPivotClause node) {
        toAccpect(node.getAggregation_function());
        toAccpect(node.getValueColumn());

        if (node.getValueColumnList() != null) {
            for (int i = 0; i < node.getValueColumnList().size(); i++) {
                node.getValueColumnList().getObjectName(i).accept(this);
            }
        }

        toAccpect(node.getPivotColumn());
        toAccpect(node.getPivotColumnList());
        toAccpect(node.getAggregation_function_list());
        toAccpect(node.getIn_result_list());
        toAccpect(node.getPivotInClause());
        toAccpect(node.getUnpivotInClause());
    }


    public void preVisit(TJoinList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getJoin(i).accept(this);
        }
    }


    public void preVisit(TWhereClause node) {
        node.getCondition().accept(this);
    }

    public void preVisit(THierarchical node) {
        if (node.getConnectByList() != null) {
            for (int i = 0; i < node.getConnectByList().size(); i++) {
                node.getConnectByList().getElement(i).accept(this);
            }
        }

        toAccpect(node.getStartWithClause());
    }

    public void preVisit(TConnectByClause node) {
        node.getCondition().accept(this);
    }

    public void preVisit(TRollupCube node) {
        node.getItems().accept(this);
    }

    public void preVisit(TGroupBy node) {
        toAccpect(node.getItems());
        toAccpect(node.getHavingClause());
    }

    public void preVisit(TGroupByItem node) {
        if (node.getExpr() != null) {
            TExpression ge = node.getExpr();
            ge.accept(this);
        } else if (node.getGroupingSet() != null) {
            node.getGroupingSet().accept(this);
        } else if (node.getRollupCube() != null) {
            node.getRollupCube().accept(this);
        }
    }

    public void preVisit(TGroupingSet node) {
        for (int i = 0; i < node.getItems().size(); i++) {
            if (node.getItems()
                    .getGroupingSetItem(i)
                    .getGrouping_expression() != null) {
                TExpression ge = node.getItems()
                        .getGroupingSetItem(i)
                        .getGrouping_expression();
                ge.accept(this);
            } else if (node.getItems()
                    .getGroupingSetItem(i)
                    .getRollupCubeClause() != null) {
                TRollupCube rollupCube = node.getItems()
                        .getGroupingSetItem(i)
                        .getRollupCubeClause();
                rollupCube.accept(this);
            }
        }
    }

    public void preVisit(TGroupByItemList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getGroupByItem(i).accept(this);
        }
    }


    public void preVisit(TForUpdate node) {
    }

    public void preVisit(TStatementList node) {
        for (int i = 0; i < node.size(); i++) {
            node.get(i).accept(this);
        }
    }

    public void preVisit(TPlsqlCreatePackage node) {
        toAccpect(node.getEndlabelName());
        if (node.getDeclareStatements().size() > 0) {
            node.getDeclareStatements().accept(this);
        }
    }

    public void preVisit(TMssqlCreateFunction node) {
        node.getFunctionName().accept(this);
        toAccpect(node.getEndlabelName());
        toAccpect(node.getReturnDataType());
        toAccpect(node.getParameterDeclarations());
        if (node.getBodyStatements().size() > 0) {
            node.getBodyStatements().accept(this);
        }
    }

    public void preVisit(TCreateDatabaseSqlStatement stmt) {
        stmt.getDatabaseName().accept(this);
    }

    public void preVisit(TCreateSchemaSqlStatement stmt) {
        stmt.getSchemaName().accept(this);
    }

    public void preVisit(TUseDatabase stmt) {
        stmt.getDatabaseName().accept(this);
    }

    public void preVisit(TMssqlBlock node) {
        if (node.getBodyStatements().size() > 0) {
            node.getBodyStatements().accept(this);
        }
    }

    private void doProcedureSpecification(TPlsqlCreateProcedure node) {
        node.getProcedureName().accept(this);

        toAccpect(node.getEndlabelName());
        toAccpect(node.getParameterDeclarations());

        if (node.getInnerStatements().size() > 0)
            node.getInnerStatements().accept(this);
        if (node.getDeclareStatements().size() > 0) {
            node.getDeclareStatements().accept(this);
        }

        if (node.getBodyStatements().size() > 0) {
            node.getBodyStatements().accept(this);
        }
        toAccpect(node.getExceptionClause());
    }

    private void doFunctionSpecification(TPlsqlCreateFunction node) {
        if (node.isWrapped())
            return;
        node.getFunctionName().accept(this);
        toAccpect(node.getEndlabelName());
        node.getReturnDataType().accept(this);

        toAccpect(node.getParameterDeclarations());

        if (node.getDeclareStatements().size() > 0) {
            node.getDeclareStatements().accept(this);
        }
        if (node.getBodyStatements().size() > 0) {
            node.getBodyStatements().accept(this);
        }
        toAccpect(node.getExceptionClause());
    }

    public void preVisit(TPlsqlCreateFunction node) {
        switch (node.getKind()) {
            case TBaseType.kind_create:
                doFunctionSpecification(node);
                break;
            case TBaseType.kind_declare:
                node.getFunctionName().accept(this);
                if (node.getParameterDeclarations() != null)
                    node.getParameterDeclarations().accept(this);
                break;
            case TBaseType.kind_define:
                doFunctionSpecification(node);
                break;
        }
    }

    public void preVisit(TCommonBlock node) {
        toAccpect(node.getLabelName());

        if (node.getDeclareStatements().size() > 0)
            node.getDeclareStatements().accept(this);
        if (node.getBodyStatements().size() > 0)
            node.getBodyStatements().accept(this);
        toAccpect(node.getExceptionClause());


    }

    public void preVisit(TExceptionClause node) {
        node.getHandlers().accept(this);
    }

    public void preVisit(TExceptionHandler node) {
        node.getExceptionNames().accept(this);
        node.getStatements().accept(this);
    }

    public void preVisit(TExceptionHandlerList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getExceptionHandler(i).accept(this);
        }

    }

    public void preVisit(TAlterTableOption node) {
        switch (node.getOptionType()) {
            case AddColumn:
                node.getColumnDefinitionList().accept(this);
                break;
            case AlterColumn:
                node.getColumnName().accept(this);
                break;
            case ChangeColumn:
                node.getColumnName().accept(this);
                break;
            case DropColumn:
                for (int i = 0; i < node.getColumnNameList().size(); i++) {
                    node.getColumnNameList().getObjectName(i).accept(this);
                }
                break;
            case ModifyColumn:
                node.getColumnDefinitionList().accept(this);
                break;
            case RenameColumn:
                node.getColumnName().accept(this);
                node.getNewColumnName().accept(this);
                break;
            case AddConstraint:
                node.getConstraintList().accept(this);
                break;
            case switchPartition:
                node.getNewTableName().accept(this);
                toAccpect(node.getPartitionExpression1());
                toAccpect(node.getPartitionExpression2());
                break;
            default:

        }
    }

    public void preVisit(TAlterTableStatement stmt) {
        stmt.getTableName().accept(this);

        if (stmt.getAlterTableOptionList() != null) {
            for (int i = 0; i < stmt.getAlterTableOptionList().size(); i++) {
                stmt.getAlterTableOptionList()
                        .getAlterTableOption(i)
                        .accept(this);
            }
        }

    }

    public void preVisit(TTypeName node) {

    }

    public void preVisit(TColumnDefinition node) {
        node.getColumnName().accept(this);
        toAccpect(node.getDatatype());

        if ((node.getConstraints() != null)
                && (node.getConstraints().size() > 0)) {
            node.getConstraints().accept(this);
        }
    }

    public void preVisit(TColumnDefinitionList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getColumn(i).accept(this);
        }
    }

    public void preVisit(TMergeWhenClause node) {
        toAccpect(node.getCondition());
        toAccpect(node.getUpdateClause());
        toAccpect(node.getInsertClause());
        toAccpect(node.getDeleteClause());
    }

    public void preVisit(TMergeUpdateClause node) {

        if (node.getUpdateColumnList() != null) {
            for (int i = 0; i < node.getUpdateColumnList().size(); i++) {
                node.getUpdateColumnList()
                        .getResultColumn(i)
                        .getExpr()
                        .accept(this);
            }
        }

        if (node.getUpdateWhereClause() != null) {
            node.getUpdateWhereClause().accept(this);
        }

        if (node.getDeleteWhereClause() != null) {
            node.getDeleteWhereClause().accept(this);
        }

    }

    public void preVisit(TMergeInsertClause node) {
        toAccpect(node.getColumnList());

        if (node.getValuelist() != null) {
            for (int i = 0; i < node.getValuelist().size(); i++) {
                node.getValuelist()
                        .getResultColumn(i)
                        .getExpr()
                        .accept(this);
            }
        }
        toAccpect(node.getInsertWhereClause());
    }

    public void preVisit(TMergeDeleteClause node) {
    }

    public void preVisit(TConstraint node) {
        switch (node.getConstraint_type()) {
            case notnull:
                break;
            case unique:
                break;
            case check:
                toAccpect(node.getCheckCondition());
                break;
            case primary_key:
                toAccpect(node.getColumnList());
                break;
            case foreign_key:
            case reference:
                toAccpect(node.getColumnList());
                toAccpect(node.getReferencedObject());
                toAccpect(node.getReferencedColumnList());
                break;
            case default_value:
                node.getDefaultExpression().accept(this);
                break;
            default:
                break;
        }
    }

    public void preVisit(TConstraintList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getConstraint(i).accept(this);
        }
    }

    public void preVisit(TCreateMaterializedSqlStatement stmt) {
        stmt.getViewName().accept(this);

        if (stmt.getViewAliasClause() != null) {
            for (int i = 0; i < stmt.getViewAliasClause()
                    .getViewAliasItemList()
                    .size(); i++) {
                TViewAliasItem viewAliasItem = stmt.getViewAliasClause()
                        .getViewAliasItemList()
                        .getViewAliasItem(i);
                if (viewAliasItem.getAlias() == null)
                    continue;
                viewAliasItem.getAlias().accept(this);
            }
        }
        stmt.getSubquery().accept(this);
    }

    public void preVisit(TCreateViewSqlStatement stmt) {
        if (stmt.getViewAttributeList() != null) {
            for (int i = 0; i < stmt.getViewAttributeList().size(); i++) {
                stmt.getViewAttributeList().getObjectName(i).accept(this);
            }
        }
        stmt.getViewName().accept(this);

        if (stmt.getViewAliasClause() != null) {
            for (int i = 0; i < stmt.getViewAliasClause()
                    .getViewAliasItemList()
                    .size(); i++) {
                TViewAliasItem viewAliasItem = stmt.getViewAliasClause()
                        .getViewAliasItemList()
                        .getViewAliasItem(i);
                if (viewAliasItem.getAlias() == null)
                    continue;
                viewAliasItem.getAlias().accept(this);
            }
        }
        stmt.getSubquery().accept(this);
    }

    public void postVisit(TCreateViewSqlStatement stmt) {
        toMap(Constant.TCreateViewSqlStatement, stmt);
    }

    public void preVisit(TMssqlCreateTrigger stmt) {
        stmt.getTriggerName().accept(this);
        stmt.getOnTable().accept(this);
        if (stmt.getBodyStatements().size() > 0)
            stmt.getBodyStatements().accept(this);
    }

    public void preVisit(TCreateSequenceStmt stmt) {
        stmt.getSequenceName().accept(this);
        if (stmt.getOptions() != null) {
            for (int i = 0; i < stmt.getOptions().size(); i++) {
                TSequenceOption sequenceOption = stmt.getOptions()
                        .getElement(i);
                switch (sequenceOption.getSequenceOptionType()) {
                    case start:
                    case startWith:
                        break;
                    case restart:
                    case restartWith:
                        break;
                    case increment:
                    case incrementBy:
                        break;
                    case minValue:
                        break;
                    case maxValue:
                        break;
                    case cycle:
                        break;
                    case noCycle:
                        break;
                    case cache:
                        break;
                    case noCache:
                        break;
                    case order:
                        break;
                    case noOrder:
                        break;
                    default:
                        break;
                }

            }
        }
    }

    public void preVisit(TCreateSynonymStmt stmt) {
        stmt.getSynonymName().accept(this);
        stmt.getForName().accept(this);
    }

    public void preVisit(TExecParameter node) {
        toAccpect(node.getParameterName());
        node.getParameterValue().accept(this);
    }

    public void preVisit(TMssqlExecute stmt) {
        switch (stmt.getExecType()) {
            case TBaseType.metExecSp:
                stmt.getModuleName().accept(this);
                if (stmt.getParameters() != null) {
                    for (int i = 0; i < stmt.getParameters().size(); i++) {
                        stmt.getParameters()
                                .getExecParameter(i)
                                .accept(this);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void preVisit(TMssqlDeclare stmt) {
        switch (stmt.getDeclareType()) {
            case variable:
                if (stmt.getDeclareType() == EDeclareType.variable) {
                    stmt.getVariables().accept(this);
                }
                break;
            default:
                break;
        }
    }

    public void preVisit(TMssqlSet stmt) {
        switch (stmt.getSetType()) {
            case TBaseType.mstUnknown:
                break;
            case TBaseType.mstLocalVar:
                stmt.getVarName().accept(this);
                stmt.getVarExpr().accept(this);
                break;
            case TBaseType.mstLocalVarCursor:
                break;
            case TBaseType.mstSetCmd:
                break;
            case TBaseType.mstXmlMethod:
                break;
            case TBaseType.mstSybaseLocalVar:
                break;
            default:
                break;

        }
    }

    public void preVisit(TMergeSqlStatement stmt) {
        toAccpect(stmt.getCteList());

        stmt.getTargetTable().accept(this);
        stmt.getUsingTable().accept(this);

        stmt.getCondition().accept(this);

        if (stmt.getWhenClauses() != null) {
            for (int i = 0; i < stmt.getWhenClauses().size(); i++) {
                TMergeWhenClause whenClause = stmt.getWhenClauses()
                        .getElement(i);
                whenClause.accept(this);
            }
        }
    }

    public void preVisit(TCreateIndexSqlStatement stmt) {
        toAccpect(stmt.getIndexName());

        stmt.getTableName().accept(this);

        for (int i = 0; i < stmt.getColumnNameList().size(); i++) {
            TOrderByItem orderByItem = stmt.getColumnNameList()
                    .getOrderByItem(i);
            orderByItem.getSortKey().accept(this);
        }
    }

    public void preVisit(TCreateTableSqlStatement stmt) {
        stmt.getTargetTable().accept(this);

        if (stmt.getColumnList().size() > 0) {
            stmt.getColumnList().accept(this);
        }

        if ((stmt.getTableConstraints() != null)
                && (stmt.getTableConstraints().size() > 0)) {
            stmt.getTableConstraints().accept(this);
        }
        toAccpect(stmt.getSubQuery());
    }

    public void preVisit(TDropIndexSqlStatement stmt) {
    }

    public void preVisit(TDropTableSqlStatement stmt) {
        stmt.getTableName().accept(this);
    }

    public void preVisit(TTruncateStatement stmt) {
        stmt.getTableName().accept(this);
    }

    public void preVisit(TDropViewSqlStatement stmt) {
        stmt.getViewName().accept(this);
    }

    public void preVisit(TDeleteSqlStatement stmt) {
        toAccpect(stmt.getCteList());
        toAccpect(stmt.getTopClause());
        stmt.getTargetTable().accept(this);

        if (stmt.joins.size() > 0) {
            stmt.joins.accept(this);
        }
        toAccpect(stmt.getOutputClause());
        toAccpect(stmt.getWhereClause());
        toAccpect(stmt.getReturningClause());
    }

    public void postVisit(TDeleteSqlStatement stmt) {
        toMap(Constant.TDeleteSqlStatement, stmt);
    }


    public void preVisit(TWithinGroup withinGroup) {
        withinGroup.getOrderBy().accept(this);
    }

    public void preVisit(TKeepDenseRankClause keepDenseRankClause) {
        keepDenseRankClause.getOrderBy().accept(this);
    }

    public void preVisit(TPlsqlCreateProcedure node) {

        switch (node.getKind()) {
            case TBaseType.kind_define:
                doProcedureSpecification(node);
                break;
            case TBaseType.kind_declare:
                node.getProcedureName().accept(this);
                if (node.getParameterDeclarations() != null)
                    node.getParameterDeclarations().accept(this);

                break;
            case TBaseType.kind_create:
                doProcedureSpecification(node);
                break;
        }
    }

    public void preVisit(TWindowDef windowDef) {
        toAccpect(windowDef.getWithinGroup());
        toAccpect(windowDef.getKeepDenseRankClause());

        if (windowDef.isIncludingOverClause()) {
            toAccpect(windowDef.getPartitionClause()
                    .getExpressionList());

            toAccpect(windowDef.getOrderBy());

            if (windowDef.getWindowFrame() != null) {
                TWindowFrame windowFrame = windowDef.getWindowFrame();
                windowFrame.getStartBoundary().accept(this);
                toAccpect(windowFrame.getEndBoundary());
            }
        }
    }

    public void preVisit(TFunctionCall node) {
        node.getFunctionName().accept(this);
        switch (node.getFunctionType()) {
            case unknown_t:
                toAccpect(node.getArgs());
                break;
            case udf_t:
            case case_n_t:
            case chr_t:
                toAccpect(node.getArgs());
                toAccpect(node.getAnalyticFunction());
                break;
            case cast_t:
                node.getExpr1().accept(this);
                node.getTypename().accept(this);
                break;
            case convert_t:
                toAccpect(node.getTypename());
                node.getParameter().accept(this);
                break;
            case trim_t:
                if (node.getTrimArgument() != null) {
                    TTrimArgument trimArgument = node.getTrimArgument();
                    toAccpect(trimArgument.getTrimCharacter());
                    trimArgument.getStringExpression().accept(this);
                }
                break;
            case extract_t:
                if (node.getArgs() != null) {
                    node.getArgs().accept(this);
                } else {
                    toAccpect(node.getExpr1());
                }
                break;
            case treat_t:
                node.getExpr1().accept(this);
                node.getTypename().accept(this);
                break;
            case contains_t:
                node.getExpr1().accept(this);
                node.getExpr2().accept(this);
                break;
            case freetext_t:
                node.getExpr1().accept(this);
                node.getExpr2().accept(this);
                break;
            case substring_t:
                node.getExpr1().accept(this);
                toAccpect(node.getExpr2());
                toAccpect(node.getExpr3());
                break;
            case range_n_t:
            case position_t:
            case xmlquery_t:
            case xmlcast_t:
            case match_against_t:
            case adddate_t:
            case date_add_t:
            case subdate_t:
            case date_sub_t:
            case timestampadd_t:
            case timestampdiff_t:
                break;
            default:
                toAccpect(node.getArgs());
                break;
        }
        toAccpect(node.getWindowDef());
    }

    public void preVisit(TWindowFrameBoundary boundary) {
    }

    public void preVisit(TInsertSqlStatement stmt) {
        toAccpect(stmt.getCteList());
        toAccpect(stmt.getTargetTable());
        toAccpect(stmt.getColumnList());

        switch (stmt.getInsertSource()) {
            case values:
                for (int i = 0; i < stmt.getValues().size(); i++) {
                    TMultiTarget multiTarget = stmt.getValues()
                            .getMultiTarget(i);

                    for (int j = 0; j < multiTarget.getColumnList().size(); j++) {
                        if (multiTarget.getColumnList()
                                .getResultColumn(j)
                                .isPlaceHolder())
                            continue; // teradata allow empty value
                        multiTarget.getColumnList()
                                .getResultColumn(j)
                                .getExpr()
                                .accept(this);
                    }

                }
                break;
            case subquery:
                stmt.getSubQuery().accept(this);
                break;
            case values_empty:
                break;
            case values_function:
                stmt.getFunctionCall().accept(this);
                break;
            case values_oracle_record:
                stmt.getRecordName().accept(this);
                break;
            case set_column_value:
                stmt.getSetColumnValues().accept(this);
                break;
            case execute:
                stmt.getExecuteStmt().accept(this);
                break;
            default:
                break;
        }
        toAccpect(stmt.getReturningClause());

    }

    public void preVisit(TMultiTarget node) {
        toAccpect(node.getColumnList());
        toAccpect(node.getSubQuery());
    }

    public void preVisit(TMultiTargetList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getMultiTarget(i).accept(this);
        }
    }


    public void preVisit(TCTEList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getCTE(i).accept(this);
        }
    }

    public void preVisit(TAssignStmt node) {
        node.getLeft().accept(this);
        node.getExpression().accept(this);
    }

    public void preVisit(TMssqlCreateXmlSchemaCollectionStmt stmt) {
        stmt.getSchemaName().accept(this);
        stmt.getExpr().accept(this);
    }

    public void preVisit(TIfStmt node) {
        node.getCondition().accept(this);
        node.getThenStatements().accept(this);
        if (node.getElseifStatements().size() > 0) {
            for (int i = 0; i < node.getElseifStatements().size(); i++) {
                TElsifStmt elsifStmt = (TElsifStmt) node.getElseifStatements()
                        .get(i);

                elsifStmt.getCondition().accept(this);
                elsifStmt.getThenStatements().accept(this);
            }
        }
        if (node.getElseStatements().size() > 0) {
            node.getElseStatements().accept(this);
        }
    }

    public void preVisit(TMssqlIfElse node) {
        toAccpect(node.getCondition());

        node.getStmt().accept(this);
    }

    public void preVisit(TBasicStmt node) {
        node.getExpr().accept(this);
    }

    public void preVisit(TCaseStmt node) {
    }

    public void preVisit(TCaseExpression node) {
        toAccpect(node.getInput_expr());
        for (int i = 0; i < node.getWhenClauseItemList().size(); i++) {
            node.getWhenClauseItemList()
                    .getWhenClauseItem(i)
                    .getComparison_expr()
                    .accept(this);
            node.getWhenClauseItemList()
                    .getWhenClauseItem(i)
                    .getReturn_expr()
                    .accept(this);
        }
        toAccpect(node.getElse_expr());

        if (node.getElse_statement_list().size() > 0) {
            node.getElse_statement_list().accept(this);
        }
    }

    public void preVisit(TWhenClauseItemList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getWhenClauseItem(i).accept(this);
        }
    }

    public void preVisit(TWhenClauseItem node) {
        node.getComparison_expr().accept(this);
        if (node.getReturn_expr() != null) {
            node.getReturn_expr().accept(this);
        } else if (node.getStatement_list().size() > 0) {
            node.getStatement_list().accept(this);
        }
    }

    public void preVisit(TCloseStmt node) {
        node.getCursorName().accept(this);
    }

    public void preVisit(TPlsqlCreateTrigger stmt) {
        stmt.getTriggerName().accept(this);
        stmt.getTriggerBody().accept(this);
    }

    public void preVisit(TTypeAttribute node) {
        node.getAttributeName().accept(this);
        node.getDatatype().accept(this);
    }

    public void preVisit(TTypeAttributeList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getAttributeItem(i).accept(this);
        }
    }

    public void preVisit(TPlsqlCreateTypeBody stmt) {
        stmt.getTypeName().accept(this);
        stmt.getBodyStatements().accept(this);
    }

    public void preVisit(TPlsqlVarrayTypeDefStmt node) {
        node.getTypeName().accept(this);
        node.getElementDataType().accept(this);
    }

    public void preVisit(TPlsqlTableTypeDefStmt node) {
        node.getTypeName().accept(this);
        node.getElementDataType().accept(this);
    }

    public void preVisit(TPlsqlCreateType node) {
        node.getTypeName().accept(this);

        if (node.getAttributes() != null) {
            for (int i = 0; i < node.getAttributes().size(); i++) {
                node.getAttributes()
                        .getAttributeItem(i)
                        .getAttributeName()
                        .accept(this);
                node.getAttributes()
                        .getAttributeItem(i)
                        .getDatatype()
                        .accept(this);
            }
        }
    }

    public void preVisit(TPlsqlCreateType_Placeholder node) {
        TPlsqlCreateType createType = null;

        switch (node.getCreatedType()) {
            case octIncomplete:
                createType = node.getObjectStatement();
                createType.getTypeName().accept(this);
                break;
            case octObject:
                node.getObjectStatement().accept(this);
                break;
            case octNestedTable:
                node.getNestedTableStatement().accept(this);
                break;
            case octVarray:
                node.getVarrayStatement().accept(this);
                break;
            default:
                break;

        }
    }

    public void preVisit(TMssqlCommit node) {
    }

    public void preVisit(TMssqlRollback node) {

    }

    public void preVisit(TMssqlSaveTran node) {

    }

    public void postVisit(TMssqlSaveTran node) {
        toMap(Constant.TMssqlSaveTran, node);
    }

    public void preVisit(TMssqlGo node) {
    }

    public void preVisit(TMssqlCreateProcedure node) {
        node.getProcedureName().accept(this);
        toAccpect(node.getParameterDeclarations());
        if (node.getBodyStatements().size() > 0) {
            node.getBodyStatements().accept(this);
        }
    }

    public void preVisit(TParameterDeclarationList list) {
        for (int i = 0; i < list.size(); i++) {
            list.getParameterDeclarationItem(i).accept(this);
        }
    }

    public void preVisit(TParameterDeclaration node) {
        node.getParameterName().accept(this);
        node.getDataType().accept(this);
        toAccpect(node.getDefaultValue());
    }

    public void preVisit(TDeclareVariable node) {
        node.getVariableName().accept(this);
        toAccpect(node.getDatatype());
        toAccpect(node.getDefaultValue());
    }

    public void preVisit(TDeclareVariableList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getDeclareVariable(i).accept(this);
        }
    }

    public void preVisit(TVarDeclStmt node) {
        switch (node.getDeclareType()) {
            case constant:
                node.getElementName().accept(this);
                node.getDataType().accept(this);
                node.getDefaultValue().accept(this);
                break;
            case variable:
                node.getElementName().accept(this);
                node.getDataType().accept(this);
                if (node.getDefaultValue() != null) {
                    node.getDefaultValue().accept(this);
                }
                break;
            case exception:
                node.getElementName().accept(this);
                break;
            case subtype:
                node.getElementName().accept(this);
                node.getDataType().accept(this);
                break;
            default:
                break;
        }
    }

    public void preVisit(TRaiseStmt node) {
        toAccpect(node.getExceptionName());
    }

    public void preVisit(TReturnStmt node) {
        toAccpect(node.getExpression());
    }

    public void preVisit(TMssqlReturn node) {
        toAccpect(node.getReturnExpr());
    }

    public void preVisit(TPlsqlRecordTypeDefStmt stmt) {
        stmt.getTypeName().accept(this);

        for (int i = 0; i < stmt.getFieldDeclarations().size(); i++) {
            stmt.getFieldDeclarations()
                    .getParameterDeclarationItem(i)
                    .accept(this);
        }
    }

    public void preVisit(TSqlplusCmdStatement stmt) {

    }

    public void preVisit(TCursorDeclStmt stmt) {
        switch (stmt.getKind()) {
            case TCursorDeclStmt.kind_ref_cursor_type_definition:
                stmt.getCursorTypeName().accept(this);
                toAccpect(stmt.getRowtype());
                break;
            case TCursorDeclStmt.kind_cursor_declaration:
                stmt.getCursorName().accept(this);
                if (stmt.getCursorParameterDeclarations() != null) {
                    stmt.getCursorParameterDeclarations().accept(this);
                }
                toAccpect(stmt.getRowtype());
                stmt.getSubquery().accept(this);
                break;
            default:
                break;
        }
    }

    public void preVisit(TLoopStmt stmt) {
        switch (stmt.getKind()) {
            case TLoopStmt.basic_loop:
                break;
            case TLoopStmt.cursor_for_loop:
                break;
            case TLoopStmt.for_loop:
                break;
            case TLoopStmt.while_loop:
                break;
        }

        if (stmt.getRecordName() != null) {
            stmt.getRecordName().accept(this);

            if (stmt.getSubquery() != null) {
                stmt.getSubquery().accept(this);
            } else if (stmt.getCursorName() != null) {
                stmt.getCursorName().accept(this);
                if (stmt.getCursorParameterNames() != null) {
                    stmt.getCursorParameterNames().accept(this);
                }
            }
        }
        toAccpect(stmt.getCondition());
        toAccpect(stmt.getBodyStatements());
    }

    public void preVisit(TPlsqlContinue stmt) {
        toAccpect(stmt.getLabelName());
        toAccpect(stmt.getCondition());
    }

    public void preVisit(TPlsqlExecImmeStmt stmt) {
        toAccpect(stmt.getDynamicStringExpr());
        toAccpect(stmt.getDynamicStatements());
    }

    public void preVisit(TExitStmt stmt) {
        toAccpect(stmt.getExitlabelName());
        toAccpect(stmt.getWhenCondition());
    }

    public void preVisit(TFetchStmt stmt) {
        stmt.getCursorName().accept(this);
        stmt.getVariableNames().accept(this);
    }

    public void preVisit(TPlsqlGotoStmt stmt) {
        stmt.getGotolabelName().accept(this);
    }

    public void preVisit(TPlsqlNullStmt stmt) {
    }

    public void preVisit(TCommentOnSqlStmt stmt) {
        stmt.getObjectName().accept(this);
    }

    public void preVisit(TOpenStmt stmt) {
        stmt.getCursorName().accept(this);
        toAccpect(stmt.getCursorParameterNames());
    }

    public void preVisit(TOpenforStmt stmt) {
        stmt.getCursorVariableName().accept(this);
        toAccpect(stmt.getSubquery());
    }

    public void preVisit(TPlsqlForallStmt stmt) {
        stmt.getIndexName().accept(this);
        toAccpect(stmt.getLower_bound());
        toAccpect(stmt.getUpper_bound());
        toAccpect(stmt.getCollectionName());
        toAccpect(stmt.getCollecitonNameExpr());
        stmt.getStatement().accept(this);
    }

}
