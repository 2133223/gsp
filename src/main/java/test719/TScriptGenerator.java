package test719;


import gudusoft.gsqlparser.nodes.*;
import gudusoft.gsqlparser.scriptWriter.TScriptWriter;


import java.io.StringWriter;

public class TScriptGenerator {
    private TScriptWriter scriptWriter;
    private TScriptGeneratorVisitor scriptGeneratorVisitor;

    public TScriptGenerator() {
        scriptWriter = new TScriptWriter();
        scriptGeneratorVisitor = new TScriptGeneratorVisitor(scriptWriter);
    }


    public String generateScript(TParseTreeNode parseTreeNode) {
        // TScriptWriter scriptWriter = new TScriptWriter();
        // TScriptGeneratorVisitor scriptGeneratorVisitor = new
        // TScriptGeneratorVisitor(scriptWriter);
        scriptWriter.reset();
        parseTreeNode.accept(scriptGeneratorVisitor);
        StringWriter stringWriter = new StringWriter();
        scriptWriter.writeTo(stringWriter);
        return stringWriter.toString();

    }


//	class searchColumnVisitor implements IExpressionVisitor {
//		private TExpressionList _resultList;
//		private String _targetColumn;
//
//		public searchColumnVisitor(String targetColumn, TExpressionList resultList){
//			_resultList = resultList;
//			_targetColumn = targetColumn;
//		}
//
//		public boolean exprVisit(TParseTreeNode pNode,boolean isLeafNode){
//			TExpression expr = (TExpression)pNode;
//			if (expr.getExpressionType() == EExpressionType.simple_object_name_t){
//				if (_targetColumn.contains(".")){
//					if (_targetColumn.equalsIgnoreCase(expr.getObjectOperand().toString())){
//						_resultList.addExpression(expr);
//					}
//				}else{
//					if (_targetColumn.equalsIgnoreCase(expr.getObjectOperand().getColumnNameOnly())){
//						_resultList.addExpression(expr);
//					}
//				}
//			}else if (expr.getExpressionType() == EExpressionType.function_t){
//				if (expr.getFunctionCall().getArgs().size() > 0){
//					TExpressionList list1;
//					for(int i=0;i<expr.getFunctionCall().getArgs().size();i++){
//						list1 = searchColumnInExpr(expr.getFunctionCall().getArgs().getExpression(i),_targetColumn);
//						if (list1.size() > 0){
//							for(int j=0;j<list1.size();j++){
//								_resultList.addExpression(list1.getExpression(j));
//							}
//						}
//					}
//					//expr.getFunctionCall().getArgs().getExpression(0).
//				}
//			}
//			return true;
//		};
//	}

