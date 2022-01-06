package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class Condition extends CParseRule {
    // condition ::= conditionNot | unsignedCondition
    private CParseRule condition;

    public Condition(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return (ConditionNot.isFirst(tk)||UnsignedCondition.isFirst(tk));
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);

        if(ConditionNot.isFirst(tk)){
            condition = new ConditionNot(pcx);
            condition.parse(pcx);
        }else if(UnsignedCondition.isFirst(tk)){
            condition = new UnsignedCondition(pcx);
            condition.parse(pcx);
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(condition!=null){
            condition.semanticCheck(pcx);
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
