package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class ConditionSimple extends CParseRule {
    // conditionSimple ::= TRUE | FALSE | expression ( conditionLT | conditionLE | conditionGT | conditionGE | conditionEQ | conditionNE )
    private CParseRule expression,condition;
    private boolean flag;

    public ConditionSimple(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return (Expression.isFirst(tk) | tk.getType() == CToken.TK_TRUE | tk.getType() == CToken.TK_FALSE);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);

        if (Expression.isFirst(tk)) {
            expression = new Expression(pcx);
            expression.parse(pcx);
            tk = ct.getCurrentToken(pcx);
            if (ConditionLT.isFirst(tk)) {
                condition = new ConditionLT(pcx,expression);
                condition.parse(pcx);
            } else if (ConditionLE.isFirst(tk)) {
                condition = new ConditionLE(pcx,expression);
                condition.parse(pcx);
            } else if (ConditionGT.isFirst(tk)) {
                condition = new ConditionGT(pcx,expression);
                condition.parse(pcx);
            } else if (ConditionGE.isFirst(tk)) {
                condition = new ConditionGE(pcx,expression);
                condition.parse(pcx);
            } else if (ConditionEQ.isFirst(tk)) {
                condition = new ConditionEQ(pcx,expression);
                condition.parse(pcx);
            } else if (ConditionNE.isFirst(tk)) {
                condition = new ConditionNE(pcx,expression);
                condition.parse(pcx);
            } else {
                pcx.fatalError(tk.toExplainString() + " expressionの後には比較演算子が必要です");
            }
        } else if (tk.getType() == CToken.TK_TRUE) {
            flag = true;
            tk = ct.getNextToken(pcx);
        } else if (tk.getType() == CToken.TK_FALSE) {
            flag = false;
            tk = ct.getNextToken(pcx);
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(expression!=null){
            expression.semanticCheck(pcx);
        }
        if(condition!=null){
            condition.semanticCheck(pcx);
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; condition starts");
        if (condition != null) {
            condition.codeGen(pcx);
        }else{
            if(flag){
                o.println("\tMOV\t#0x0001, (R6)+\t; ConditionSimple: true(1)をスタックに積む");
            }else{
                o.println("\tMOV\t#0x0000, (R6)+\t; ConditionSimple: false(0)をスタックに積む");
            }
        }
        o.println(";;; condition completes");
    }
}
