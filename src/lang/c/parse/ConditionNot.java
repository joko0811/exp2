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

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {

    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {

    }
}