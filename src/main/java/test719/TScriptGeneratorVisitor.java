package test719;

import gudusoft.gsqlparser.*;
import gudusoft.gsqlparser.nodes.*;
import gudusoft.gsqlparser.nodes.mssql.TProcedureOption;
import gudusoft.gsqlparser.nodes.mysql.TGroupConcatParam;
import gudusoft.gsqlparser.nodes.oracle.TInvokerRightsClause;
import gudusoft.gsqlparser.scriptWriter.TScriptWriter;
import gudusoft.gsqlparser.stmt.*;
import gudusoft.gsqlparser.stmt.mssql.*;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateProcedure;

import java.lang.reflect.Method;
import java.util.*;

/**
 * supported statements:
 * <ul>
 * <li>alter table</li>
 * <li>Create table</li>
 * <li>Create view</li>
 * <li>Delete</li>
 * <li>Insert</li>
 * <li>Merge</li>
 * <li>Select</li>
 * <li>Update</li>
 * </ul>
 */

public class TScriptGeneratorVisitor extends TParseTreeVisitor {

    private TScriptWriter scriptWriter;

    // private EDbVendor dbVendor;

    public TScriptGeneratorVisitor(TScriptWriter scriptWriter) {
        this.scriptWriter = scriptWriter;
        // this.dbVendor = dbVendor;
    }

    void acceptToken(TSourceToken st) {
        scriptWriter.addToken(st);
    }

    void acceptSymbol(String symbol) {
        scriptWriter.addSymbol(symbol);
    }

    void acceptSymbol(String symbol, int tokenCode) {
        scriptWriter.addSymbol(symbol, tokenCode);
    }

    void acceptKeyword(String keyword) {
        scriptWriter.addKeyword(keyword);
    }

    void acceptKeyword(String keyword, boolean spaceAround) {
        scriptWriter.addKeyword(keyword, spaceAround);
    }

    void acceptNewline() {
        scriptWriter.addNewline();
    }

    void acceptSemicolon() {
        scriptWriter.addSemicolon();
    }

    void acceptSpace(int count) {
        scriptWriter.addSpace(count);
    }

    void acceptLiteral(String literal) {
        scriptWriter.addLiteral(literal);
    }

    void acceptIdentifier(String identifier) {
        scriptWriter.addIdentifier(identifier);
    }

    void acceptOracleHint(String identifier) {
        scriptWriter.acceptOracleHint(identifier);
    }

