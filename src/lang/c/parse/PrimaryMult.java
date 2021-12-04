package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class PrimaryMult extends CParseRule {
    // primaryMult ::= MULT variable
    private CToken mul;
    private CParseRule variable;
    public PrimaryMult(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType() == CToken.TK_MULT;
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        mul=tk;
        tk=ct.getNextToken(pcx);
        if(Variable.isFirst(tk)){
            variable=new Variable(pcx);
            variable.parse(pcx);
        }else{
            pcx.fatalError(tk.toExplainString()+"*の後ろにvariableがありません");
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; primaryMult starts");
        if (variable != null) {
        }
        o.println(";;; primaryMult completes");
    }
}
