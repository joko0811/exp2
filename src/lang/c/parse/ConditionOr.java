package lang.c.parse;

import lang.*;
import lang.c.*;

public class ConditionOr extends CParseRule{
    // conditionOr ::= OR conditionSimple
    private CParseRule left,right;
    private CToken op;

    public ConditionOr(CParseContext pcx,CParseRule left){
        this.left=left;
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType()==CToken.TK_OR;
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);
        if(ConditionTerm.isFirst(tk)){
            right = new ConditionTerm(pcx);
            right.parse(pcx);
        }else{
            pcx.fatalError(tk.toExplainString()+"\"||\"の後はconditionです");
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {

    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {

    }
}
