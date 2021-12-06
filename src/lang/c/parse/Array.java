package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class Array extends CParseRule {
    // array ::= LBRA expression RBRA （注）LBRA=’[’, RBRA=’]’
    private CToken lBar,rBar;
    private CParseRule expression;

    public Array(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType()==CToken.TK_LBAR;
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        lBar=tk;
        tk=ct.getNextToken(pcx);
        if(Expression.isFirst(tk)){
            expression = new Expression(pcx);
            expression.parse(pcx);
            tk = ct.getCurrentToken(pcx);
            if(tk.getType()==CToken.TK_RBAR){
               lBar=tk;
               tk=ct.getNextToken(pcx);
            }else{
                pcx.fatalError(tk.toExplainString()+"[の後ろに]がありません");
            }
        }else{
            pcx.fatalError(tk.toExplainString() + "[の後ろはexpressionです");
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(expression!=null){
            expression.semanticCheck(pcx);
            if(expression.getCType().isCType(CType.T_int)){
                this.setCType(expression.getCType());
                this.setConstant(expression.isConstant());
            }else{
                pcx.fatalError("[と]の間の式がintではありません");
            }
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; array starts");
        if (expression != null) {
            expression.codeGen(pcx);
        }
        o.println(";;; array completes");
    }
}