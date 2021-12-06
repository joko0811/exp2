package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class StatementAssign extends CParseRule {
    // statementAssign ::= primary ASSIGN expression SEMI （注）ASSIGN=’=’, SEMI=’;’
    private CParseRule primary,expression;
    private CToken assign,semi;

    public StatementAssign(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return Expression.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        primary=new Primary(pcx);
        primary.parse(pcx);
        tk=ct.getCurrentToken(pcx);
        if(tk.getType()==CToken.TK_ASSIGN){
            assign=tk;
            tk=ct.getNextToken(pcx);
            if(Expression.isFirst(tk)){
                expression=new Expression(pcx);
                expression.parse(pcx);
                tk=ct.getCurrentToken(pcx);
                if(tk.getType()==CToken.TK_SEMI){
                    semi=tk;
                    tk=ct.getNextToken(pcx);
                }else{
                    pcx.fatalError(tk.toExplainString()+"行末に';'がありません");
                }
            }else{
                pcx.fatalError(tk.toExplainString()+"'='の後に右辺がありません");
            }
        }else{
            pcx.fatalError(tk.toExplainString()+"左辺の後に'='がありません");
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; program starts");
        o.println(";;; program completes");
    }
}
