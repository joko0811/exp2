package lang.c.parse;

import lang.*;
import lang.c.*;

public class ConditionAnd extends CParseRule{
    // conditionAnd ::= AND conditionSimple
    private CParseRule left,right;
    private CToken op;

    public ConditionAnd(CParseRule left){
        this.left=left;
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType()==CToken.TK_AND;
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);
        if(ConditionSimple.isFirst(tk)){
            right = new ConditionSimple(pcx);
            right.parse(pcx);
        }else{
            pcx.fatalError(tk.toExplainString()+"\"&&\"の後はconditionです");
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {

    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {

    }
}
