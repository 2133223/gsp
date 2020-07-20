package test719;

import gudusoft.gsqlparser.EComparisonType;
import gudusoft.gsqlparser.EExpressionType;
import gudusoft.gsqlparser.ETokenType;
import gudusoft.gsqlparser.TBaseType;
import gudusoft.gsqlparser.TSourceToken;
import gudusoft.gsqlparser.TSourceTokenList;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class TScriptWriter {

    private List<TSourceToken> tokenList = new ArrayList<TSourceToken>();

    public void TScriptWriter() {

    }

    public void addComparisonOperator(EComparisonType comparisonType) {
        addSpace(1);
        switch (comparisonType) {
            case equals:
                addSymbol("=");
                break;
            case greaterThan:
                addSymbol(">");
                break;
            case lessThan:
                addSymbol("<");
                break;
            case notEqualToBrackets:
                addSymbol("<>");
                break;
            case notEqualToCaret:
                addSymbol("^=");
                break;
            case notEqualToExclamation:
                addSymbol("!=");
                break;
            case greaterThanOrEqualTo:
                addSymbol(">=");
                break;
            case lessThanOrEqualTo:
                addSymbol("<=");
                break;
            case notGreaterThan:
                addSymbol("!>");
                break;
            case notGreaterThanToCaret:
                addSymbol("^>");
                break;
            case notLessThan:
                addSymbol("!<");
                break;
            case notLessThanToCaret:
                addSymbol("^<");
                break;
            default:
                break;
        }
        addSpace(1);
    }

    public void addUnaryOperator(EExpressionType expressionType) {
        switch (expressionType) {
            case unary_plus_t:
                addSymbol("+");
                break;
            case unary_minus_t:
                addSymbol("-");
                break;
            case unary_bitwise_not_t:
                addSymbol("~");
                break;
            case unary_prior_t:
                addKeyword("prior");
                break;
            case unary_connect_by_root_t:
                addKeyword("connect_by_root");
                break;
            default:
                break;
        }

    }

    public void addBinaryOperator(EExpressionType expressionType) {
        if (expressionType == EExpressionType.collate_t) {
            addKeyword("COLLATE");
            return;
        }
        addSpace(1);
        switch (expressionType) {
            case arithmetic_plus_t:
                addSymbol("+");
                break;
            case arithmetic_minus_t:
                addSymbol("-");
                break;
            case arithmetic_times_t:
                addSymbol("*");
                break;
            case arithmetic_divide_t:
                addSymbol("/");
                break;
            case arithmetic_modulo_t:
                addSymbol("%");
                break;
            case bitwise_and_t:
                addSymbol("&");
                break;
            case bitwise_or_t:
                addSymbol("|");
                break;
            case bitwise_exclusive_or_t:
                addSymbol("^");
                break;
            case bitwise_xor_t:
                addSymbol("#");
                break;
            case logical_and_t:
                addKeyword("and");
                break;
            case logical_or_t:
                addKeyword("or");
                break;
            case concatenate_t:
                addSymbol("||");
                break;
            case sqlserver_proprietary_column_alias_t:
                addSymbol("=");
                break;
            case left_join_t:
                addSymbol("*=");
                break;
            case right_join_t:
                addSymbol("=*");
                break;
        }
        addSpace(1);

    }

    public void reset() {
        tokenList.clear();
    }

    public void writeTo(Writer writer) {

        for (TSourceToken st : tokenList) {
            try {
                writer.write(st.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean verifyTokens(TSourceTokenList originalTokens,
                                boolean partialChecking) {
        boolean result = true;
        int old = 0;
        boolean startParenthesis = false;
        int nestedParenthesis = 0;

        for (int i = 0; i < tokenList.size(); i++) {
            if (tokenList.get(i).tokentype == ETokenType.ttkeyword) {
                // must a space after keyword
                if (i != tokenList.size() - 1) {
                    if ((tokenList.get(i + 1).tokencode == TBaseType.lexnewline)
                            || (tokenList.get(i + 1).tokencode == TBaseType.lexspace)
                            || (tokenList.get(i + 1).tokencode == '(')
                            || (tokenList.get(i + 1).tokencode == ')')) {
                        continue;
                    } else {
                        System.out.print("lack space after keyword:"
                                + tokenList.get(i).toString());
                        result = false;
                        break;
                    }
                }
            }

            if (tokenList.get(i).tokentype == ETokenType.ttidentifier) {
                // must a space between identifier and keyword/identifier
                if (i != 0) {
                    if ((tokenList.get(i - 1).tokentype == ETokenType.ttkeyword)
                            || (tokenList.get(i - 1).tokentype == ETokenType.ttidentifier)) {
                        System.out.print("lack space between identifier and keyword:"
                                + tokenList.get(i).toString());
                        result = false;
                        break;
                    } else {
                        continue;
                    }
                }
            }

        }

        if (!result)
            return result;

        for (int i = 0; i < originalTokens.size(); i++) {
            if ((originalTokens.get(i).tokencode == TBaseType.lexnewline)
                    || (originalTokens.get(i).tokencode == TBaseType.lexspace)
                    || (originalTokens.get(i).tokentype == ETokenType.ttsimplecomment)
                    || (originalTokens.get(i).tokentype == ETokenType.ttbracketedcomment)
                    || (originalTokens.get(i).tokentype == ETokenType.ttsemicolon)) {
                continue;
            }

            if (partialChecking) {
                if (originalTokens.get(i).tokencode == '(') {
                    startParenthesis = true;
                    nestedParenthesis++;
                } else if (originalTokens.get(i).tokencode == ')') {
                    if (nestedParenthesis > 0)
                        nestedParenthesis--;
                    if ((nestedParenthesis == 0) && startParenthesis) {
                        result = true;
                        break;
                    }
                }
            }

            result = false;
            for (int j = old; j < tokenList.size(); j++) {
                if ((tokenList.get(j).tokencode == TBaseType.lexnewline)
                        || (tokenList.get(j).tokencode == TBaseType.lexspace)
                        || (tokenList.get(j).tokentype == ETokenType.ttsimplecomment)
                        || (tokenList.get(j).tokentype == ETokenType.ttbracketedcomment)
                        || (tokenList.get(j).tokentype == ETokenType.ttsemicolon)) {
                    continue;
                }

                if ((originalTokens.get(i).tokencode == TBaseType.outer_join)
                        && (tokenList.get(j).tokencode == TBaseType.outer_join)) {
                    result = true;
                } else {
                    result = originalTokens.get(i)
                            .toString()
                            .equalsIgnoreCase(tokenList.get(j).toString());
                }

                old = j + 1;
                break;
            }

            if (!result) {
                System.out.print("source token:"
                        + originalTokens.get(i).toString()
                        + "("
                        + originalTokens.get(i).lineNo
                        + ","
                        + originalTokens.get(i).columnNo
                        + ")");
                System.out.print(", target token:"
                        + tokenList.get(old - 1).toString()
                        + "("
                        + tokenList.get(old - 1).lineNo
                        + ","
                        + tokenList.get(old - 1).columnNo
                        + ")");
                break;
            }
            // if (! result) break;
        }

        return result;
    }

    public void addSymbol(String symbol) {
        TSourceToken st = new TSourceToken(symbol);
        addToken(st);
    }

    public void addSymbol(String symbol, int tokenCode) {
        TSourceToken st = new TSourceToken(symbol);
        st.tokencode = tokenCode;
        addToken(st);
    }

    public void addToken(TSourceToken st) {
        if ((tokenList.size() == 0)
                && ((st.tokencode == TBaseType.lexspace) || (st.tokencode == TBaseType.lexnewline))) {
            // don't add space at the beginning
        } else
            tokenList.add(st);
    }

    public void addSpace(int count) {
        TSourceToken st = new TSourceToken(String.format("%" + count + "s",
                " "));
        st.tokencode = TBaseType.lexspace;
        addToken(st);
    }

    public void addNewline() {
        TSourceToken st = new TSourceToken(TBaseType.linebreak);
        st.tokencode = TBaseType.lexnewline;
        addToken(st);
    }

    public void addSemicolon() {
        TSourceToken st = new TSourceToken(";");
        st.tokentype = ETokenType.ttsemicolon;
        addToken(st);
    }

    public void addIdentifier(String text) {
        TSourceToken st = new TSourceToken(text);
        st.tokencode = TBaseType.ident;
        st.tokentype = ETokenType.ttidentifier;
        addToken(st);
    }

    public void acceptOracleHint(String text) {
        TSourceToken st = new TSourceToken(text);
        st.tokencode = TBaseType.cmtslashstar;
        st.tokentype = ETokenType.ttbracketedcomment;
        addToken(st);
    }

    public void addKeyword(String text) {
        addKeyword(text, true);
    }

    public void addKeyword(String text, boolean spaceAround) {
        TSourceToken st = new TSourceToken(text);
        st.tokentype = ETokenType.ttkeyword;
        if (spaceAround)
            addSpace(1);
        addToken(st);
        if (spaceAround)
            addSpace(1);
    }

    public void addOperator(String text, ETokenType tokenType) {
        TSourceToken st = new TSourceToken(text);
        st.tokentype = tokenType;
        addSpace(1);
        addToken(st);
        addSpace(1);
    }

    public void addLiteral(String text) {
        TSourceToken st = new TSourceToken(text);
        addToken(st);
    }

}
