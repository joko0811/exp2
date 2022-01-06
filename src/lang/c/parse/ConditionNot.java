package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class ConditionNot extends CParseRule{
    // conditionNot ::= NOT conditionSimple
    private CParseRule condition;
    private CToken op;

    public ConditionNot(){}
    public static boolean isFirst(CToken tk) {
        return tk.getType()==CToken.TK_NOT;
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);
        if(ConditionSimple.isFirst(tk)){
            condition = new ConditionSimple(pcx);
            condition.parse(pcx);
        }else{
            pcx.fatalError(tk.toExplainString()+"\"!\"の後はconditionです");
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {

    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {

    }
}