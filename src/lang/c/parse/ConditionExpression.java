package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class ConditionExpression extends CParseRule {
    // conditionExpression ::= conditionTerm {conditionAnd}
    private CParseRule conditionExpression;

    public ConditionExpression(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return ConditionTerm.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CParseRule conditionTerm=null, list=null;
        conditionTerm = new ConditionTerm(pcx);

        conditionTerm.parse(pcx);
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);

        while(ConditionLT.isFirst(tk) || ConditionLE.isFirst(tk) || ConditionGT.isFirst(tk) || ConditionGE.isFirst(tk) || ConditionEQ.isFirst(tk) || ConditionNE.isFirst(tk)){
            if (ConditionLT.isFirst(tk)) {
                list = new ConditionLT(pcx,conditionTerm);
                list.parse(pcx);
                conditionTerm = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionLE.isFirst(tk)) {
                list = new ConditionLE(pcx,conditionTerm);
                list.parse(pcx);
                conditionTerm = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionGT.isFirst(tk)) {
                list = new ConditionGT(pcx,conditionTerm);
                list.parse(pcx);
                conditionTerm = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionGE.isFirst(tk)) {
                list = new ConditionGE(pcx,conditionTerm);
                list.parse(pcx);
                conditionTerm = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionEQ.isFirst(tk)) {
                list = new ConditionEQ(pcx,conditionTerm);
                list.parse(pcx);
                conditionTerm = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionNE.isFirst(tk)) {
                list = new ConditionNE(pcx,conditionTerm);
                list.parse(pcx);
                conditionTerm = list;
                tk = ct.getCurrentToken(pcx);
            }
            tk = ct.getCurrentToken(pcx);
        }
        conditionExpression = conditionTerm;
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(conditionExpression!=null){
            conditionExpression.semanticCheck(pcx);
            setCType(conditionExpression.getCType());
            setConstant(conditionExpression.isConstant());
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; conditionExpression starts");
        if (conditionExpression != null) {
            conditionExpression.codeGen(pcx);
        }
        o.println(";;; conditionExpression completes");
    }
}
