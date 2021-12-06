package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class Statement extends CParseRule {
    // statement ::= statementAssign
    private CParseRule statementAssign;

    public Statement(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return StatementAssign.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        statementAssign=new StatementAssign(pcx);
        statementAssign.parse(pcx);
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; program starts");
        o.println(";;; program completes");
    }
}