    //private EDbVendor dbVendor;


//	public TExpressionList searchColumnInExpr(TExpression sourceExpr, String columnName){
//		TExpressionList resultList = new TExpressionList();
//		sourceExpr.inOrderTraverse(new searchColumnVisitor(columnName,resultList));
//		return  resultList;
//	}

//	public void removeExpr(TExpression pExpr){
//		if (pExpr.getExpressionType() == EExpressionType.removed_t) return;
//		pExpr.setExpressionType(EExpressionType.removed_t);
//		TExpression parentExpr = pExpr.getParentExpr();
//
//		if (parentExpr == null) return;
//
//		switch (parentExpr.getExpressionType()){
//			case list_t: // (1,2,3) in (column1,column2,column3)
//				// remove column1 will break this expr, so need to remove parent expr of list_t as well.
//				//if (parentExpr.getParentExpr() != null) removeExpr(parentExpr.getParentExpr());
//				removeExpr(parentExpr);
//				break;
//			case pattern_matching_t:
//				removeExpr(parentExpr);
//				break;
//			case unary_plus_t:
//			case unary_minus_t:
//			case unary_prior_t:
//				removeExpr(parentExpr);
//				break;
//			case arithmetic_plus_t:
//			case arithmetic_minus_t:
//			case arithmetic_times_t:
//			case arithmetic_divide_t:
//			case power_t:
//			case range_t:
//			case period_ldiff_t:
//			case period_rdiff_t:
//			case period_p_intersect_t:
//			case period_p_normalize_t:
//			case contains_t:
//				removeExpr(parentExpr);
//				break;
//			case assignment_t:
//				removeExpr(parentExpr);
//				break;
//			case sqlserver_proprietary_column_alias_t:
//				removeExpr(parentExpr);
//				break;
//			case arithmetic_modulo_t:
//			case bitwise_exclusive_or_t:
//			case bitwise_or_t:
//			case bitwise_and_t:
//			case bitwise_xor_t:
//			case exponentiate_t:
//			case scope_resolution_t:
//			case at_time_zone_t:
//			case member_of_t:
//			case arithmetic_exponentiation_t:
//				removeExpr(parentExpr);
//				break;
//			case at_local_t:
//			case day_to_second_t:
//			case year_to_month_t:
//				removeExpr(parentExpr);
//				break;
//			case parenthesis_t:
//				removeExpr(parentExpr);
//				break;
//			case simple_comparison_t:
//				removeExpr(parentExpr);
//				break;
//			case group_comparison_t:
//				removeExpr(parentExpr);
//				break;
//			case in_t:
//				removeExpr(parentExpr);
//				break;
//			case floating_point_t:
//				removeExpr(parentExpr);
//				break;
//			case logical_xor_t:
//			case is_t:
//				removeExpr(parentExpr);
//				break;
//			case logical_not_t:
//				removeExpr(parentExpr);
//				break;
//			case null_t:
//				removeExpr(parentExpr);
//				break;
//			case between_t:
//				removeExpr(parentExpr);
//				break;
//			case is_of_type_t:
//				removeExpr(parentExpr);
//				break;
//			case collate_t: //sql server,postgresql
//				removeExpr(parentExpr);
//				break;
//			case left_join_t:
//			case right_join_t:
//				removeExpr(parentExpr);
//				break;
//			case ref_arrow_t:
//				removeExpr(parentExpr);
//				break;
//			case typecast_t:
//				removeExpr(parentExpr);
//				break;
//			case arrayaccess_t:
//				removeExpr(parentExpr);
//				break;
//			case unary_connect_by_root_t:
//				removeExpr(parentExpr);
//				break;
//			case interval_t:
//				removeExpr(parentExpr);
//				break;
//			case unary_binary_operator_t:
//				removeExpr(parentExpr);
//				break;
//			case left_shift_t:
//			case right_shift_t:
//				removeExpr(parentExpr);
//				break;
//			case array_constructor_t:
//				removeExpr(parentExpr);
//				break;
//			case objectConstruct_t:
//				removeExpr(parentExpr);
//				break;
//			case row_constructor_t:
//				removeExpr(parentExpr);
//				break;
//			case namedParameter_t:
//				removeExpr(parentExpr);
//				break;
//			case positionalParameter_t:
//				removeExpr(parentExpr);
//				break;
//			case collectionArray_t:
//				removeExpr(parentExpr);
//				break;
//			case collectionCondition_t:
//				removeExpr(parentExpr);
//				break;
//			case unary_squareroot_t:
//			case unary_cuberoot_t:
//			case unary_factorialprefix_t:
//			case unary_absolutevalue_t:
//			case unary_bitwise_not_t:
//				removeExpr(parentExpr);
//				break;
//			case unary_factorial_t:
//				removeExpr(parentExpr);
//				break;
//			case bitwise_shift_left_t:
//			case bitwise_shift_right_t:
//				removeExpr(parentExpr);
//				break;
//			case multiset_union_t:
//			case multiset_union_distinct_t:
//			case multiset_intersect_t:
//			case multiset_intersect_distinct_t:
//			case multiset_except_t:
//			case multiset_except_distinct_t:
//				removeExpr(parentExpr);
//				break;
//			case json_get_text:
//			case json_get_text_at_path:
//			case json_get_object:
//			case json_get_object_at_path:
//			case json_left_contain:
//			case json_right_contain:
//			case json_exist:
//			case json_any_exist:
//			case json_all_exist:
//				removeExpr(parentExpr);
//				break;
//			case interpolate_previous_value_t:
//				removeExpr(parentExpr);
//				break;
//			case concatenate_t:
//			case logical_and_t:
//			case logical_or_t:
//				if (pExpr == parentExpr.getLeftOperand()){
//				  copyExpr(parentExpr.getRightOperand(),parentExpr);
//				}else if (pExpr == parentExpr.getRightOperand()){
//					copyExpr(parentExpr.getLeftOperand(),parentExpr);
//				}
//
//				break;
//			default:
//				removeExpr(parentExpr);
//				break;
//
//		}
//
//   }

//	public static void copyExpr(TExpression src,TExpression target){
//		target.setExpressionType(src.getExpressionType());
//		target.setLeftOperand(src.getLeftOperand());
//		target.setRightOperand(src.getRightOperand());
//		target.setObjectOperand(src.getObjectOperand());
//		target.setFunctionCall(src.getFunctionCall());
//		target.setSubQuery(src.getSubQuery());
//		target.setConstantOperand(src.getConstantOperand());
//		target.setExprList(src.getExprList());
//		target.setOperatorToken(src.getOperatorToken());
//		target.setComparisonOperator(src.getComparisonOperator());
//		target.setBetweenOperand(src.getBetweenOperand());
//		target.setCaseExpression(src.getCaseExpression());
//
//		target.setLikeEscapeOperand(src.getLikeEscapeOperand());
//		target.setNotToken(src.getNotToken());
//		target.setQuantifier(src.getQuantifier());
//		target.setQuantifierType(src.getQuantifierType());
//
//	}


//	public TExpression createBinaryExpression( EExpressionType expressionType,
//			TExpression left, TExpression right )
//	{
//		TExpression ret = new TExpression( );
//		ret.setExpressionType( expressionType );
//		ret.setLeftOperand( left );
//		ret.setRightOperand( right );
//		return ret;
//	}
//
//	public TExpression createComparisonPredicate(
//			EComparisonType comparisonType, TExpression left, TExpression right )
//	{
//		TExpression ret = new TExpression( );
//		ret.setExpressionType( EExpressionType.simple_comparison_t );
//		ret.setComparisonType( comparisonType );
//		ret.setLeftOperand( left );
//		ret.setRightOperand( right );
//		return ret;
//	}
//
//	public TExpression createAndPredicate( TExpression left, TExpression right )
//	{
//		TExpression ret = createPredicate( EExpressionType.logical_and_t );
//		ret.setLeftOperand( left );
//		ret.setRightOperand( right );
//		return ret;
//	}
//
//	public TExpression createOrPredicate( TExpression left, TExpression right )
//	{
//		TExpression ret = createPredicate( EExpressionType.logical_or_t );
//		ret.setLeftOperand( left );
//		ret.setRightOperand( right );
//		return ret;
//	}
//
//	public TExpression createSubqueryPredicate( EComparisonType comparisonType,
//			EQuantifierType quantifierType, TExpression left,
//			TExpression subqueryExpr )
//	{
//		TExpression ret;
//		if ( quantifierType == EQuantifierType.none )
//		{
//			ret = createPredicate( EExpressionType.simple_comparison_t );
//		}
//		else
//		{
//			ret = createPredicate( EExpressionType.group_comparison_t );
//		}
//		ret.setComparisonType( comparisonType );
//		ret.setQuantifierType( quantifierType );
//		ret.setLeftOperand( left );
//		ret.setRightOperand( subqueryExpr );
//		return ret;
//	}
//
//	public TExpression existsSubqueryPredicate( TExpression subqueryExpr )
//	{
//		TExpression ret = createPredicate( EExpressionType.exists_t );
//		ret.setSubQuery( subqueryExpr.getSubQuery( ) );
//		return ret;
//	}
//
//	public TExpression createParenthesisExpression( TExpression expression )
//	{
//		TExpression ret = new TExpression( );
//		ret.setExpressionType( EExpressionType.parenthesis_t );
//		ret.setLeftOperand( expression );
//		return ret;
//	}
//
//	private TExpression createPredicate( EExpressionType expressionType )
//	{
//		TExpression ret = new TExpression( );
//		ret.setExpressionType( expressionType );
//		return ret;
//	}
//
//
//	public boolean verifyScript( TSourceTokenList originalTokenList )
//	{
//		return scriptWriter.verifyTokens( originalTokenList, false );
//	}
//
//	public boolean verifyScript( TCustomSqlStatement sqlStatement )
//	{
//		return scriptWriter.verifyTokens( sqlStatement.sourcetokenlist,
//				sqlStatement.sqlstatementtype == ESqlStatementType.sstcreatetable );
//	}


}
