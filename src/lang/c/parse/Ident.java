package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class Ident extends CParseRule{
    // ident ::= IDENT （注)’_’ も英字として扱うこと
    private CToken id;

    public Ident(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType() == CToken.TK_IDENT;
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        id = tk;
        tk = ct.getNextToken(pcx);
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; ident starts");
        if (id != null) {
            o.println("\tMOV\t#" + id.getText() + ", (R6)+\t; Ident: 変数アドレスを積む<"
                    + id.toExplainString() + ">");
        }
        o.println(";;; ident completes");
    }
}
