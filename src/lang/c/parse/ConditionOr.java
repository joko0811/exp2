package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class ConditionOr extends CParseRule{
    // conditionOr ::= OR conditionSimple
    private CParseRule condition;
    private CToken op;

    public ConditionOr(){}
    public static boolean isFirst(CToken tk) {
        return tk.getType()==CToken.TK_OR;
    }

    public void parse(CParseContext pcx) throws FatalErrorException {

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {

    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {

    }
}
