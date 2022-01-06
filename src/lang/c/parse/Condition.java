package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class Condition extends CParseRule {
    // condition ::= conditionSimple [conditionNot | conditionAnd | conditionOr]
    private CParseRule conditionSimple,condition;

    public Condition(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return ConditionSimple.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        conditionSimple = new ConditionSimple(pcx);
        CToken tk = ct.getCurrentToken(pcx);

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
