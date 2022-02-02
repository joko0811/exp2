package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class ConditionFactor extends CParseRule{
    // conditionFactor ::= TRUE | FALSE | expression
    private CParseRule conditionFactor;

    private boolean flag;

    public ConditionFactor(CParseContext pcx){}
    public static boolean isFirst(CToken tk) {
        return (Expression.isFirst(tk)||tk.getType()==CToken.TK_TRUE||tk.getType()==CToken.TK_FALSE);
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);

        if(Expression.isFirst(tk)){
            conditionFactor = new Expression(pcx);
            conditionFactor.parse(pcx);
        }else if(tk.getType()==CToken.TK_TRUE){
            flag=true;
            tk=ct.getNextToken(pcx);
        }else if(tk.getType()==CToken.TK_FALSE){
            flag=false;
            tk=ct.getNextToken(pcx);
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(conditionFactor!=null){
            conditionFactor.semanticCheck(pcx);
            this.setCType(conditionFactor.getCType());
            this.setConstant(conditionFactor.isConstant());
        }else{ //true/falseの時
            this.setCType(CType.getCType(CType.T_bool));
            this.setConstant(true);
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; conditionFactor starts");
        if (conditionFactor != null) {
            conditionFactor.codeGen(pcx);
        }else{
            if(flag){
                o.println("\tMOV\t#0x0001, (R6)+\t; Condition: true(1)をスタックに積む");
            }else{
                o.println("\tMOV\t#0x0000, (R6)+\t; Condition: false(0)をスタックに積む");
            }
        }
        o.println(";;; conditionFactor completes");
    }
}
