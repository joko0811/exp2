package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class ConditionTerm extends CParseRule {
    // conditionTerm ::= conditionFactor|conditionNot
    private CParseRule conditionTerm;

    public ConditionTerm(CParseContext pcx) {}
    public static boolean isFirst(CToken tk) {
        return (ConditionFactor.isFirst(tk)||ConditionNot.isFirst(tk));
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        CToken tk = ct.getCurrentToken(pcx);

        if(ConditionFactor.isFirst(tk)){
            conditionTerm = new ConditionFactor(pcx);
            conditionTerm.parse(pcx);
        }else if(ConditionNot.isFirst(tk)){
            conditionTerm = new ConditionNot(pcx);
            conditionTerm.parse(pcx);
        }
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
