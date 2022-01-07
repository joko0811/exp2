package lang.c.parse;

import java.io.PrintStream;
import java.util.ArrayList;

import lang.*;
import lang.c.*;

public class UnsignedCondition extends CParseRule{
    // unsignedCondition ::= conditionTerm {conditionAnd | conditionOr}
    private CParseRule conditionTerm;
    private ArrayList<CParseRule> conditionList;

    public UnsignedCondition(CParseContext pcx){
        conditionList = new ArrayList<CParseRule>();
    }
    public static boolean isFirst(CToken tk) {
        return ConditionTerm.isFirst(tk);
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        conditionTerm = new ConditionTerm(pcx);
        conditionTerm.parse(pcx);
        CToken tk = ct.getCurrentToken(pcx);

        while(true){
            CParseRule condition;
            if(ConditionAnd.isFirst(tk)){
                condition = new ConditionAnd(pcx,conditionTerm);
                condition.parse(pcx);
                conditionList.add(condition);
            }else if(ConditionOr.isFirst(tk)){
                condition = new ConditionOr(pcx,conditionTerm);
                condition.parse(pcx);
                conditionList.add(condition);
            }else{
                break;
            }
            tk=ct.getCurrentToken(pcx);
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(conditionTerm!=null){
            conditionTerm.semanticCheck(pcx);
            setCType(conditionTerm.getCType());
            setConstant(conditionTerm.isConstant());
            for(int i=0;i<conditionList.size();i++){
                conditionList.get(i).semanticCheck(pcx);
            }
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {

    }
}
