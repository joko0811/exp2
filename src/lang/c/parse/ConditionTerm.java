package lang.c.parse;

import java.io.PrintStream;
import java.util.ArrayList;

import lang.*;
import lang.c.*;

public class ConditionTerm extends CParseRule {
    // conditionTerm ::= conditionFactor {conditionLT | conditionLE | conditionGT | conditionGE | conditionEQ | conditionNE}
    private CParseRule conditionFactor;
    private ArrayList<CParseRule> conditionList;

    public ConditionTerm(CParseContext pcx) {
        conditionList = new ArrayList<CParseRule>();
    }
    public static boolean isFirst(CToken tk) {
        return ConditionFactor.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        conditionFactor = new ConditionFactor(pcx);
        conditionFactor.parse(pcx);

        CToken tk = ct.getCurrentToken(pcx);
        while(true){
            CParseRule condition;
            if (ConditionLT.isFirst(tk)) {
                condition = new ConditionLT(pcx,conditionFactor);
                condition.parse(pcx);
                conditionList.add(condition);
            } else if (ConditionLE.isFirst(tk)) {
                condition = new ConditionLE(pcx,conditionFactor);
                condition.parse(pcx);
                conditionList.add(condition);
            } else if (ConditionGT.isFirst(tk)) {
                condition = new ConditionGT(pcx,conditionFactor);
                condition.parse(pcx);
                conditionList.add(condition);
            } else if (ConditionGE.isFirst(tk)) {
                condition = new ConditionGE(pcx,conditionFactor);
                condition.parse(pcx);
                conditionList.add(condition);
            } else if (ConditionEQ.isFirst(tk)) {
                condition = new ConditionEQ(pcx,conditionFactor);
                condition.parse(pcx);
                conditionList.add(condition);
            } else if (ConditionNE.isFirst(tk)) {
                condition = new ConditionNE(pcx,conditionFactor);
                condition.parse(pcx);
                conditionList.add(condition);
            } else {
                break;
            }
            tk = ct.getCurrentToken(pcx);
        }

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(conditionFactor!=null){
            conditionFactor.semanticCheck(pcx);
            for(int i=0;i<conditionList.size();i++){
                conditionList.get(i).semanticCheck(pcx);
            }
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; condition starts");
        o.println(";;; condition completes");
    }
}
