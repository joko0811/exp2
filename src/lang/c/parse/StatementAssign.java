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
        return Primary.isFirst(tk);
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
        if(primary!=null&&expression!=null){
            primary.semanticCheck(pcx);
            expression.semanticCheck(pcx);
            if(primary.getCType()==expression.getCType()){
                if(primary.isConstant()){
                    pcx.fatalError("'='の左辺が定数です。定数には代入できません");
                }
            }else{
                pcx.fatalError("'='の左右の式の方は一致している必要があります");
            }
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; statementAssign starts");
        primary.codeGen(pcx);
        expression.codeGen(pcx);

        o.println("\tMOV\t-(R6), R0\t; statementAssign: 左辺、右辺の式を取り出して、右辺の式の値を左辺の式のアドレスに書き込む");
        o.println("\tMOV\t-(R6), R1\t; statementAssign: ");
        o.println("\tMOV\tR0, (R1)\t; statementAssign: ");

        o.println(";;; statementAssign completes");
    }
}
