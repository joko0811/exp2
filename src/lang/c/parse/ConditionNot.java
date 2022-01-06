package lang.c.parse;

import lang.*;
import lang.c.*;

public class ConditionNot extends CParseRule{
    // conditionNot ::= NOT unsignedCondition
    private CParseRule condition;
    private CToken op;

    public ConditionNot(CParseContext pcx){}
    public static boolean isFirst(CToken tk) {
        return tk.getType()==CToken.TK_NOT;
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);
        if(UnsignedCondition.isFirst(tk)){
            condition = new UnsignedCondition(pcx);
            condition.parse(pcx);
        }else{
            pcx.fatalError(tk.toExplainString()+"\"!\"の後はunsignedConditionです");
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {

    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {

    }
}