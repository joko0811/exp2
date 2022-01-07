package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class ConditionTerm extends CParseRule {
    // conditionTerm ::= conditionFactor {conditionLT | conditionLE | conditionGT | conditionGE | conditionEQ | conditionNE}
    private CParseRule conditionTerm;

    public ConditionTerm(CParseContext pcx) {}
    public static boolean isFirst(CToken tk) {
        return ConditionFactor.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CParseRule conditionFactor = null, list = null;

        conditionFactor = new ConditionFactor(pcx);
        conditionFactor.parse(pcx);

        CToken tk = ct.getCurrentToken(pcx);
        while(ConditionLT.isFirst(tk) || ConditionLE.isFirst(tk) || ConditionGT.isFirst(tk) || ConditionGE.isFirst(tk) || ConditionEQ.isFirst(tk) || ConditionNE.isFirst(tk)){
            if (ConditionLT.isFirst(tk)) {
                list = new ConditionLT(pcx,conditionFactor);
                list.parse(pcx);
                conditionFactor = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionLE.isFirst(tk)) {
                list = new ConditionLE(pcx,conditionFactor);
                list.parse(pcx);
                conditionFactor = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionGT.isFirst(tk)) {
                list = new ConditionGT(pcx,conditionFactor);
                list.parse(pcx);
                conditionFactor = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionGE.isFirst(tk)) {
                list = new ConditionGE(pcx,conditionFactor);
                list.parse(pcx);
                conditionFactor = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionEQ.isFirst(tk)) {
                list = new ConditionEQ(pcx,conditionFactor);
                list.parse(pcx);
                conditionFactor = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionNE.isFirst(tk)) {
                list = new ConditionNE(pcx,conditionFactor);
                list.parse(pcx);
                conditionFactor = list;
                tk = ct.getCurrentToken(pcx);
            }
            tk = ct.getCurrentToken(pcx);
        }
        conditionTerm = conditionFactor;

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if (conditionTerm!=null){
            conditionTerm.semanticCheck(pcx);
            this.setCType(conditionTerm.getCType());
            this.setConstant(conditionTerm.isConstant());
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; conditionTerm starts");
        if(conditionTerm!=null){conditionTerm.codeGen(pcx);}
        o.println(";;; conditionTerm completes");
    }
}
