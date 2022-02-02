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
        while(ConditionAnd.isFirst(tk)){
            if(ConditionAnd.isFirst(tk)){
                list = new ConditionAnd(pcx,conditionTerm);
                list.parse(pcx);
                conditionTerm = list;
                tk=ct.getCurrentToken(pcx);
            }
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
