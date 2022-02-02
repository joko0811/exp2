package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class ConditionStatement extends CParseRule {
    // conditionStatement ::= conditionExpression {conditionOr}
    private CParseRule condition;

    public ConditionStatement(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return ConditionExpression.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CParseRule conditionExpression=null, list=null;
        conditionExpression = new ConditionTerm(pcx);
        conditionExpression.parse(pcx);
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        while(ConditionAnd.isFirst(tk)||ConditionOr.isFirst(tk)){
            if(ConditionAnd.isFirst(tk)){
                list = new ConditionAnd(pcx,conditionExpression);
                list.parse(pcx);
                conditionExpression = list;
                tk=ct.getCurrentToken(pcx);
            }else if(ConditionOr.isFirst(tk)){
                list = new ConditionOr(pcx,conditionExpression);
                list.parse(pcx);
                conditionExpression = list;
                tk=ct.getCurrentToken(pcx);
            }
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
        o.println(";;; conditionStatement starts");
        if (condition != null) {
            condition.codeGen(pcx);
        }
        o.println(";;; conditionStatement completes");
    }
}
