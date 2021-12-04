package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class Variable extends CParseRule {
    // variable ::= ident [ array ]
    private CParseRule ident,array;
    public Variable(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return Ident.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        ident=new Ident(pcx);
        ident.parse(pcx);
        tk=ct.getCurrentToken(pcx);
        if(Array.isFirst(tk)){
           array = new Array(pcx);
           array.parse(pcx);
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; number starts");
        if (array != null) {
        }
        o.println(";;; number completes");
    }
}
