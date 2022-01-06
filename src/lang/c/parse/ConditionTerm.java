package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class ConditionTerm extends CParseRule {
    // conditionTerm ::= conditionFactor {conditionLT | conditionLE | conditionGT | conditionGE | conditionEQ | conditionNE}
    private CParseRule conditionFactor,condition;
    private boolean flag;

    public ConditionTerm(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return ConditionFactor.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        conditionFactor = new ConditionFactor(pcx);
        conditionFactor.parse(pcx);

        CToken tk = ct.getCurrentToken(pcx);

        if (ConditionLT.isFirst(tk)) {
            condition = new ConditionLT(pcx,conditionFactor);
            condition.parse(pcx);
        } else if (ConditionLE.isFirst(tk)) {
            condition = new ConditionLE(pcx,conditionFactor);
            condition.parse(pcx);
        } else if (ConditionGT.isFirst(tk)) {
            condition = new ConditionGT(pcx,conditionFactor);
            condition.parse(pcx);
        } else if (ConditionGE.isFirst(tk)) {
            condition = new ConditionGE(pcx,conditionFactor);
            condition.parse(pcx);
        } else if (ConditionEQ.isFirst(tk)) {
            condition = new ConditionEQ(pcx,conditionFactor);
            condition.parse(pcx);
        } else if (ConditionNE.isFirst(tk)) {
            condition = new ConditionNE(pcx,conditionFactor);
            condition.parse(pcx);
        } else {
            pcx.fatalError(tk.toExplainString() + " conditionFactorの後には比較演算子が必要です");
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; condition starts");
        o.println(";;; condition completes");
    }
}