    public void preVisit(TConstant node) {
        switch (node.getLiteralType()) {
            case etNumber:
                acceptToken(node.getValueToken());
                break;
            case etFloat:
                acceptToken(node.getValueToken());
                break;
            case etString:
                acceptToken(node.getValueToken());
                break;
            case etTimestamp:
                acceptKeyword("timestamp");
                acceptToken(node.getValueToken());
                break;
            case etDate:
                acceptKeyword("date");
                acceptToken(node.getValueToken());
                break;
            case etInterval:
                acceptKeyword("interval");
                acceptToken(node.getValueToken());
                switch (node.getIntervalType()) {
                    case itYear:
                        acceptKeyword("year");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        break;
                    case itYearToYear:
                        acceptKeyword("year");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("year");
                        break;
                    case itYearToMonth:
                        acceptKeyword("year");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("month");
                        break;
                    case itMonth:
                        acceptKeyword("month");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        break;
                    case itMonthToMonth:
                        acceptKeyword("month");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("month");
                        break;
                    case itDay:
                        acceptKeyword("day");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        break;
                    case itDayToDay:
                        acceptKeyword("day");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("day");
                        break;
                    case itDayToHour:
                        acceptKeyword("day");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("hour");
                        break;
                    case itDayToMinute:
                        acceptKeyword("day");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("minute");
                        break;
                    case itDayToSecond:
                        acceptKeyword("day");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("second");
                        if (node.getFractionalSecondsPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getFractionalSecondsPrecision());
                            acceptSymbol(")");
                        }
                        break;
                    case itHour:
                        acceptKeyword("hour");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        break;
                    case itHourToHour:
                        acceptKeyword("hour");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("hour");
                        break;
                    case itHourToMinute:
                        acceptKeyword("hour");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("minute");
                        break;
                    case itHourToSecond:
                        acceptKeyword("hour");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("second");
                        if (node.getFractionalSecondsPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getFractionalSecondsPrecision());
                            acceptSymbol(")");
                        }
                        break;
                    case itMinute:
                        acceptKeyword("minute");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        break;
                    case itMinuteToMinute:
                        acceptKeyword("minute");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("minute");
                        break;
                    case itMinuteToSecond:
                        acceptKeyword("minute");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            acceptSymbol(")");
                        }
                        acceptKeyword("to");
                        acceptKeyword("second");
                        if (node.getFractionalSecondsPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getFractionalSecondsPrecision());
                            acceptSymbol(")");
                        }
                        break;
                    case itSecond:
                        acceptKeyword("second");
                        if (node.getLeadingPrecision() != null) {
                            acceptSymbol("(");
                            acceptToken(node.getLeadingPrecision());
                            if (node.getFractionalSecondsPrecision() != null) {
                                acceptSymbol(",");
                                acceptToken(node.getFractionalSecondsPrecision());
                            }
                            acceptSymbol(")");
                        }
                        break;
                }
                break;
            case etBindVar:
                acceptToken(node.getBind1());
                if (node.getIndicator() != null) {
                    acceptToken(node.getIndicator());
                }
                acceptToken(node.getBind2());
                break;
            default:
                if (node.getStartToken() != null) {
                    visitNodeByToken(node);
                } else {
                    if (node.getValueToken() != null) {
                        acceptToken(node.getValueToken());
                    } else if (node.getStringValue() != null) {
                        acceptLiteral(node.getStringValue());
                    }
                }

                break;
        }
    }

    public void preVisit(TObjectName node) {
        // acceptIdentifier(node.toString());
        if (node.getServerToken() != null) {
            acceptIdentifier(node.getServerToken().toString());
            acceptSymbol(".");
        }
        if (node.getDatabaseToken() != null) {
            acceptIdentifier(node.getDatabaseToken().toString());
            acceptSymbol(".");
        }
        if (node.getSchemaToken() != null) {
            acceptIdentifier(node.getSchemaToken().toString());
            acceptSymbol(".");
        }
        if (node.getObjectToken() != null) {
            acceptIdentifier(node.getObjectToken().toString());
        }
        if (node.getPartToken() != null) {
            if (node.getObjectToken() != null) {
                acceptSymbol(".");
            }
            acceptIdentifier(node.getPartToken().toString());
        }

        if (node.getDblink() != null) {
            acceptSymbol("@");
            node.getDblink().accept(this);
        } else if (node.getExclamationmark() != null) {
            acceptSymbol("@");
            acceptSymbol("!");
        }

    }

    public void preVisit(TExpressionList node) {
        for (int i = 0; i < node.size(); i++) {
            node.getExpression(i).accept(this);
            if (i != (node.size() - 1)) {
                acceptSymbol(",");
            }
        }
    }

    public void preVisit(TFunctionCall node) {
        node.getFunctionName().accept(this);
        acceptSymbol("(");
        switch (node.getAggregateType()) {
            case distinct:
                acceptKeyword("distinct");
                break;
            case all:
                acceptKeyword("all");
                break;
            case unique:
                acceptKeyword("unique");
                break;
        }
        switch (node.getFunctionType()) {
            case unknown_t:
                if (node.getArgs() != null) {
                    node.getArgs().accept(this);
                }
                break;
            case udf_t:
            case case_n_t:
            case chr_t:
                if (node.getArgs() != null) {
                    node.getArgs().accept(this);
                }
                if (node.getWindowDef() != null) {
                    node.getWindowDef().accept(this);
                }
                break;
            case cast_t:
                node.getExpr1().accept(this);
                acceptKeyword("as");
                node.getTypename().accept(this);
                break;
            case convert_t:
                if (node.getTypename() != null) {
                    node.getTypename().accept(this);
                } else {
                    // convert in MySQL have no datatype argument
                }
                if (node.getParameter() != null) {
                    acceptSymbol(",");
                    node.getParameter().accept(this);
                }
                if (node.getStyle() != null) {
                    acceptSymbol(",");
                    node.getStyle().accept(this);
                }

                break;
            case trim_t:
                if (node.getTrimArgument() != null) {
                    // node.getTrimArgument().accept(this);
                    TTrimArgument trimArgument = node.getTrimArgument();
                    if (trimArgument.isBoth())
                        acceptKeyword("both");
                    if (trimArgument.isTrailing())
                        acceptKeyword("trailing");
                    if (trimArgument.isLeading())
                        acceptKeyword("leading");
                    if (trimArgument.getTrimCharacter() != null) {
                        trimArgument.getTrimCharacter().accept(this);
                        acceptKeyword("from");
                    }
                    trimArgument.getStringExpression().accept(this);
                }

                break;
            case extract_t:
                if (node.getArgs() != null) { // extract xml
                    node.getArgs().accept(this);
                } else {
                    acceptKeyword(node.getExtract_time_token().toString());
                    acceptKeyword("from");
                    // node.getExtract_time_token().toString()

                    if (node.getExpr1() != null) {
                        node.getExpr1().accept(this);
                    }
                }

                break;
            case treat_t:
                node.getExpr1().accept(this);
                node.getTypename().accept(this);
                break;
            case contains_t:
                node.getExpr1().accept(this);
                acceptSymbol(",");
                node.getExpr2().accept(this);
                break;
            case freetext_t:
                node.getExpr1().accept(this);
                acceptSymbol(",");
                node.getExpr2().accept(this);
                break;
            case group_concat_t:
                node.getGroupConcatParam().accept(this);
                break;
            case timestampadd_t:
            case timestampdiff_t:
                acceptKeyword(node.getIntervalUnit());
                acceptSymbol(",");
                node.getExpr1().accept(this);
                acceptSymbol(",");
                node.getExpr2().accept(this);
                break;
            case range_n_t:
            case position_t:
            case substring_t:
            case xmlquery_t:
            case xmlcast_t:
            case match_against_t:
            case adddate_t:
            case date_add_t:
            case subdate_t:
            case date_sub_t:
                // node.toString();
                //break;
            default:
                if (node.getExpr1() != null) {
                    node.getExpr1().accept(this);
                }
                if (node.getExpr2() != null) {
                    acceptSymbol(",");
                    node.getExpr2().accept(this);
                }
                if (node.getArgs() != null) {
                    node.getArgs().accept(this);
                }
                break;
        }
        acceptSymbol(")");
        if (node.getWindowDef() != null) {
            node.getWindowDef().accept(this);
        }
    }

    public void preVisit(TGroupConcatParam node) {
        if (node.isDistinct()) {
            acceptKeyword("distinct");
        }
        node.getExprList().accept(this);
        if (node.getOrderBy() != null) {
            node.getOrderBy().accept(this);
        }

        if (node.getSeparatorToken() != null) {
            acceptKeyword("separator");
            acceptToken(node.getSeparatorToken());
        }

    }

    public void preVisit(TWindowDef node) {
        if (node.getKeepDenseRankClause() != null) {
            node.getKeepDenseRankClause().accept(this);
        }
        if (node.getWithinGroup() != null) {
            acceptKeyword("within");
            acceptKeyword("group");
            acceptSymbol("(");
            if (node.getWithinGroup().getOrderBy() != null) {
                node.getWithinGroup().getOrderBy().accept(this);
            }
            acceptSymbol(")");
        }
        if (node.isIncludingOverClause()) {
            acceptKeyword("over");
            acceptSymbol("(");
            if (node.getPartitionClause() != null) {
                acceptKeyword("partition");
                acceptKeyword("by");
                visitExprList(node.getPartitionClause().getExpressionList());
            }
            if (node.getOrderBy() != null) {
                node.getOrderBy().accept(this);
            }
            if (node.getWindowFrame() != null) {
                acceptNewline();
                node.getWindowFrame().accept(this);
            }
            acceptSymbol(")");
        }

        if (node.getName() != null) {
            preVisit(node.getName());
        }
    }

    public void preVisit(TWindowFrame node) {
        if (node.getLimitRowType() == ELimitRowType.Rows) {
            acceptKeyword("rows");
        } else if (node.getLimitRowType() == ELimitRowType.Range) {
            acceptKeyword("range");
        }

        if (node.getStartBoundary() != null && node.getEndBoundary() != null) {
            acceptKeyword("between");
            node.getStartBoundary().accept(this);
            acceptKeyword("and");
            node.getEndBoundary().accept(this);
        } else if (node.getStartBoundary() != null) {
            node.getStartBoundary().accept(this);
        }
    }

    public void preVisit(TWindowFrameBoundary node) {
        if (node.getBoundaryNumber() != null) {
            node.getBoundaryNumber().accept(this);
        }

        if (node.getExclusionClause() != null) {
            if (node.getExclusionClause().getExcludeType() == EWindowExcludeType.currentRow) {
                acceptKeyword("current");
                acceptKeyword("row");
            }
        }

        if (node.getBoundaryType() == EBoundaryType.ebtCurrentRow) {
            acceptKeyword("current");
            acceptKeyword("row");
        } else if (node.getBoundaryType() == EBoundaryType.ebtFollowing) {
            acceptKeyword("following");
        } else if (node.getBoundaryType() == EBoundaryType.ebtPreceding) {
            acceptKeyword("preceding");
        } else if (node.getBoundaryType() == EBoundaryType.ebtUnboundedFollowing) {
            acceptKeyword("unbounded");
            acceptKeyword("following");
        } else if (node.getBoundaryType() == EBoundaryType.ebtUnboundedPreceding) {
            acceptKeyword("unbounded");
            acceptKeyword("preceding");
        }
    }

    public void preVisit(TExpression node) {

        switch (node.getExpressionType()) {
            case simple_object_name_t:
                node.getObjectOperand().accept(this);
                if (node.isOracleOuterJoin()) {
                    // acceptSymbol("(");
                    // acceptSymbol("+");
                    // acceptSymbol(")");
                    acceptSymbol("(+)", TBaseType.outer_join);
                }
                break;
            case simple_constant_t:
                node.getConstantOperand().accept(this);
                break;
            case new_structured_type_t:
            case function_t:
                node.getFunctionCall().accept(this);
                break;
            case type_constructor_t:
                acceptKeyword("new");
                node.getFunctionCall().accept(this);
                break;
            case cursor_t:
                acceptKeyword("cursor");
                node.getSubQuery().accept(this);
                break;
            case multiset_t:
            case subquery_t:
                node.getSubQuery().accept(this);
                break;
            case exists_t:
                acceptKeyword("exists");
                node.getSubQuery().accept(this);
                break;
            case assignment_t:
                node.getLeftOperand().accept(this);
                if ((node.getOperatorToken() != null) && (node.getOperatorToken().toString().equals(":="))) {
                    acceptSymbol(":=");
                } else {
                    acceptSymbol("=");
                }
                node.getRightOperand().accept(this);
                break;
            case simple_comparison_t:
                if (node.getSubQuery() != null) {
                    node.getExprList().accept(this);
                    scriptWriter.addComparisonOperator(node.getComparisonType());
                    node.getSubQuery().accept(this);
                } else {
                    node.getLeftOperand().accept(this);
                    scriptWriter.addComparisonOperator(node.getComparisonType());
                    node.getRightOperand().accept(this);
                }
                break;
            case group_comparison_t:
                if (node.getExprList() != null) {
                    node.getExprList().accept(this);
                } else {
                    node.getLeftOperand().accept(this);
                }
                scriptWriter.addComparisonOperator(node.getComparisonType());
                if (node.getQuantifierType() != EQuantifierType.none) {
                    switch (node.getQuantifierType()) {
                        case all:
                            acceptKeyword("all");
                            break;
                        case some:
                            acceptKeyword("some");
                            break;
                        case any:
                            acceptKeyword("any");
                            break;
                    }

                }
                node.getRightOperand().accept(this);
                break;
            case in_t:
                if (node.getExprList() != null) {
                    acceptSymbol("(");
                    visitExprList(node.getExprList());
                    acceptSymbol(")");
                } else {
                    node.getLeftOperand().accept(this);
                }

                if (node.isNotOperator())
                    acceptKeyword("not");
                acceptKeyword("in");

                node.getRightOperand().accept(this);
                break;
            case collection_constructor_list_t:
            case collection_constructor_multiset_t:
            case collection_constructor_set_t:
            case list_t:
                if (node.getExprList() != null) {
                    acceptSymbol("(");
                    visitExprList(node.getExprList());
                    acceptSymbol(")");
                }
                break;
            case pattern_matching_t:
                node.getLeftOperand().accept(this);
                if (node.isNotOperator())
                    acceptKeyword("not");
                if (node.getOperatorToken() != null) {
                    acceptSpace(1);
                    acceptToken(node.getOperatorToken());
                    acceptSpace(1);
                } else {
                    acceptKeyword("like");
                }

                node.getRightOperand().accept(this);
                if (node.getLikeEscapeOperand() != null) {
                    acceptKeyword("escape");
                    node.getLikeEscapeOperand().accept(this);
                }
                break;
            case between_t:
                node.getBetweenOperand().accept(this);
                if (node.isNotOperator())
                    acceptKeyword("not");
                acceptKeyword("between");
                node.getLeftOperand().accept(this);
                acceptKeyword("and");
                node.getRightOperand().accept(this);
                break;
            case logical_not_t:
                acceptKeyword("not");
                node.getRightOperand().accept(this);
                break;
            case null_t:
                node.getLeftOperand().accept(this);
                acceptKeyword("is");
                if (node.isNotOperator()) {
                    acceptKeyword("not");
                }
                acceptKeyword("null");
                break;
            case parenthesis_t:
                acceptSymbol("(");
                node.getLeftOperand().accept(this);
                acceptSymbol(")");
                break;
            case at_local_t:
                node.getLeftOperand().accept(this);
                acceptKeyword("at");
                acceptKeyword("local");
                break;
            case day_to_second_t:
                node.getLeftOperand().accept(this);
                acceptKeyword("day");
                if (node.getLeadingPrecision() != null) {
                    acceptSymbol("(");
                    acceptToken(node.getLeadingPrecision());
                    acceptSymbol(")");
                }
                acceptKeyword("to");
                acceptKeyword("second");
                if (node.getFractionalSecondsPrecision() != null) {
                    acceptSymbol("(");
                    acceptToken(node.getFractionalSecondsPrecision());
                    acceptSymbol(")");
                }
                break;
            case year_to_month_t:
                node.getLeftOperand().accept(this);
                acceptKeyword("year");
                if (node.getLeadingPrecision() != null) {
                    acceptSymbol("(");
                    acceptToken(node.getLeadingPrecision());
                    acceptSymbol(")");
                }
                acceptKeyword("to");
                acceptKeyword("month");
                break;
            case floating_point_t:
            case typecast_t:
            case unary_factorial_t:
                node.getLeftOperand().accept(this);
                break;
            case is_of_type_t:
                visitNodeByToken(node);
                // node.getLeftOperand().accept(this);
                // acceptKeyword("is");
                // if (node.isNotOperator()) acceptKeyword("not");
                // acceptKeyword("of");
                break;
            case unary_plus_t:
            case unary_minus_t:
            case unary_prior_t:
            case unary_connect_by_root_t:
            case unary_binary_operator_t:
            case unary_squareroot_t:
            case unary_cuberoot_t:
            case unary_factorialprefix_t:
            case unary_absolutevalue_t:
            case unary_bitwise_not_t:
                scriptWriter.addUnaryOperator(node.getExpressionType());
                node.getRightOperand().accept(this);
                break;
            case arithmetic_plus_t:
            case arithmetic_minus_t:
            case arithmetic_times_t:
            case arithmetic_divide_t:
            case power_t:
            case range_t:
            case concatenate_t:
            case period_ldiff_t:
            case period_rdiff_t:
            case period_p_intersect_t:
            case period_p_normalize_t:
            case contains_t:
            case arithmetic_modulo_t:
            case bitwise_exclusive_or_t:
            case bitwise_or_t:
            case bitwise_and_t:
            case bitwise_xor_t:
            case exponentiate_t:
            case scope_resolution_t:
            case member_of_t:
            case logical_and_t:
            case logical_or_t:
            case logical_xor_t:
            case is_t:
            case collate_t:
            case left_join_t:
            case right_join_t:
            case ref_arrow_t:
            case left_shift_t:
            case right_shift_t:
            case bitwise_shift_left_t:
            case bitwise_shift_right_t:
            case multiset_union_t:
            case multiset_union_distinct_t:
            case multiset_intersect_t:
            case multiset_intersect_distinct_t:
            case multiset_except_t:
            case multiset_except_distinct_t:
            case json_get_text:
            case json_get_text_at_path:
            case json_get_object:
            case json_get_object_at_path:
            case json_left_contain:
            case json_right_contain:
            case json_exist:
            case json_any_exist:
            case json_all_exist:
            case sqlserver_proprietary_column_alias_t:
            case submultiset_t:
                node.getLeftOperand().accept(this);
                scriptWriter.addBinaryOperator(node.getExpressionType());
                node.getRightOperand().accept(this);
                break;
            case at_time_zone_t:
                node.getLeftOperand().accept(this);
                acceptKeyword("at");
                acceptKeyword("time");
                acceptKeyword("zone");
                node.getRightOperand().accept(this);
                break;
            case row_constructor_t:

                if (node.getExprList() != null) {
                    node.getExprList().accept(this);
                }
                break;
            case array_constructor_t:
                if (node.getSubQuery() != null) {
                    node.getSubQuery().accept(this);
                }

                if (node.getExprList() != null) {
                    node.getExprList().accept(this);
                }
                break;
            case case_t:
                node.getCaseExpression().accept(this);
                break;
            case arrayaccess_t:
                break;
            case interval_t:
                break;
            case simple_source_token_t:
                acceptIdentifier(node.getSourcetokenOperand().toString());
                break;
            case place_holder_t:
                acceptLiteral(node.toString());
                break;
            default:
                break;
        }

    }

    public void preVisit(TSelectModifier node) {
        switch (node.getSelectModifier()) {
            case HIGH_PRIORITY:
                acceptKeyword("HIGH_PRIORITY");
                break;
            case STRAIGHT_JOIN:
                acceptKeyword("STRAIGHT_JOIN");
                break;
            case SQL_SMALL_RESULT:
                acceptKeyword("SQL_SMALL_RESULT");
                break;
            case SQL_BIG_RESULT:
                acceptKeyword("SQL_BIG_RESULT");
                break;
            case SQL_BUFFER_RESULT:
                acceptKeyword("SQL_BUFFER_RESULT");
                break;
            case SQL_NO_CACHE:
                acceptKeyword("SQL_NO_CACHE");
                break;
            case SQL_CALC_FOUND_ROWS:
                acceptKeyword("SQL_CALC_FOUND_ROWS");
                break;
            case SQL_CACHE:
                acceptKeyword("SQL_CACHE");
                break;
            case ALL:
                acceptKeyword("ALL");
                break;
            case DISTINCT:
                acceptKeyword("DISTINCT");
                break;
            case DISTINCTROW:
                acceptKeyword("DISTINCTROW");
                break;
            default:
                break;
        }
    }

    public void preVisit(TSelectSqlStatement node) {

        for (int i = 0; i < node.getParenthesisCount(); i++) {
            acceptSymbol("(");
        }

        for (int i = 0; i < node.getParenthesisCountBeforeOrder(); i++) {
            acceptSymbol("(");
        }

        if (node.getCteList() != null) {
            acceptKeyword("with");
            visitCTEList(node.getCteList());
        }

        if (node.isCombinedQuery()) {

            acceptNewline();
            node.getLeftStmt().accept(this);
            acceptNewline();
            acceptKeyword(node.getSetOperatorType().toString());
            if (node.isAll())
                acceptKeyword("all");
            acceptNewline();
            node.getRightStmt().accept(this);
            for (int i = 0; i < node.getParenthesisCountBeforeOrder(); i++) {
                acceptSymbol(")");
            }

            if (node.getOrderbyClause() != null) {
                acceptNewline();
                node.getOrderbyClause().accept(this);
            }

            if (node.getLimitClause() != null) {
                node.getLimitClause().accept(this);
            }

            if (node.getForUpdateClause() != null) {
                node.getForUpdateClause().accept(this);
            }

            if (node.getComputeClause() != null) {
                node.getComputeClause().accept(this);
            }

            for (int i = 0; i < node.getParenthesisCount(); i++) {
                acceptSymbol(")");
            }

            return;
        }

        if (node.getValueClause() != null) {
            // DB2 values constructor
            return;
        }

        acceptKeyword("select");
        if (node.getSelectDistinct() != null) {
            switch (node.getSelectDistinct().getUniqueRowFilter()) {
                case urfDistinct:
                    acceptKeyword("distinct");
                    break;
                case urfAll:
                    acceptKeyword("all");
                    break;
                case urfUnique:
                    acceptKeyword("unique");
                    break;
                case urfDistinctOn:
                    acceptKeyword("distinct");
                    acceptKeyword("on");
                    break;
            }

        }

        if ((node.getOracleHint() != null)) {
            if (node.getOracleHint().length() > 0)
                acceptOracleHint(node.getOracleHint());
        }

        if (node.getTopClause() != null) {
            node.getTopClause().accept(this);
        }

        if (node.getSelectModifiers() != null) {
            for (int i = 0; i < node.getSelectModifiers().size(); i++) {
                node.getSelectModifiers().get(i).accept(this);
            }
        }

        if (node.getResultColumnList() != null) {
            acceptNewline();
            visitResultColumnList(node.getResultColumnList());
        } else {
            // hive transform clause with no select list
        }

        if (node.getIntoClause() != null) {
            acceptNewline();
            node.getIntoClause().accept(this);
        }

        if (node.joins.size() > 0) {
            acceptNewline();
            acceptKeyword("from");
            visitJoinList(node.joins);
        }

        if (node.getWhereClause() != null) {
            acceptNewline();
            node.getWhereClause().accept(this);
        }

        if (node.getHierarchicalClause() != null) {
            acceptNewline();
            node.getHierarchicalClause().accept(this);
        }

        if (node.getGroupByClause() != null) {
            acceptNewline();
            node.getGroupByClause().accept(this);
        }

        if (node.getQualifyClause() != null) {
            node.getQualifyClause().accept(this);
        }

        for (int i = 0; i < node.getParenthesisCountBeforeOrder(); i++) {
            acceptSymbol(")");
        }

        if (node.getOrderbyClause() != null) {
            acceptNewline();
            node.getOrderbyClause().accept(this);
        }

        if (node.getLimitClause() != null) {
            node.getLimitClause().accept(this);
        }

        if (node.getForUpdateClause() != null) {
            node.getForUpdateClause().accept(this);
        }

        if (node.getComputeClause() != null) {
            node.getComputeClause().accept(this);
        }

        for (int i = 0; i < node.getParenthesisCount(); i++) {
            acceptSymbol(")");
        }

    }

    public void preVisit(TComputeClause computeClause) {
        for (int i = 0; i < computeClause.getItems().size(); i++) {
            computeClause.getItems().getElement(i).accept(this);
        }
    }

    public void preVisit(TComputeExpr computeExpr) {
        switch (computeExpr.getComputeFunctionType()) {
            case cftNone:
                break;
            case cftCount:
                acceptKeyword("count");
                break;
            case cftMax:
                acceptKeyword("max");
                break;
            case cftMin:
                acceptKeyword("min");
                break;
            case cftSum:
                acceptKeyword("sum");
                break;
            case cftAvg:
                acceptKeyword("avg");
                break;
            case cftStdev:
                acceptKeyword("stdev");
                break;
            case cftStdevp:
                acceptKeyword("stdevp");
                break;
            case cftVar:
                acceptKeyword("var");
                break;
            case cftVarp:
                acceptKeyword("varp");
                break;
        }
        acceptSymbol("(");
        computeExpr.getExpr().accept(this);
        acceptSymbol(")");

    }

    public void preVisit(TComputeClauseItem computeClauseItem) {
        acceptNewline();
        acceptKeyword("compute");
        if (computeClauseItem.getComputerExprList() != null) {
            for (int i = 0; i < computeClauseItem.getComputerExprList()
                    .size(); i++) {
                computeClauseItem.getComputerExprList()
                        .getElement(i)
                        .accept(this);
                if (i != computeClauseItem.getComputerExprList().size() - 1) {
                    acceptSymbol(",");
                }
            }
        }

        if (computeClauseItem.getByExprlist() != null) {
            acceptKeyword("by");
            for (int i = 0; i < computeClauseItem.getByExprlist().size(); i++) {
                computeClauseItem.getByExprlist()
                        .getExpression(i)
                        .accept(this);
                if (i != computeClauseItem.getByExprlist().size() - 1) {
                    acceptSymbol(",");
                }
            }
        }
    }

    public void preVisit(TCreateViewSqlStatement stmt) {
        if (stmt.getCteList() != null) {
            acceptKeyword("with");
            visitCTEList(stmt.getCteList());
        }

        acceptKeyword("create");

        if (stmt.getStReplace() != null) {
            acceptKeyword("or");
            acceptKeyword("replace");
        }

        if (stmt.getStForce() != null) {
            if (stmt.isForce()) {
                acceptKeyword("force");
            }
            if (stmt.isNoForce()) {
                acceptKeyword("noforce");
            }
        }

        acceptKeyword("view");
        stmt.getViewName().accept(this);
        if (stmt.getViewAliasClause() != null) {
            stmt.getViewAliasClause().accept(this);
        }
        acceptNewline();
        acceptKeyword("as");
        acceptNewline();

        if (stmt.getSubquery() != null) {
            stmt.getSubquery().accept(this);
        }

        if (stmt.getRestrictionClause() != null) {
            acceptNewline();
            preVisit(stmt.getRestrictionClause());
        }
    }

    public void preVisit(TRestrictionClause clause) {
        if (clause.getRestrictionType() == ERestrictionType.withCheckOption) {
            acceptKeyword("with");
            acceptKeyword("check");
            acceptKeyword("option");
        } else if (clause.getRestrictionType() == ERestrictionType.withReadOnly) {
            acceptKeyword("with");
            acceptKeyword("read");
            acceptKeyword("only");
        } else if (clause.getRestrictionType() == ERestrictionType.withLocalCheckOption) {
            acceptKeyword("with");
            acceptKeyword("local");
            acceptKeyword("check");
            acceptKeyword("option");
        } else if (clause.getRestrictionType() == ERestrictionType.withCascadedCheckOption) {
            acceptKeyword("with");
            acceptKeyword("cascaded");
            acceptKeyword("check");
            acceptKeyword("option");
        }
        if (clause.getConstraintName() != null) {
            clause.getConstraintName().accept(this);
        }
    }

    public void preVisit(TViewAliasClause clause) {
        acceptSymbol("(");
        visitViewAliasItemList(clause.getViewAliasItemList());
        acceptSymbol(")");
    }

    void visitViewAliasItemList(TViewAliasItemList viewAliasItemList) {
        for (int i = 0; i < viewAliasItemList.size(); i++) {
            TViewAliasItem aliasItem = viewAliasItemList.getViewAliasItem(i);
            visitViewAliasItem(aliasItem);
            if (i != viewAliasItemList.size() - 1)
                acceptSymbol(",");
        }
    }

    private void visitViewAliasItem(TViewAliasItem aliasItem) {
        aliasItem.getAlias().accept(this);
    }

    public void preVisit(TResultColumn node) {
        node.getExpr().accept(this);
        if ((node.getAliasClause() != null) && (node.getExpr().getExpressionType() != EExpressionType.sqlserver_proprietary_column_alias_t)) {
            acceptSpace(1);
            node.getAliasClause().accept(this);
        }
    }

    public void preVisit(TColumnDefinitionList node) {

        for (int i = 0; i < node.size(); i++) {
            node.getColumn(i).accept(this);
            if (i != node.size() - 1) {
                acceptSymbol(",");
            }
        }
    }

    public void preVisit(TObjectNameList node) {

        for (int i = 0; i < node.size(); i++) {
            node.getObjectName(i).accept(this);
            if (i != node.size() - 1) {
                acceptSymbol(",");
            }
        }
    }

    public void preVisit(TConstraintList node) {

        for (int i = 0; i < node.size(); i++) {
            node.getConstraint(i).accept(this);
            if (i != node.size() - 1) {
                acceptSymbol(",");
            }
        }
    }

    public void preVisit(TParameterDeclarationList params) {
        for (int i = 0; i < params.size(); i++) {
            TParameterDeclaration param = params.getParameterDeclarationItem(i);
            param.accept(this);
            if (i != params.size() - 1) {
                acceptSymbol(",");
            }
        }
    }

    public void preVisit(TParameterDeclaration param) {
        if (param.getParameterName() != null) {
            param.getParameterName().accept(this);
        }
        if (param.getDataType() != null) {
            param.getDataType().accept(this);
        }
    }

    public void preVisit(TMssqlCreateFunction function) {
        acceptKeyword("create");
        acceptKeyword("function");

        if (function.getFunctionName() != null) {
            function.getFunctionName().accept(this);
        }

        if (function.getParameterDeclarations() != null
                && function.getParameterDeclarations().size() > 0) {
            acceptSymbol("(");
            function.getParameterDeclarations().accept(this);
            acceptSymbol(")");
        }

        if (function.getReturnDataType() != null) {
            acceptNewline();
            acceptSpace(4);
            acceptKeyword("returns");
            function.getReturnDataType().accept(this);
        }

        acceptNewline();
        acceptKeyword("as");
        acceptNewline();

        if (function.getBodyStatements() != null) {
            function.getBodyStatements().accept(this);
        }
    }


    public void preVisit(TDmlEventItem node) {
        switch (node.getDmlType()) {
            case sstinsert:
                acceptKeyword("insert");
                break;
            case sstdelete:
                acceptKeyword("delete");
                break;
            case sstupdate:
                acceptKeyword("update");
                if (node.getColumnList() != null) {
                    acceptKeyword("of");
                    node.getColumnList().accept(this);
                }
                break;
        }
    }

    public void preVisit(TDdlEventItem node) {
        acceptIdentifier(node.getEventName().toString());
    }

    public void preVisit(TSimpleDmlTriggerClause node) {
        acceptNewline();
        acceptSpace(4);

        acceptKeyword("on");
        TDmlEventClause dmlEventClause = (TDmlEventClause) node.getEventClause();
        dmlEventClause.getTableName().accept(this);

        acceptNewline();
        switch (node.getActionTime()) {
            case tatFor:
                acceptKeyword("for");
                break;
            case tatBefore:
                acceptKeyword("before");
                break;
            case tatAfter:
                acceptKeyword("after");
                break;
            case tatInsteadOf:
                acceptKeyword("instead of");
                break;
        }

        for (int i = 0; i < dmlEventClause.getEventItems().size(); i++) {
            dmlEventClause.getEventItems().get(i).accept(this);
            if (i != dmlEventClause.getEventItems().size() - 1) {
                acceptSymbol(",");
            }
        }


    }

    public void preVisit(TCreateTriggerStmt trigger) {
        if (trigger.isAlterTrigger())
            acceptKeyword("alter");
        else
            acceptKeyword("create");
        acceptKeyword("trigger");

        if (trigger.getTriggerName() != null) {
            trigger.getTriggerName().accept(this);
        }

        trigger.getTriggeringClause().accept(this);

//		if ( trigger.getOnTable( ) != null )
//		{
//			acceptNewline( );
//			acceptSpace( 4 );
//
//			acceptKeyword( "on" );
//			trigger.getOnTable( ).accept( this );
//		}
//
//		acceptNewline( );
//
//		if ( trigger.getTimingPoint( ) == ETriggerTimingPoint.ttpFor )
//			acceptKeyword( "for" );
//		if ( trigger.getTimingPoint( ) == ETriggerTimingPoint.ttpTinsteadOf )
//		{
//			acceptKeyword( "instead" );
//			acceptKeyword( "of" );
//		}
//		if ( trigger.getTimingPoint( ) == ETriggerTimingPoint.ttpAfter )
//			acceptKeyword( "after" );
//		if ( trigger.getTimingPoint( ) == ETriggerTimingPoint.ttpBefore )
//			acceptKeyword( "before" );
//
//		if ( trigger.getDmlTypes( ) != null
//				&& trigger.getDmlTypes( ).size( ) > 0 )
//		{
//			EnumSet<ETriggerDmlType> types = trigger.getDmlTypes( );
//			for ( Iterator<ETriggerDmlType> iterator = types.iterator( ); iterator.hasNext( ); )
//			{
//				ETriggerDmlType eTriggerDmlType = (ETriggerDmlType) iterator.next( );
//				if ( eTriggerDmlType == ETriggerDmlType.tdtInsert )
//				{
//					acceptKeyword( "insert" );
//				}
//				if ( eTriggerDmlType == ETriggerDmlType.tdtUpdate )
//				{
//					acceptKeyword( "update" );
//				}
//				if ( eTriggerDmlType == ETriggerDmlType.tdtDelete )
//				{
//					acceptKeyword( "delete" );
//				}
//				if ( iterator.hasNext( ) )
//				{
//					acceptSymbol( "," );
//				}
//			}
//		}

        acceptNewline();
        acceptKeyword("as");
        acceptNewline();

        if (trigger.getBodyStatements() != null) {
            trigger.getBodyStatements().accept(this);
        }
    }

    public void preVisit(TProcedureOption option) {
        if (option.getOptionType() == EProcedureOptionType.potExecuteAs) {
            acceptKeyword("execute");
            acceptKeyword("as");
            acceptKeyword("caller");
        }
        if (option.getOptionType() == EProcedureOptionType.potCalledOnNullInput) {
            acceptKeyword("called");
            acceptKeyword("on");
            acceptKeyword("null");
            acceptKeyword("input");
        }
        if (option.getOptionType() == EProcedureOptionType.potEncryption) {
            acceptKeyword("encryption");
        }
        if (option.getOptionType() == EProcedureOptionType.potNativeCompilation) {
            acceptKeyword("native");
            acceptKeyword("compilation");
        }
        if (option.getOptionType() == EProcedureOptionType.potRecompile) {
            acceptKeyword("recompile");
        }
        if (option.getOptionType() == EProcedureOptionType.potReturnsNullOnNullInput) {
            acceptKeyword("returns");
            acceptKeyword("null");
            acceptKeyword("on");
            acceptKeyword("null");
            acceptKeyword("input");
        }
        if (option.getOptionType() == EProcedureOptionType.potSchemaBinding) {
            acceptKeyword("schema");
            acceptKeyword("binding");
        }
    }

    public void preVisit(TMssqlCreateProcedure procedure) {
        acceptKeyword("create");
        acceptKeyword("procedure");

        if (procedure.getProcedureName() != null) {
            procedure.getProcedureName().accept(this);
        }

        acceptNewline();
        acceptSpace(4);

        if (procedure.getParameterDeclarations() != null
                && procedure.getDeclareStatements().size() > 0) {
            procedure.getParameterDeclarations().accept(this);
        }

        if (procedure.getProcedureOptions() != null
                && procedure.getProcedureOptions().size() > 0) {
            acceptKeyword("with");

            for (int i = 0; i < procedure.getProcedureOptions().size(); i++) {
                TProcedureOption option = procedure.getProcedureOptions()
                        .getElement(i);
                option.accept(this);
                if (i != procedure.getProcedureOptions().size() - 1) {
                    acceptSymbol(",");
                }
            }
        }

        if (procedure.isForReplication()) {
            acceptKeyword("for");
            acceptKeyword("replication");
        }

        acceptNewline();
        acceptKeyword("as");
        acceptNewline();

        acceptKeyword("begin");
        acceptNewline();

        if (procedure.getBodyStatements() != null) {
            procedure.getBodyStatements().accept(this);
        }

        acceptKeyword("end");
    }

    public void preVisit(TMssqlFetch stmt) {
        acceptKeyword("fetch");
        acceptKeyword("from");
        if (stmt.getCursorName() != null) {
            stmt.getCursorName().accept(this);
        }
        acceptKeyword("into");
        if (stmt.getVariableNames() != null) {
            stmt.getVariableNames().accept(this);
        }
    }

    public void preVisit(TMssqlBlock block) {
        acceptKeyword("begin");
        acceptNewline();
        if (block.getBodyStatements() != null) {
            block.getBodyStatements().accept(this);
            acceptNewline();
        }
        acceptKeyword("end");
    }

    public void preVisit(TMssqlRollback rollback) {
        acceptKeyword("rollback");
        if (rollback.getTrans_or_work() != null) {
            acceptToken(rollback.getTrans_or_work());
        }
        if (rollback.getTransactionName() != null) {
            rollback.getTransactionName().accept(this);
        }
    }

    public void preVisit(TMssqlRaiserror raiseError) {
        acceptKeyword("raiserror");
        acceptSymbol("(");
        if (raiseError.getMessageText() != null) {
            raiseError.getMessageText().accept(this);
        }

        if (raiseError.getSeverity() != null) {
            acceptSymbol(",");
            raiseError.getSeverity().accept(this);
        }

        if (raiseError.getState() != null) {
            acceptSymbol(",");
            raiseError.getState().accept(this);
        }

        if (raiseError.getArgs() != null && raiseError.getArgs().size() > 0) {
            for (int i = 0; i < raiseError.getArgs().size(); i++) {
                acceptSymbol(",");
                raiseError.getArgs().getExpression(i).accept(this);
            }
        }
        acceptSymbol(")");
        ;
    }

    public void preVisit(TMssqlPrint stmt) {
        acceptKeyword("print");
        if (stmt.getMessages() != null) {
            stmt.getMessages().accept(this);
        }
    }

    public void preVisit(TMssqlDeclare stmt) {
        acceptKeyword("declare");
        if (stmt.getVariables() != null) {
            stmt.getVariables().accept(this);
        }

        if (stmt.getCursorName() != null) {
            stmt.getCursorName().accept(this);
            acceptKeyword("cursor");
            if (stmt.getSubquery() != null) {
                acceptKeyword("for");
                stmt.getSubquery().accept(this);
            }
        }
    }

    public void preVisit(TVarDeclStmt stmt) {
        if (stmt.getElementName() != null) {
            stmt.getElementName().accept(this);
        }
        if (stmt.getDataType() != null) {
            stmt.getDataType().accept(this);
        }
        if (stmt.getDefaultValue() != null) {
            if (stmt.getDeclareType() == EDeclareType.variable) {
                acceptKeyword("default");
                stmt.getDefaultValue().accept(this);
            } else if (stmt.getDeclareType() == EDeclareType.constant) {
                acceptSymbol(":=");
                stmt.getDefaultValue().accept(this);
            }
        }
    }

    public void preVisit(TMssqlSet stmt) {
        acceptKeyword("set");
        if (stmt.getVarName() != null) {
            stmt.getVarName().accept(this);
            acceptSymbol("=");
        }
        if (stmt.getVarExpr() != null) {
            stmt.getVarExpr().accept(this);
        }
    }

    public void preVisit(TMssqlReturn stmt) {
        acceptKeyword("return");
        if (stmt.getReturnExpr() != null) {
            stmt.getReturnExpr().accept(this);
        }
    }

    public void preVisit(TMssqlLabel stmt) {
        super.preVisit(stmt);
    }

    public void preVisit(TDeclareVariableList variables) {
        for (int i = 0; i < variables.size(); i++) {
            variables.getDeclareVariable(i).accept(this);
            if (i != variables.size() - 1) {
                acceptSymbol(",");
            }
        }
    }

    public void preVisit(TDeclareVariable variable) {
        if (variable.getVariableName() != null) {
            variable.getVariableName().accept(this);
        }

        if (variable.getDatatype() != null) {
            variable.getDatatype().accept(this);
        }
    }

    public void preVisit(TMssqlExecute stmt) {
        acceptKeyword("exec");
        if (stmt.getModuleName() != null) {
            stmt.getModuleName().accept(this);
        }
        if (stmt.getParameters() != null && stmt.getParameters().size() > 0) {
            acceptSpace(1);
            stmt.getParameters().accept(this);
        }
    }

    public void preVisit(TMssqlExecuteAs stmt) {
        acceptKeyword("execute");
        acceptKeyword("as");
        if (stmt.getExecuteAsOption() != null) {
            switch (stmt.getExecuteAsOption()) {
                case eaoCaller:
                    acceptKeyword("caller");
                    break;
                case eaoLogin:
                    acceptKeyword("login");
                    break;
                case eaoOwner:
                    acceptKeyword("owner");
                    break;
                case eaoSelf:
                    acceptKeyword("self");
                    break;
                case eaoString:
                    acceptKeyword("string");
                    break;
                case eaoUser:
                    acceptKeyword("user");
                    break;
                default:
                    break;
            }
        }
        if (stmt.getLoginName() != null) {
            stmt.getLoginName().accept(this);
            // TScriptGenerator generator = new TScriptGenerator( this.dbVendor
            // );
            // generator.createObjectName( stmt.getUserName( ) ).accept( this );
        }
    }

    public void preVisit(TMssqlRevert stmt) {
        acceptKeyword("revert");
        if (stmt.getCookie() != null) {
            acceptKeyword("with");
            acceptIdentifier("cookie");
            acceptSymbol("=");
            stmt.getCookie().accept(this);
        }
    }

    public void preVisit(TMssqlStmtStub stmt) {
        super.preVisit(stmt);
    }

    public void preVisit(TExecParameter param) {
        if (param.getParameterName() != null) {
            param.getParameterName().accept(this);
        }
        if (param.getParameterValue() != null) {
            param.getParameterValue().accept(this);
        }
    }

    public void preVisit(TExecParameterList params) {
        for (int i = 0; i < params.size(); i++) {
            params.getExecParameter(i).accept(this);
            if (i != params.size() - 1) {
                acceptSymbol(",");
            }
        }
    }

    public void preVisit(TAlterTableOption node) {

        switch (node.getOptionType()) {
            case AddColumn:
                acceptKeyword("add");
                if (node.getColumnDefinitionList().size() > 1)
                    acceptSymbol("(");
                node.getColumnDefinitionList().accept(this);
                if (node.getColumnDefinitionList().size() > 1)
                    acceptSymbol(")");
                break;
            case AlterColumn:
                acceptKeyword("alter");
                acceptKeyword("column");
                node.getColumnName().accept(this);
                break;
            case ChangeColumn:
                acceptKeyword("change");
                node.getColumnName().accept(this);
                break;
            case DropColumn:
                acceptKeyword("drop");
                acceptKeyword("column");
                if (node.getColumnNameList().size() > 1)
                    acceptSymbol("(");
                node.getColumnNameList().accept(this);
                if (node.getColumnNameList().size() > 1)
                    acceptSymbol(")");

                break;
            case ModifyColumn:
                acceptKeyword("modify");
                acceptKeyword("column");
                if (node.getColumnDefinitionList().size() > 1)
                    acceptSymbol("(");
                node.getColumnDefinitionList().accept(this);
                if (node.getColumnDefinitionList().size() > 1)
                    acceptSymbol(")");
                break;
            case RenameColumn:
                acceptKeyword("rename");
                acceptKeyword("column");
                node.getColumnName().accept(this);
                acceptKeyword("to");
                node.getNewColumnName().accept(this);
                break;
            case AddConstraint:
                acceptKeyword("add");
                node.getConstraintList().accept(this);
                break;
            default:
        }

    }

    public void preVisit(TAlterTableStatement stmt) {

        acceptKeyword("alter");
        acceptKeyword("table");
        stmt.getTableName().accept(this);
        acceptNewline();

        if (stmt.getAlterTableOptionList() != null) {
            for (int i = 0; i < stmt.getAlterTableOptionList().size(); i++) {
                stmt.getAlterTableOptionList()
                        .getAlterTableOption(i)
                        .accept(this);
                if (i != stmt.getAlterTableOptionList().size() - 1) {
                    acceptSymbol(",");
                    acceptNewline();
                }
            }
        }

    }

    public void preVisit(TStatementList stmts) {
        for (int i = 0; i < stmts.size(); i++) {
            TCustomSqlStatement stmt = stmts.get(i);
            preVisit(stmt);
            if (stmts.size() > 1)
                acceptSemicolon();

            if (i != stmts.size() - 1) {
                acceptNewline();
            }
        }
    }

    protected void preVisit(TCustomSqlStatement stmt) {
        try {
            Class<?> clazz = stmt.getClass();
            Method m = this.getClass().getMethod("preVisit", clazz);
            m.setAccessible(true);
            m.invoke(this, stmt);
        } catch (Exception e) {
            throw new UnsupportedOperationException(stmt.sqlstatementtype.name(),
                    e);
        }
    }

    public void preVisit(TElsifStmt elsifStmt) {
        acceptKeyword("elsif");
        if (elsifStmt.getCondition() != null) {
            elsifStmt.getCondition().accept(this);
        }
        if (elsifStmt.getThenStatements() != null
                && elsifStmt.getThenStatements().size() > 0) {
            acceptKeyword("then");
            acceptNewline();
            acceptSpace(4);
            preVisit(elsifStmt.getThenStatements());
            acceptNewline();
        }
    }

    public void preVisit(TAssignStmt assignStmt) {
        assignStmt.getLeft().accept(this);
        scriptWriter.addComparisonOperator(EComparisonType.equals);
        assignStmt.getExpression().accept(this);
    }

    public void preVisit(TIfStmt ifStmt) {
        acceptKeyword("if");
        if (ifStmt.getCondition() != null) {
            ifStmt.getCondition().accept(this);
        }
        if (ifStmt.getThenStatements() != null
                && ifStmt.getThenStatements().size() > 0) {
            acceptKeyword("then");
            acceptNewline();
            acceptSpace(4);
            preVisit(ifStmt.getThenStatements());
            acceptNewline();
        }
        if (ifStmt.getElseifStatements() != null) {
            preVisit(ifStmt.getElseifStatements());
        }
        if (ifStmt.getElseStatements() != null
                && ifStmt.getElseStatements().size() > 0) {
            acceptKeyword("else");
            preVisit(ifStmt.getElseStatements());
            acceptNewline();
        }

        acceptKeyword("end");
        acceptKeyword("if");
        acceptNewline();
    }

    public void preVisit(TMssqlIfElse ifElse) {
        acceptKeyword("if");
        if (ifElse.getCondition() != null) {
            ifElse.getCondition().accept(this);
        }
        if (ifElse.getStmt() != null) {
            acceptNewline();
            acceptSpace(4);
            preVisit(ifElse.getStmt());
        }
        if (ifElse.getElseStmt() != null) {
            acceptNewline();
            acceptSpace(4);
            acceptKeyword("else");
            acceptNewline();
            acceptSpace(4);
            preVisit(ifElse.getElseStmt());
        }
    }

    public void preVisit(TAliasClause node) {
        if (node.isHasAs()) {
            acceptKeyword("as");
        }
        node.getAliasName().accept(this);

        if (node.getColumns() != null) {
            acceptSpace(1);
            acceptSymbol("(");
            visitObjectNameList(node.getColumns());
            acceptSymbol(")");
        }
    }


    public void preVisit(TWhereClause node) {
        if (node.getCondition() == null)
            return;
        acceptKeyword("where");
        node.getCondition().accept(this);
    }

    public void preVisit(TIntoClause node) {
        if (node.isBulkCollect()) {
            acceptKeyword("bulk");
            acceptKeyword("collect");
        }
        acceptKeyword("into");
        if (node.getExprList() != null) {
            acceptNewline();
            node.getExprList().accept(this);
        } else {
            node.getIntoName().accept(this);
        }
    }

    public void preVisit(TTopClause node) {
        acceptKeyword("top");
        node.getExpr().accept(this);
        if (node.isPercent())
            acceptKeyword("percent");
        if (node.isWithties()) {
            acceptKeyword("with ties");
        }
    }

    public void preVisit(TLimitClause node) {
        acceptNewline();
        acceptKeyword("limit");

        if (node.getOffset() != null) {
            node.getOffset().accept(this);
            if (node.getRow_count() != null) {
                acceptSymbol(",");
                node.getRow_count().accept(this);
            }
        } else {
            node.getRow_count().accept(this);
        }
    }

    public void preVisit(TJoin node) {

        if (node.getJoinItems().size() > 0) {
            if (node.getTable() != null) {
                node.setKind(TBaseType.join_source_table);
            } else if (node.getJoin() != null) {
                node.setKind(TBaseType.join_source_join);
            }
        }

        if (node.isWithParen()) {
            for (int i = 0; i < node.getNestedParen(); i++) {
                acceptSymbol("(");
            }
        }
        switch (node.getKind()) {
            case TBaseType.join_source_fake:
                node.getTable().accept(this);
                break;
            case TBaseType.join_source_table:
            case TBaseType.join_source_join:

                if (node.getKind() == TBaseType.join_source_table) {
                    node.getTable().accept(this);
                } else if (node.getKind() == TBaseType.join_source_join) {
                    // preVisit(node.getJoin());
                    node.getJoin().accept(this);
                }

                for (int i = 0; i < node.getJoinItems().size(); i++) {
                    TJoinItem joinItem = node.getJoinItems().getJoinItem(i);
                    acceptNewline();
                    joinItem.accept(this);
                }
                break;
        }
        if (node.isWithParen()) {
            for (int i = 0; i < node.getNestedParen(); i++) {
                acceptSymbol(")");
            }
        }

        if (node.getAliasClause() != null) {
            acceptSpace(1);
            node.getAliasClause().accept(this);
        }
    }

    public void preVisit(TJoinItem joinItem) {

        switch (joinItem.getJoinType()) {
            case inner:
                acceptKeyword("inner");
                acceptKeyword("join");
                break;
            case join:
                acceptKeyword("join");
                break;
            case left:
                acceptKeyword("left");
                acceptKeyword("join");
                break;
            case leftouter:
                acceptKeyword("left");
                acceptKeyword("outer");
                acceptKeyword("join");
                break;
            case right:
                acceptKeyword("right");
                acceptKeyword("join");
                break;
            case rightouter:
                acceptKeyword("right");
                acceptKeyword("outer");
                acceptKeyword("join");
                break;
            case full:
                acceptKeyword("full");
                acceptKeyword("join");
                break;
            case fullouter:
                acceptKeyword("full");
                acceptKeyword("outer");
                acceptKeyword("join");
                break;
            case cross:
                acceptKeyword("cross");
                acceptKeyword("join");
                break;
            case natural:
                acceptKeyword("natural");
                acceptKeyword("join");
                break;
            case natural_inner:
                acceptKeyword("natural");
                acceptKeyword("inner");
                acceptKeyword("join");
                break;
            case crossapply:
                acceptKeyword("cross");
                acceptKeyword("apply");
                break;
            case outerapply:
                acceptKeyword("outer");
                acceptKeyword("apply");
                break;
            default:
                acceptKeyword("join");
                break;
        }

        if (joinItem.getTable() != null) {
            joinItem.getTable().accept(this);
        } else if (joinItem.getJoin() != null) {
            joinItem.getJoin().accept(this);
        }

        if (joinItem.getOnCondition() != null) {
            acceptKeyword("on");
            joinItem.getOnCondition().accept(this);
        }

        if (joinItem.getUsingColumns() != null) {
            acceptKeyword("using");
            acceptSymbol("(");
            visitObjectNameList(joinItem.getUsingColumns());
            acceptSymbol(")");
        }

        // already implemented in public void preVisit(TJoin node){

        // if (node.getKind() == TBaseType.join_source_table){
        // node.getTable().accept(this);
        // }else if (node.getKind() == TBaseType.join_source_join){
        // node.getJoin().accept(this);
        // }

        // if (node.getTable() != null){
        // node.getTable().accept(this);
        // }else if (node.getJoin() != null){
        // node.getJoin().accept(this);
        // }
        //
        //
        // if (node.getOnCondition() != null){
        // node.getOnCondition().accept(this);
        // }
        //
        // if (node.getUsingColumns() != null){
        // node.getUsingColumns().accept(this);
        // }
    }

    public void preVisit(TTable node) {

        if (node.getParenthesisCount() > 0) {
            for (int i = 0; i < node.getParenthesisCount(); i++) {
                acceptSymbol("(");
            }
        }
        if (node.getParenthesisAfterAliasCount() > 0) {
            for (int i = 0; i < node.getParenthesisAfterAliasCount(); i++) {
                acceptSymbol("(");
            }
        }

        if (node.isTableKeyword()) {
            acceptKeyword("table");
            acceptSymbol("(");
        }

        if (node.isOnlyKeyword()) {
            acceptKeyword("only");
            acceptSymbol("(");
        }

        switch (node.getTableType()) {
            case objectname: {
                node.getTableName().accept(this);
                if (node.getFlashback() != null) {
                    acceptNewline();
                    visitNodeByToken(node.getFlashback());
                }
                break;
            }
            case tableExpr: {
                node.getTableExpr().accept(this);
                break;
            }
            case subquery: {
                node.getSubquery().accept(this);
                break;
            }
            case function: {
                node.getFuncCall().accept(this);
                break;
            }
            case pivoted_table: {
                node.getPivotedTable().accept(this);
                break;
            }
            case output_merge: {
                // e_table_reference.setTextContent(node.getOutputMerge().toString());
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
                node.getOuterClause().accept(this);
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
                // sb.append(node.toString().replace(">","&#62;").replace("<","&#60;"));
                break;

        }

        if (node.isTableKeyword()) {
            acceptSymbol(")");
        }

        if (node.isOnlyKeyword()) {
            acceptSymbol(")");
        }

        if (node.getParenthesisCount() > 0) {
            for (int i = 0; i < node.getParenthesisCount(); i++) {
                acceptSymbol(")");
            }
        }

        if (node.getPxGranule() != null) {
            // node.getPxGranule().accept(this);
            acceptNewline();
            visitNodeByToken(node.getPxGranule());
        }

        if (node.getTableSample() != null) {
            // node.getTableSample().accept(this);
            acceptNewline();
            visitNodeByToken(node.getTableSample());
        }

        if (node.getAliasClause() != null) {
            acceptSpace(1);
            node.getAliasClause().accept(this);
        }

        if (node.getParenthesisAfterAliasCount() > 0) {
            for (int i = 0; i < node.getParenthesisAfterAliasCount(); i++) {
                acceptSymbol(")");
            }
        }

        if (node.getTableHintList() != null) {
            for (int i = 0; i < node.getTableHintList().size(); i++) {
                TTableHint tableHint = node.getTableHintList().getElement(i);
                tableHint.accept(this);
            }
        }

    }

    public void preVisit(THierarchical node) {
        if (node.getStartWithClause() != null) {
            acceptKeyword("start");
            acceptKeyword("with");
            node.getStartWithClause().accept(this);
        }

        for (int i = 0; i < node.getConnectByList().size(); i++) {
            node.getConnectByList().getElement(i).accept(this);
            if (i != node.getConnectByList().size() - 1) {
                acceptNewline();
            }
        }

    }

    public void preVisit(TConnectByClause node) {
        acceptKeyword("connect");
        acceptKeyword("by");
        if (node.isNoCycle()) {
            acceptKeyword("nocycle");
        }
        node.getCondition().accept(this);
    }

    public void preVisit(TGroupBy node) {
        if (node.getItems() != null && node.getItems().size() > 0) {
            acceptKeyword("group");
            acceptKeyword("by");
            for (int i = 0; i < node.getItems().size(); i++) {
                node.getItems().getElement(i).accept(this);
                if (i != node.getItems().size() - 1)
                    acceptSymbol(",");
            }
        }
        if (node.getHavingClause() != null) {
            acceptKeyword("having");
            node.getHavingClause().accept(this);
        }
    }

    public void preVisit(TGroupByItem node) {

        if (node.getExpr() != null) {
            node.getExpr().accept(this);
        } else if (node.getGroupingSet() != null) {
            node.getGroupingSet().accept(this);
        } else if (node.getRollupCube() != null) {
            node.getRollupCube().accept(this);
        }

    }

    public void preVisit(TOrderBy node) {
        if (node.getItems().size() == 0)
            return;

        acceptKeyword("order");
        if (node.isSiblings())
            acceptKeyword("siblings");
        acceptKeyword("by");
        for (int i = 0; i < node.getItems().size(); i++) {
            node.getItems().getOrderByItem(i).accept(this);
            if (i != node.getItems().size() - 1)
                acceptSymbol(",");
        }

    }

    public void preVisit(TOrderByItem node) {
        if (node.getSortKey() != null) {
            node.getSortKey().accept(this);
        }
        if (node.getSortOrder() == ESortType.asc) {
            acceptKeyword("asc");
        } else if (node.getSortOrder() == ESortType.desc) {
            acceptKeyword("desc");
        }

        if (node.getNullOrder() == ENullOrder.nullsFirst) {
            acceptKeyword("nulls");
            acceptKeyword("first");
        } else if (node.getNullOrder() == ENullOrder.nullsLast) {
            acceptKeyword("nulls");
            acceptKeyword("last");
        }
        // e_order_by_item.setAttribute("sort_order",node.getSortOrder().toString());
    }

    public void preVisit(TCTE node) {
        node.getTableName().accept(this);

        if (node.getColumnList() != null) {
            acceptSymbol("(");
            for (int i = 0; i < node.getColumnList().size(); i++) {
                node.getColumnList().getObjectName(i).accept(this);
                if (i != node.getColumnList().size() - 1)
                    acceptSymbol(",");
            }
            acceptSymbol(")");

        }

        acceptKeyword("as");
        acceptNewline();
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

    public void preVisit(TPivotInClause node) {
        acceptKeyword("in");
        if (node.getItems() != null) {
            acceptSymbol("(");
            visitResultColumnList(node.getItems());
            acceptSymbol(")");
        }
        if (node.getSubQuery() != null)
            node.getSubQuery().accept(this);

    }

    public void preVisit(TPivotedTable node) {
        TPivotClause pivotClause;

        for (int i = 0; i < node.getPivotClauseList().size(); i++) {

            if (i == 0) {
                node.getTableSource().accept(this);
            } else {
            }

            pivotClause = node.getPivotClauseList().getElement(i);
            if (pivotClause.getAliasClause() != null) {
                pivotClause.getAliasClause().accept(this);
            }
            pivotClause.accept(this);

        }

    }

    public void preVisit(TPivotClause node) {
        if (node.getType() == TPivotClause.pivot) {
            acceptKeyword("pivot");
            acceptSymbol("(");
            if (node.getAggregation_function() != null) {
                node.getAggregation_function().accept(this);
            } else if (node.getAggregation_function_list() != null) {
                if (node.getAggregation_function_list().size() > 1)
                    acceptSymbol("(");
                for (int i = 0; i < node.getAggregation_function_list()
                        .size(); i++) {
                    node.getAggregation_function_list()
                            .getResultColumn(i)
                            .accept(this);
                    if (i != node.getAggregation_function_list().size() - 1)
                        acceptSymbol(",");
                }
                if (node.getAggregation_function_list().size() > 1)
                    acceptSymbol(")");
            }
            acceptKeyword("for");

            if (node.getPivotColumnList().size() > 1)
                acceptSymbol("(");
            for (int i = 0; i < node.getPivotColumnList().size(); i++) {
                node.getPivotColumnList().getElement(i).accept(this);
                if (i != node.getPivotColumnList().size() - 1)
                    acceptSymbol(",");
            }
            if (node.getPivotColumnList().size() > 1)
                acceptSymbol(")");

            node.getPivotInClause().accept(this);

            acceptSymbol(")");
        } else {
            acceptKeyword("unpivot");
            acceptSymbol("(");
            if (node.getValueColumnList().size() > 1)
                acceptSymbol("(");
            for (int i = 0; i < node.getValueColumnList().size(); i++) {
                node.getValueColumnList().getObjectName(i).accept(this);
                if (i != node.getValueColumnList().size() - 1)
                    acceptSymbol(",");
            }
            if (node.getValueColumnList().size() > 1)
                acceptSymbol(")");

            acceptKeyword("for");

            if (node.getPivotColumnList().size() > 1)
                acceptSymbol("(");
            for (int i = 0; i < node.getPivotColumnList().size(); i++) {
                node.getPivotColumnList().getElement(i).accept(this);
                if (i != node.getPivotColumnList().size() - 1)
                    acceptSymbol(",");
            }
            if (node.getPivotColumnList().size() > 1)
                acceptSymbol(")");

            node.getUnpivotInClause().accept(this);

            acceptSymbol(")");
        }
    }

    public void preVisit(TUnpivotInClauseItem node) {

        if (node.getColumn() != null) {
            node.getColumn().accept(this);
        } else if (node.getColumnList() != null) {
            acceptSymbol("(");
            for (int i = 0; i < node.getColumnList().size(); i++) {
                node.getColumnList().getObjectName(i).accept(this);
                if (i != node.getColumnList().size() - 1)
                    acceptSymbol(",");
            }
            acceptSymbol(")");
        }

        if (node.getConstant() != null) {
            acceptKeyword("as");
            node.getConstant().accept(this);
        } else if (node.getConstantList() != null) {
            acceptKeyword("as");
            acceptSymbol("(");
            for (int i = 0; i < node.getConstantList().size(); i++) {
                node.getConstantList().getElement(i).accept(this);
                if (i != node.getConstantList().size() - 1)
                    acceptSymbol(",");
            }
            acceptSymbol(")");
        }

    }

    public void preVisit(TUnpivotInClause node) {
        acceptKeyword("in");
        acceptSymbol("(");
        for (int i = 0; i < node.getItems().size(); i++) {
            node.getItems().getElement(i).accept(this);
            if (i != node.getItems().size() - 1)
                acceptSymbol(",");
        }
        acceptSymbol(")");
    }

    public void preVisit(TCaseExpression node) {

        acceptKeyword("case");
        if (node.getInput_expr() != null) {
            node.getInput_expr().accept(this);
        }

        for (int i = 0; i < node.getWhenClauseItemList().size(); i++) {
            node.getWhenClauseItemList().getWhenClauseItem(i).accept(this);
            if (i != node.getWhenClauseItemList().size() - 1)
                acceptNewline();
        }

        if (node.getElse_expr() != null) {
            acceptNewline();
            acceptKeyword("else");
            node.getElse_expr().accept(this);
        }

        if (node.getElse_statement_list().size() > 0) {
            node.getElse_statement_list().accept(this);
        }

        acceptNewline();
        acceptKeyword("end");
    }

    public void preVisit(TWhenClauseItem node) {
        acceptKeyword("when");
        node.getComparison_expr().accept(this);
        acceptNewline();
        acceptKeyword("then");
        if (node.getReturn_expr() != null) {
            node.getReturn_expr().accept(this);
        } else if (node.getStatement_list().size() > 0) {
            for (int i = 0; i < node.getStatement_list().size(); i++) {
                node.getStatement_list().get(i).accept(this);
            }

        }
    }

    public void preVisit(TForUpdate node) {
        switch (node.getForUpdateType()) {
            case forReadOnly:
                acceptKeyword("read");
                acceptKeyword("only");
                break;
            case forUpdate:
                acceptKeyword("for");
                acceptKeyword("update");
                break;
            case forUpdateOf:
                acceptKeyword("for");
                acceptKeyword("update");
                acceptKeyword("of");
                break;
        }

        if (node.getColumnRefs() != null) {
            visitObjectNameList(node.getColumnRefs());
        }
        if (node.isNowait())
            acceptKeyword("nowait");
        if (node.isWait()) {
            acceptKeyword("wait");
            acceptIdentifier(node.getWaitValue());
        }

    }

    public void preVisit(TDeleteSqlStatement stmt) {

        if (stmt.getCteList() != null) {
            acceptKeyword("with");
            visitCTEList(stmt.getCteList());
        }

        acceptKeyword("delete");

        if (stmt.getTopClause() != null) {
            stmt.getTopClause().accept(this);
        }

        if (stmt.isFromKeyword())
            acceptKeyword("from");
        stmt.getTargetTable().accept(this);

        if (stmt.joins.size() > 0) {
            acceptNewline();
            acceptKeyword("from");
            visitJoinList(stmt.joins);
        }

        if (stmt.getOutputClause() != null) {
            stmt.getOutputClause().accept(this);
        }

        if (stmt.getWhereClause() != null) {
            acceptNewline();
            stmt.getWhereClause().accept(this);
        }

        if (stmt.getReturningClause() != null) {
            stmt.getReturningClause().accept(this);
        }

    }

    public void preVisit(TUpdateSqlStatement stmt) {

        if (stmt.getCteList() != null) {
            acceptKeyword("with");
            visitCTEList(stmt.getCteList());
        }

        acceptKeyword("update");

        if (stmt.getTopClause() != null) {
            stmt.getTopClause().accept(this);
        }

        if (getDBVerdor(stmt) == EDbVendor.dbvmysql) {
            if (stmt.joins.size() > 0) {
                visitJoinList(stmt.joins);
            } else {
                stmt.getTargetTable().accept(this);
            }
        } else {
            stmt.getTargetTable().accept(this);
        }

        acceptNewline();
        acceptKeyword("set");
        visitResultColumnList(stmt.getResultColumnList());

        if (getDBVerdor(stmt) != EDbVendor.dbvmysql) {
            if (stmt.joins.size() > 0) {
                acceptNewline();
                acceptKeyword("from");
                visitJoinList(stmt.joins);
            }
        }

        if (stmt.getWhereClause() != null) {
            acceptNewline();
            stmt.getWhereClause().accept(this);
        }

        if (stmt.getOrderByClause() != null) {
            stmt.getOrderByClause().accept(this);
        }

        if (stmt.getLimitClause() != null) {
            stmt.getLimitClause().accept(this);
        }

        if (stmt.getOutputClause() != null) {
            stmt.getOutputClause().accept(this);
        }

        if (stmt.getReturningClause() != null) {
            stmt.getReturningClause().accept(this);
        }

    }

    private EDbVendor getDBVerdor(TCustomSqlStatement stmt) {
        if (stmt.dbvendor != null) {
            return stmt.dbvendor;
        } else if (stmt.getGsqlparser() != null) {
            return stmt.getGsqlparser().getDbVendor();
        }
        return null;
    }

    public void preVisit(TInsertSqlStatement stmt) {

        boolean insertAll = false;

        if (stmt.getCteList() != null) {
            acceptKeyword("with");
            visitCTEList(stmt.getCteList());
        }

        acceptKeyword("insert");
        if (stmt.isInsertAll())
            acceptKeyword("all");
        if (stmt.isInsertFirst())
            acceptKeyword("first");

        if (stmt.getInsertConditions() != null
                && stmt.getInsertConditions().size() > 0) {
            for (int i = 0; i < stmt.getInsertConditions().size(); i++) {
                TInsertCondition condition = stmt.getInsertConditions()
                        .getElement(i);
                preVisit(condition);
            }

            insertAll = true;
        }

        if (stmt.getElseIntoValues() != null) {
            acceptKeyword("else");
            for (int i = 0; i < stmt.getElseIntoValues().size(); i++) {
                stmt.getElseIntoValues().getElement(i).accept(this);
            }
            insertAll = true;
        }

        if (insertAll) {
            if (stmt.getSubQuery() != null) {
                acceptNewline();
                stmt.getSubQuery().accept(this);
            }
        }

        if (!insertAll) {
            acceptKeyword("into");

            if (stmt.getTargetTable() != null) {
                stmt.getTargetTable().accept(this);
            } else {
                // hive insert may have no target table
            }

            if (stmt.getColumnList() != null) {
                acceptSymbol("(");
                visitObjectNameList(stmt.getColumnList());
                acceptSymbol(")");
            }

            switch (stmt.getInsertSource()) {
                case values:
                    acceptNewline();
                    acceptKeyword("values");
                    TMultiTargetList multiTargetList = stmt.getValues();
                    visitMultiTargetList(multiTargetList);
                    break;
                case subquery:
                    acceptNewline();
                    stmt.getSubQuery().accept(this);
                    break;
                case values_empty:
                    break;
                case values_function:
                    acceptNewline();
                    acceptKeyword("values");
                    stmt.getFunctionCall().accept(this);
                    break;
                case values_oracle_record:
                    acceptNewline();
                    acceptKeyword("values");
                    stmt.getRecordName().accept(this);
                    break;
                case set_column_value:
                    // stmt.getSetColumnValues().accept(this);
                    break;
                default:
                    break;
            }
        }

        if (stmt.getOnDuplicateKeyUpdate() != null) {
            acceptKeyword("on");
            acceptKeyword("duplicate");
            acceptKeyword("key");
            acceptKeyword("update");
            visitResultColumnList(stmt.getOnDuplicateKeyUpdate());
        }

        if (stmt.getReturningClause() != null) {
            acceptNewline();
            stmt.getReturningClause().accept(this);
        }

    }

    public void preVisit(TInsertCondition node) {
        if (node.getCondition() != null) {
            acceptNewline();
            acceptKeyword("when");
            preVisit(node.getCondition());
            acceptKeyword("then");
        }

        if (node.getInsertIntoValues() != null
                && node.getInsertIntoValues().size() > 0) {
            acceptNewline();
            for (int i = 0; i < node.getInsertIntoValues().size(); i++) {
                TInsertIntoValue value = node.getInsertIntoValues()
                        .getElement(i);
                preVisit(value);
            }
        }
    }

    public void preVisit(TInsertIntoValue node) {
        if (node.getTable() != null) {
            acceptKeyword("into");
            preVisit(node.getTable());
            if (node.getColumnList() != null
                    && node.getColumnList().size() > 0) {
                acceptSymbol("(");
                visitObjectNameList(node.getColumnList());
                acceptSymbol(")");
            }
            if (node.getTargetList() != null
                    && node.getTargetList().size() > 0) {
                acceptKeyword("values");
                TMultiTargetList multiTargetList = node.getTargetList();
                visitMultiTargetList(multiTargetList);
            }
        }
    }

    private void visitMultiTargetList(TMultiTargetList multiTargetList) {
        for (int i = 0; i < multiTargetList.size(); i++) {
            TMultiTarget multiTarget = multiTargetList.getMultiTarget(i);
            acceptSymbol("(");

            for (int j = 0; j < multiTarget.getColumnList().size(); j++) {
                if (multiTarget.getColumnList()
                        .getResultColumn(j)
                        .isPlaceHolder())
                    continue; // teradata allow empty value
                multiTarget.getColumnList()
                        .getResultColumn(j)
                        .getExpr()
                        .accept(this);
                if (j != multiTarget.getColumnList().size() - 1)
                    acceptSymbol(",");
            }

            acceptSymbol(")");

            if (i != multiTargetList.size() - 1)
                acceptSymbol(",");

        }
    }

    public void preVisit(TMergeSqlStatement stmt) {

        if (stmt.getCteList() != null) {
            acceptKeyword("with");
            visitCTEList(stmt.getCteList());
        }

        acceptKeyword("merge");
        acceptKeyword("into");
        stmt.getTargetTable().accept(this);
        acceptKeyword("using");
        stmt.getUsingTable().accept(this);
        acceptKeyword("on");
        acceptSymbol("(");
        stmt.getCondition().accept(this);
        acceptSymbol(")");
        acceptNewline();

        if (stmt.getWhenClauses() != null) {
            for (int i = 0; i < stmt.getWhenClauses().size(); i++) {
                TMergeWhenClause whenClause = stmt.getWhenClauses()
                        .getElement(i);
                whenClause.accept(this);
            }
        }
    }

    public void preVisit(TMergeWhenClause node) {
        switch (node.getType()) {
            case TMergeWhenClause.matched:
                acceptKeyword("when");
                acceptKeyword("matched");
                acceptKeyword("then");
                break;
            case TMergeWhenClause.not_matched:
                acceptKeyword("when");
                acceptKeyword("not");
                acceptKeyword("matched");
                acceptKeyword("then");
                break;
            case TMergeWhenClause.matched_with_condition:
                acceptKeyword("when");
                acceptKeyword("matched");
                acceptKeyword("and");
                node.getCondition().accept(this);
                acceptKeyword("then");
                break;
            case TMergeWhenClause.not_matched_with_condition:
                acceptKeyword("when");
                acceptKeyword("not");
                acceptKeyword("matched");
                acceptKeyword("and");
                node.getCondition().accept(this);
                acceptKeyword("then");
                break;
            case TMergeWhenClause.not_matched_by_target:
                acceptKeyword("when");
                acceptKeyword("not");
                acceptKeyword("matched");
                acceptKeyword("by");
                acceptKeyword("target");
                acceptKeyword("then");
                break;
            case TMergeWhenClause.not_matched_by_target_with_condition:
                acceptKeyword("when");
                acceptKeyword("not");
                acceptKeyword("matched");
                acceptKeyword("by");
                acceptKeyword("target");
                acceptKeyword("and");
                node.getCondition().accept(this);
                acceptKeyword("then");
                break;
            case TMergeWhenClause.not_matched_by_source:
                acceptKeyword("when");
                acceptKeyword("not");
                acceptKeyword("matched");
                acceptKeyword("by");
                acceptKeyword("source");
                acceptKeyword("then");
                break;
            case TMergeWhenClause.not_matched_by_source_with_condition:
                acceptKeyword("when");
                acceptKeyword("not");
                acceptKeyword("matched");
                acceptKeyword("by");
                acceptKeyword("source");
                acceptKeyword("and");
                node.getCondition().accept(this);
                acceptKeyword("then");
                break;
            default:
                break;
        }

        if (node.getUpdateClause() != null) {
            node.getUpdateClause().accept(this);
        }

        if (node.getInsertClause() != null) {
            node.getInsertClause().accept(this);
        }

        if (node.getDeleteClause() != null) {
            node.getDeleteClause().accept(this);
        }

    }

    public void preVisit(TMergeUpdateClause node) {

        acceptKeyword("update");
        acceptKeyword("set");
        if (node.getUpdateColumnList() != null) {
            visitResultColumnList(node.getUpdateColumnList());
        }

        if (node.getUpdateWhereClause() != null) {
            acceptNewline();
            acceptKeyword("where");
            node.getUpdateWhereClause().accept(this);
        }

        if (node.getDeleteWhereClause() != null) {
            acceptNewline();
            acceptKeyword("delete");
            node.getDeleteWhereClause().accept(this);
        }

    }

    public void preVisit(TMergeInsertClause node) {

        acceptKeyword("insert");
        if (node.getColumnList() != null) {
            acceptSymbol("(");
            visitObjectNameList(node.getColumnList());
            acceptSymbol(")");
        }

        acceptKeyword("values");
        if (node.getValuelist() != null) {
            acceptSymbol("(");
            visitResultColumnList(node.getValuelist());
            acceptSymbol(")");
        }

        if (node.getInsertWhereClause() != null) {
            acceptNewline();
            acceptKeyword("where");
            node.getInsertWhereClause().accept(this);
        }

    }

    public void preVisit(TMergeDeleteClause node) {

    }

    public void preVisit(TCreateTableSqlStatement stmt) {
        acceptKeyword("create");

        if (!stmt.getTableKinds().isEmpty()) {
            if (stmt.getTableKinds().contains(ETableKind.etkBase)) {
                acceptKeyword("base");
            }
            if (stmt.getTableKinds().contains(ETableKind.etkTemporary)) {
                acceptKeyword("temporary");
            }
            if (stmt.getTableKinds().contains(ETableKind.etkGlobalTemporary)) {
                acceptKeyword("global");
                acceptKeyword("temporary");
            }
            if (stmt.getTableKinds().contains(ETableKind.etkLocalTemporary)) {
                acceptKeyword("local");
                acceptKeyword("temporary");
            }
            if (stmt.getTableKinds().contains(ETableKind.etkTemp)) {
                acceptKeyword("temp");
            }
            if (stmt.getTableKinds().contains(ETableKind.etkGlobalTemp)) {
                acceptKeyword("global");
                acceptKeyword("temp");
            }
            if (stmt.getTableKinds().contains(ETableKind.etkLocalTemp)) {
                acceptKeyword("local");
                acceptKeyword("temp");
            }
            if (stmt.getTableKinds().contains(ETableKind.etkVolatile)) {
                acceptKeyword("volatile");
            }
            if (stmt.getTableKinds().contains(ETableKind.etkSet)) {
                acceptKeyword("set");
            }
            if (stmt.getTableKinds().contains(ETableKind.etkMultiset)) {
                acceptKeyword("multiset");
            }
            if (stmt.getTableKinds().contains(ETableKind.etkExternal)) {
                acceptKeyword("external");
            }
        }
        acceptKeyword("table");
        stmt.getTargetTable().accept(this);

        if (stmt.getSubQuery() != null) {
            acceptKeyword("as");
            acceptNewline();
            stmt.getSubQuery().accept(this);

        } else {
            acceptSymbol("(");
            acceptNewline();
            for (int i = 0; i < stmt.getColumnList().size(); i++) {
                stmt.getColumnList().getColumn(i).accept(this);
                if (i != stmt.getColumnList().size() - 1) {
                    acceptSymbol(",");
                    acceptNewline();
                }

            }

            if ((stmt.getTableConstraints() != null)
                    && (stmt.getTableConstraints().size() > 0)) {
                //TODO 换顺序，保持parse样式
                acceptSymbol(",");
                for (int i = 0; i < stmt.getTableConstraints().size(); i++) {
                    acceptNewline();
                    stmt.getTableConstraints()
                            .getConstraint(i)
                            .accept(this);
                    if (i != stmt.getTableConstraints().size() - 1)
                        acceptSymbol(",");
                }
            }

            acceptNewline();
            acceptSymbol(")");


            //TODO 增加)结束符之外的内容
            TSourceToken endToken = stmt.getEndToken();
            List<String> indexTokens = new ArrayList<>();
            parseIndexFromTokens(endToken, indexTokens, false, ")", endToken.astext);
            for (int i = indexTokens.size() - 2; i >= 0; i--) {
                acceptIdentifier(indexTokens.get(i));
            }
        }

        if (stmt.getMySQLTableOptionList() != null
                && stmt.getMySQLTableOptionList().size() > 0) {
            preVisit(stmt.getMySQLTableOptionList());
        }

    }

    public void preVisit(TPTNodeList options) {
        for (int i = 0; i < options.size(); i++) {
            Object element = options.getElement(i);
            if (element instanceof TMySQLCreateTableOption) {
                preVisit((TMySQLCreateTableOption) options.getElement(i));
            }
        }
    }

    public void preVisit(TMySQLCreateTableOption option) {
        acceptKeyword(option.getOptionName());
        acceptSymbol("=");
        acceptKeyword(option.getOptionValue());
    }

    public void preVisit(TColumnDefinition node) {

        node.getColumnName().accept(this);

        if (node.getDatatype() != null) {
            node.getDatatype().accept(this);
        }

        if (node.getDefaultExpression() != null) {
            acceptKeyword("default");
            node.getDefaultExpression().accept(this);
        }

        if (node.isNull())
            acceptKeyword("null");

        if ((node.getConstraints() != null)
                && (node.getConstraints().size() > 0)) {
            for (int i = 0; i < node.getConstraints().size(); i++) {
                node.getConstraints().getConstraint(i).accept(this);
            }
        }

        if (node.isIdentity()) {
            acceptKeyword("identity");
            if (node.getSeed() != null) {
                acceptSymbol("(");
                node.getSeed().accept(this);
                acceptSymbol(",");
                node.getIncrement().accept(this);
                acceptSymbol(")");
            }
        }

    }

    public void preVisit(TColumnWithSortOrder node) {
        node.getColumnName().accept(this);
        if (node.getLength() != null) {
            acceptSymbol("(");
            node.getLength().accept(this);
            acceptSymbol(")");
        }
        if (node.getSortType() == ESortType.desc) {
            acceptKeyword("desc");
        }
        if (node.getSortType() == ESortType.asc) {
            acceptKeyword("asc");
        }
    }

    public void preVisit(TConstraint node) {

        if (node.getConstraintName() != null) {
            acceptKeyword("constraint");
            node.getConstraintName().accept(this);
        }


        switch (node.getConstraint_type()) {
            case notnull:
                acceptKeyword("not");
                acceptKeyword("null");
                break;
            //TODO 新增
            case table_index:
                acceptKeyword("index");
                break;
            case unique:
                acceptKeyword("unique");
                if (node.isClustered())
                    acceptKeyword("clustered");
                if (node.isNonClustered())
                    acceptKeyword("nonclustered");
                if (node.getColumnList() != null) {
                    acceptSymbol("(");
                    // visitObjectNameList( node.getColumnList( ) );
                    for (int i = 0; i < node.getColumnList().size(); i++) {
                        node.getColumnList().getElement(i).accept(this);
                        if (i != node.getColumnList().size() - 1) {
                            acceptSymbol(",");
                        }
                    }
                    acceptSymbol(")");
                }
                break;
            case check:
                acceptKeyword("check");
                if (node.getCheckCondition() != null) {
                    acceptSymbol("(");
                    node.getCheckCondition().accept(this);
                    acceptSymbol(")");
                } else {
                    // db2 functional dependency
                }

                break;
            case primary_key:
                acceptKeyword("primary");
                acceptKeyword("key");
                if (node.isClustered())
                    acceptKeyword("clustered");
                if (node.isNonClustered())
                    acceptKeyword("nonclustered");

                if (node.getColumnList() != null) {
                    acceptSymbol("(");
                    // visitObjectNameList( node.getColumnList( ) );
                    for (int i = 0; i < node.getColumnList().size(); i++) {
                        node.getColumnList().getElement(i).accept(this);
                        if (i != node.getColumnList().size() - 1) {
                            acceptSymbol(",");
                        }
                    }
                    acceptSymbol(")");
                }

                //TODO 增加WITH
                if (node.toString().contains("WITH")) {
                    TSourceToken endToken = node.getEndToken();
                    List<String> indexTokens = new ArrayList<>();
                    parseIndexFromTokens(endToken, indexTokens, false, "WITH", endToken.astext);
                    acceptSymbol(" ");
                    for (int i = indexTokens.size() - 1; i >= 0; i--) {
                        acceptIdentifier(indexTokens.get(i));
                    }
                }
                break;
            case foreign_key:
                acceptKeyword("foreign");
                acceptKeyword("key");
                if (node.getColumnList() != null) {
                    acceptSymbol("(");
                    // visitObjectNameList( node.getColumnList( ) );
                    for (int i = 0; i < node.getColumnList().size(); i++) {
                        node.getColumnList().getElement(i).accept(this);
                        if (i != node.getColumnList().size() - 1) {
                            acceptSymbol(",");
                        }
                    }
                    acceptSymbol(")");
                }

                if (node.getReferencedObject() != null) {
                    acceptKeyword("references");
                    node.getReferencedObject().accept(this);
                }

                if (node.getReferencedColumnList() != null) {
                    acceptSymbol("(");
                    visitObjectNameList(node.getReferencedColumnList());
                    acceptSymbol(")");
                }

                if (node.getKeyActions() != null
                        && node.getKeyActions().size() > 0) {
                    for (int i = 0; i < node.getKeyActions().size(); i++) {
                        TKeyAction keyAction = node.getKeyActions()
                                .getElement(i);
                        preVisit(keyAction);
                    }
                }

                break;
            case reference:
                acceptKeyword("references");
                if (node.getReferencedObject() != null) {
                    node.getReferencedObject().accept(this);
                }

                if (node.getReferencedColumnList() != null) {
                    acceptSymbol("(");
                    visitObjectNameList(node.getReferencedColumnList());
                    acceptSymbol(")");
                }

                if (node.getKeyActions() != null
                        && node.getKeyActions().size() > 0) {
                    for (int i = 0; i < node.getKeyActions().size(); i++) {
                        TKeyAction keyAction = node.getKeyActions()
                                .getElement(i);
                        preVisit(keyAction);
                    }
                }

                break;
            case default_value:
                acceptKeyword("default");
                node.getDefaultExpression().accept(this);
                break;
            default:
                break;
        }

        //TODO 增加indexname
        if (node.getIndexName() != null) {
            node.getIndexName().accept(this);

            //TODO 增加索引列,因为gsp未解析，所以目前根据token流获取
            TSourceToken endToken = node.getEndToken();
            List<String> indexTokens = new ArrayList<>();
            parseIndexFromTokens(endToken, indexTokens, false, "(", ")");
            for (int i = indexTokens.size() - 1; i >= 0; i--) {
                acceptIdentifier(indexTokens.get(i));
            }
        }

    }

    //TODO 从token流中解析需要的token段
    private void parseIndexFromTokens(TSourceToken token, List<String> indexTokens, boolean flag, String startToken, String endToken) {
        if (null != token) {
            TSourceToken prevTokenInChain = token.getPrevTokenInChain();
            String indexToken = token.astext;
            if (startToken.equals(indexToken)) {
                indexTokens.add(indexToken);
                return;
            }

            if (!endToken.equals(indexToken)) {
                if (flag) {
                    indexTokens.add(indexToken);
                }
                parseIndexFromTokens(prevTokenInChain, indexTokens, flag, startToken, endToken);
            } else {
                indexTokens.add(indexToken);
                parseIndexFromTokens(prevTokenInChain, indexTokens, true, startToken, endToken);
            }
        }
    }

    public void preVisit(TKeyAction node) {
        if (node.getActionType() == EKeyActionType.delete) {
            acceptKeyword("on");
            acceptKeyword("delete");
        }

        if (node.getActionType() == EKeyActionType.update) {
            acceptKeyword("on");
            acceptKeyword("update");
        }

        if (node.getKeyReference() != null) {
            preVisit(node.getKeyReference());
        }
    }

    public void preVisit(TKeyReference node) {
        if (node.getReferenceType() == EKeyReferenceType.set_null) {
            acceptKeyword("set");
            acceptKeyword("null");
        }

        if (node.getReferenceType() == EKeyReferenceType.set_default) {
            acceptKeyword("set");
            acceptKeyword("default");
        }

        if (node.getReferenceType() == EKeyReferenceType.cascade) {
            acceptKeyword("cascade");
        }

        if (node.getReferenceType() == EKeyReferenceType.restrict) {
            acceptKeyword("restrict");
        }

        if (node.getReferenceType() == EKeyReferenceType.no_action) {
            acceptKeyword("no");
            acceptKeyword("action");
        }
    }

    public void preVisit(TTypeName node) {
        acceptKeyword(node.getDataTypeName());

        switch (node.getDataType()) {
            case bigint_t:
            case bit_t:
            case money_t:
            case smallmoney_t:
            case smallint_t:
            case tinyint_t:
            case real_t:
            case smalldatetime_t:
            case datetime_t:
            case text_t:
            case ntext_t:
            case image_t:
            case rowversion_t:
            case uniqueidentifier_t:
            case sql_variant_t:
            case binary_float_t:
            case binary_double_t:
            case integer_t:
            case int_t:
            case double_t:
            case date_t:
                if (node.getPrecision() != null) {
                    acceptSymbol("(");
                    node.getPrecision().accept(this);
                    if (node.getScale() != null) {
                        acceptSymbol(",");
                        node.getScale().accept(this);
                    }
                    acceptSymbol(")");
                }
                break;

            case binary_t:
            case varbinary_t:
            case timestamp_t:
            case long_t:
            case raw_t:
            case long_raw_t:
            case blob_t:
            case clob_t:
            case nclob_t:
            case bfile_t:
            case urowid_t:
                if (node.getLength() != null) {
                    acceptSymbol("(");
                    node.getLength().accept(this);
                    acceptSymbol(")");
                }
                break;

            case decimal_t:
            case dec_t:
            case numeric_t:
            case number_t:
            case float_t:

                if (node.getPrecision() != null) {
                    acceptSymbol("(");
                    node.getPrecision().accept(this);
                    if (node.getScale() != null) {
                        acceptSymbol(",");
                        node.getScale().accept(this);
                    }
                    acceptSymbol(")");
                }
                break;

            case char_t:
            case character_t:
            case varchar_t:
            case nchar_t:
            case nvarchar_t:
            case ncharacter_t:
            case nvarchar2_t:
            case varchar2_t:
                if (node.isVarying())
                    acceptKeyword("varying");
                if (node.getLength() != null) {
                    acceptSymbol("(");
                    node.getLength().accept(this);
                    if (node.isByteUnit())
                        acceptKeyword("byte");
                    if (node.isCharUnit())
                        acceptKeyword("char");
                    acceptSymbol(")");
                }
                if (node.getCharsetName() != null) {
                    if (node.getGsqlparser() != null
                            && node.getGsqlparser().getDbVendor() == EDbVendor.dbvmysql) {
                        acceptKeyword("CHARACTER");
                        acceptKeyword("SET");
                        acceptSpace(1);
                        acceptIdentifier(node.getCharsetName());
                    } else {
                        acceptSpace(1);
                        acceptIdentifier(node.getCharsetName());
                    }
                }
                break;
            case datetimeoffset_t:
            case datetime2_t:
            case time_t:
                if (node.getFractionalSecondsPrecision() != null) {
                    acceptSymbol("(");
                    node.getFractionalSecondsPrecision().accept(this);
                    acceptSymbol(")");
                }
                break;
            case timestamp_with_time_zone_t:

                if (node.getFractionalSecondsPrecision() != null) {
                    acceptSymbol("(");
                    node.getFractionalSecondsPrecision().accept(this);
                    acceptSymbol(")");
                }
                acceptKeyword("with");
                acceptKeyword("time");
                acceptKeyword("zone");
                break;
            case timestamp_with_local_time_zone_t:
                if (node.getFractionalSecondsPrecision() != null) {
                    acceptSymbol("(");
                    node.getFractionalSecondsPrecision().accept(this);
                    acceptSymbol(")");
                }
                acceptKeyword("with");
                acceptKeyword("local");
                acceptKeyword("time");
                acceptKeyword("zone");
                break;
            case interval_year_to_month_t:
                if (node.getPrecision() != null) {
                    acceptSymbol("(");
                    node.getPrecision().accept(this);
                    acceptSymbol(")");
                }
                acceptKeyword("to");
                acceptKeyword("month");
                break;
            case interval_day_to_second_t:
                if (node.getPrecision() != null) {
                    acceptSymbol("(");
                    node.getPrecision().accept(this);
                    acceptSymbol(")");
                }
                acceptKeyword("to");
                acceptKeyword("second");
                if (node.getSecondsPrecision() != null) {
                    acceptSymbol("(");
                    node.getSecondsPrecision().accept(this);
                    acceptSymbol(")");
                }
                break;
            case generic_t:
                // if ( node.getDataTypeObjectName( ) != null )
                // {
                // acceptSpace( 1 );
                // node.getDataTypeObjectName( ).accept( this );
                // }
                // else if ( node.getDataTypeName( ) != null )
                // {
                // acceptSpace( 1 );
                // acceptIdentifier( node.getDataTypeName( ) );
                // }
                break;
            default:
                break;
        }
    }

    public void preVisit(TKeepDenseRankClause node) {
        acceptKeyword("keep");
        acceptSymbol("(");
        acceptKeyword("dense_rank");
        if (node.isFirst())
            acceptKeyword("first");
        if (node.isLast())
            acceptKeyword("last");
        node.getOrderBy().accept(this);
        acceptSymbol(")");
    }

    public void preVisit(TRollupCube node) {
        if (node.getOperation() == TRollupCube.cube) {
            acceptKeyword("cube");
        } else if (node.getOperation() == TRollupCube.rollup) {
            acceptKeyword("rollup");
        }
        acceptSymbol("(");
        visitExprList(node.getItems());
        acceptSymbol(")");
    }

    public void preVisit(TGroupingSet node) {
        acceptKeyword("grouping");
        acceptKeyword("sets");
        acceptSymbol("(");
        for (int i = 0; i < node.getItems().size(); i++) {
            node.getItems().getGroupingSetItem(i).accept(this);
            if (i != node.getItems().size() - 1)
                acceptSymbol(",");
        }
        acceptSymbol(")");

    }

    public void preVisit(TGroupingSetItem node) {
        if (node.getRollupCubeClause() != null) {
            node.getRollupCubeClause().accept(this);
        } else if (node.getGrouping_expression() != null) {
            node.getGrouping_expression().accept(this);
        }

    }

    public void preVisit(TReturningClause node) {
        acceptKeyword("returning");
        visitExprList(node.getColumnValueList());
        if (node.isBulkCollect()) {
            acceptKeyword("bulk");
            acceptKeyword("collect");
        }
        acceptKeyword("into");
        visitExprList(node.getVariableList());
    }

    public void preVisit(TDropIndexSqlStatement dropIndex) {
        acceptKeyword("drop");
        acceptKeyword("index");


        if (dropIndex.getDropIndexItemList() != null
                && dropIndex.getDropIndexItemList().size() > 0) {
            for (int i = 0; i < dropIndex.getDropIndexItemList().size(); i++) {
                TDropIndexItem item = dropIndex.getDropIndexItemList()
                        .getDropIndexItem(i);
                if (item.getIndexName() != null) {
                    item.getIndexName().accept(this);
                }
                if (item.getObjectName() != null) {
                    acceptKeyword("on");
                    item.getObjectName().accept(this);
                }
            }
        } else {
            if (dropIndex.getIndexName() != null) {
                dropIndex.getIndexName().accept(this);
            }
        }
    }

    public void preVisit(TCreateIndexSqlStatement createIndex) {
        acceptKeyword("create");

        if (createIndex.isNonClustered()) {
            acceptKeyword("nonclustered");
        }

        if (createIndex.isClustered()) {
            acceptKeyword("clustered");
        }

        if (createIndex.getIndexType() == EIndexType.itUnique) {
            acceptKeyword("unique");
        }

        acceptKeyword("index");

        if (createIndex.getIndexName() != null) {
            createIndex.getIndexName().accept(this);
        }

        if (createIndex.getTableName() != null) {
            acceptKeyword("on");

            createIndex.getTableName().accept(this);

            if (createIndex.getColumnNameList() != null
                    && createIndex.getColumnNameList().size() > 0) {
                acceptSpace(1);
                acceptSymbol("(");
                createIndex.getColumnNameList().accept(this);
                acceptSymbol(")");
            }
        }
        if (createIndex.getFilegroupOrPartitionSchemeName() != null) {
            acceptKeyword("on");

            createIndex.getFilegroupOrPartitionSchemeName().accept(this);

            if (createIndex.getPartitionSchemeColumns() != null
                    && createIndex.getPartitionSchemeColumns().size() > 0) {
                acceptSpace(1);
                acceptSymbol("(");
                createIndex.getPartitionSchemeColumns().accept(this);
                acceptSymbol(")");
            }
        }
    }

    public void preVisit(TOrderByItemList orderByList) {
        for (int i = 0; i < orderByList.size(); i++) {
            orderByList.getOrderByItem(i).accept(this);
            if (i != orderByList.size() - 1)
                acceptSymbol(",");
        }
    }

    public void preVisit(TUseDatabase useDataBase) {
        acceptKeyword("use");

        if (useDataBase.getDatabaseName() != null) {
            useDataBase.getDatabaseName().accept(this);
        }
    }

    public void preVisit(TPlsqlCreateProcedure procedure) {
        acceptKeyword("create");
        acceptKeyword("procedure");

        if (procedure.getProcedureName() != null) {
            procedure.getProcedureName().accept(this);
        }

        if (procedure.getParameterDeclarations() != null
                && procedure.getParameterDeclarations().size() > 0) {
            acceptSymbol("(");
            procedure.getParameterDeclarations().accept(this);
            acceptSymbol(")");
        }

        acceptNewline();

        if (procedure.getInvokerRightsClause() != null) {
            procedure.getInvokerRightsClause().accept(this);
        }

        acceptKeyword("as");

        if (procedure.getDeclareStatements() != null
                && procedure.getDeclareStatements().size() > 0) {
            acceptNewline();
            procedure.getDeclareStatements().accept(this);
            if (procedure.getDeclareStatements().size() == 1) {
                acceptSemicolon();
            }
        }

        acceptNewline();
        acceptKeyword("begin");

        if (procedure.getBodyStatements() != null
                && procedure.getBodyStatements().size() > 0) {
            acceptNewline();
            procedure.getBodyStatements().accept(this);
            if (procedure.getBodyStatements().size() == 1) {
                acceptSemicolon();
            }
        }

        acceptNewline();
        acceptKeyword("end");
        acceptSemicolon();
    }

    public void preVisit(TInvokerRightsClause clause) {
        acceptKeyword("authid");
        if (clause.getDefiner() != null) {
            clause.getDefiner().accept(this);
        }
    }

    void visitPrecisionScale(TConstant precision, TConstant scale) {
        if (precision != null) {
            acceptSymbol("(");
            precision.accept(this);
            if (scale != null) {
                acceptSymbol(",");
                scale.accept(this);
            }
            acceptSymbol(")");
        }
    }

    void visitExprList(TExpressionList expressionList) {
        for (int i = 0; i < expressionList.size(); i++) {
            expressionList.getExpression(i).accept(this);
            if (i != expressionList.size() - 1)
                acceptSymbol(",");
        }
    }

    void visitObjectNameList(TObjectNameList objectNameList) {
        for (int i = 0; i < objectNameList.size(); i++) {
            objectNameList.getObjectName(i).accept(this);
            if (i != objectNameList.size() - 1)
                acceptSymbol(",");
        }
    }

    void visitCTEList(TCTEList cteList) {
        for (int i = 0; i < cteList.size(); i++) {
            cteList.getCTE(i).accept(this);
            if (i != cteList.size() - 1) {
                acceptSymbol(",");
                acceptNewline();
            }
        }
    }

    void visitJoinList(TJoinList joinList) {
        for (int i = 0; i < joinList.size(); i++) {
            acceptNewline();
            joinList.getJoin(i).accept(this);
            if (i != joinList.size() - 1)
                acceptSymbol(",");
        }
    }

    void visitResultColumnList(TResultColumnList resultColumnList) {
        for (int i = 0; i < resultColumnList.size(); i++) {
            resultColumnList.getElement(i).accept(this);
            if (i != resultColumnList.size() - 1) {
                acceptSymbol(",");
            }
        }
    }

    void visitNodeByToken(TParseTreeNode node) {
        if (node.getStartToken() == null)
            return;
        if (node.getEndToken() == null)
            return;
        if (node.getStartToken().container == null)
            return;
        acceptSpace(1);
        for (int i = node.getStartToken().posinlist; i <= node.getEndToken().posinlist; i++) {
            acceptToken(node.getStartToken().container.get(i));
        }
        acceptSpace(1);
    }

}
