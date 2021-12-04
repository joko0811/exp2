package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class Primary extends CParseRule {
    // primary ::= primaryMult | variable
    private CParseRule variable;
    public Primary(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return (PrimaryMult.isFirst(tk)||Variable.isFirst(tk));
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        if(PrimaryMult.isFirst(tk)){
            variable=new PrimaryMult(pcx);
            variable.parse(pcx);
        }else if(Variable.isFirst(tk)){
            variable=new Variable(pcx);
            variable.parse(pcx);
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; primary starts");
        if (variable != null) {
        }
        o.println(";;; primary completes");
    }
}
