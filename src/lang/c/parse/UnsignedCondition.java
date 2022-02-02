package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class UnsignedCondition extends CParseRule{
    // unsignedCondition ::= conditionTerm {conditionAnd | conditionOr}
    private CParseRule unsignedCondition;

    public UnsignedCondition(CParseContext pcx){}
    public static boolean isFirst(CToken tk) {
        return ConditionTerm.isFirst(tk);
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CParseRule conditionTerm=null, list=null;
        conditionTerm = new ConditionTerm(pcx);
        conditionTerm.parse(pcx);
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        while(ConditionAnd.isFirst(tk)||ConditionOr.isFirst(tk)){
            if(ConditionAnd.isFirst(tk)){
                list = new ConditionAnd(pcx,conditionTerm);
                list.parse(pcx);
                conditionTerm = list;
                tk=ct.getCurrentToken(pcx);
            }else if(ConditionOr.isFirst(tk)){
                list = new ConditionOr(pcx,conditionTerm);
                list.parse(pcx);
                conditionTerm = list;
                tk=ct.getCurrentToken(pcx);
            }
        }
        unsignedCondition = conditionTerm;
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if (unsignedCondition!=null){
            unsignedCondition.semanticCheck(pcx);
            this.setCType(unsignedCondition.getCType());
            this.setConstant(unsignedCondition.isConstant());
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; unsignedCondition starts");
        if (unsignedCondition != null) { unsignedCondition.codeGen(pcx); }
        o.println(";;; unsignedCondition completes");
    }
}
