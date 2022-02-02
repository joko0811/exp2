package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class Condition extends CParseRule {
    // condition ::= conditionStatement {conditionOr}
    private CParseRule condition;

    public Condition(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return ConditionStatement.isFirst(tk);
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CParseRule conditionStatement=null, list=null;

        conditionStatement = new ConditionStatement(pcx);
        conditionStatement.parse(pcx);

        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);

        while(ConditionOr.isFirst(tk)){
            if(ConditionOr.isFirst(tk)){
                list = new ConditionOr(pcx,conditionStatement);
                list.parse(pcx);
                conditionStatement = list;
                tk=ct.getCurrentToken(pcx);
            }
        }
        condition = conditionStatement;
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
