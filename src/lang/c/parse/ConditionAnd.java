package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class ConditionAnd extends CParseRule{
    // conditionAnd ::= AND conditionSimple
    private CParseRule condition;
    private CToken op;

    public ConditionAnd(){}
    public static boolean isFirst(CToken tk) {
        return tk.getType()==CToken.TK_AND;
    }

    public void parse(CParseContext pcx) throws FatalErrorException {

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {

    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {

    }
}
