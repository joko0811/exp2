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
        if (variable != null) {
            variable.codeGen(pcx);
            o.println("\tMOV\t-(R6), R0\t; PrimaryMult: アドレスを取り出して、内容を参照して、積む<"
                    + mul.toExplainString() + ">");
            o.println("\tMOV\t(R0), (R6)+\t; PrimaryMult:");
        }
    }
}
