package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class Condition extends CParseRule {
    // condition ::= conditionStatement  {conditionLT | conditionLE | conditionGT | conditionGE | conditionEQ | conditionNE}
    private CParseRule condition;

    public Condition(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return ConditionExpression.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CParseRule conditionExpression=null, list=null;
        conditionExpression = new ConditionExpression(pcx);
        conditionExpression.parse(pcx);

        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);

        while(ConditionLT.isFirst(tk) || ConditionLE.isFirst(tk) || ConditionGT.isFirst(tk) || ConditionGE.isFirst(tk) || ConditionEQ.isFirst(tk) || ConditionNE.isFirst(tk)){
            if (ConditionLT.isFirst(tk)) {
                list = new ConditionLT(pcx,conditionExpression);
                list.parse(pcx);
                conditionExpression = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionLE.isFirst(tk)) {
                list = new ConditionLE(pcx,conditionExpression);
                list.parse(pcx);
                conditionExpression = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionGT.isFirst(tk)) {
                list = new ConditionGT(pcx,conditionExpression);
                list.parse(pcx);
                conditionExpression = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionGE.isFirst(tk)) {
                list = new ConditionGE(pcx,conditionExpression);
                list.parse(pcx);
                conditionExpression = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionEQ.isFirst(tk)) {
                list = new ConditionEQ(pcx,conditionExpression);
                list.parse(pcx);
                conditionExpression = list;
                tk = ct.getCurrentToken(pcx);
            } else if (ConditionNE.isFirst(tk)) {
                list = new ConditionNE(pcx,conditionExpression);
                list.parse(pcx);
                conditionExpression = list;
                tk = ct.getCurrentToken(pcx);
            }
            tk = ct.getCurrentToken(pcx);
        }
        condition = conditionExpression;
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(condition !=null){
            condition.semanticCheck(pcx);
            setCType(condition.getCType());
            setConstant(condition.isConstant());
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; condition starts");
        if (condition != null) {
            condition.codeGen(pcx);
        }
        o.println(";;; condition completes");
    }
}
