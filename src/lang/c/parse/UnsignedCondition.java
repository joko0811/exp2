package lang.c.parse;

import lang.*;
import lang.c.*;

public class UnsignedCondition extends CParseRule{
    // unsignedCondition ::= conditionTerm {conditionAnd | conditionOr}
    private CParseRule conditionTerm,condition;

    public UnsignedCondition(CParseContext pcx){
    }
    public static boolean isFirst(CToken tk) {
        return ConditionTerm.isFirst(tk);
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        conditionTerm = new ConditionTerm(pcx);
        conditionTerm.parse(pcx);
        CToken tk = ct.getCurrentToken(pcx);

        if(ConditionAnd.isFirst(tk)){
            condition = new ConditionAnd(pcx,conditionTerm);
            condition.parse(pcx);
        }else if(ConditionOr.isFirst(tk)){
            condition = new ConditionOr(pcx,conditionTerm);
            condition.parse(pcx);
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {

    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {

    }
}
