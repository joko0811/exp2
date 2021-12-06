package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class Primary extends CParseRule {
    // primary ::= primaryMult | variable
    private CParseRule variable,primaryMult;
    public CParseRule getPrimaryMult(){return primaryMult;}

    public Primary(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return (PrimaryMult.isFirst(tk)||Variable.isFirst(tk));
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        if(PrimaryMult.isFirst(tk)){
            primaryMult=new PrimaryMult(pcx);
            primaryMult.parse(pcx);
        }else if(Variable.isFirst(tk)){
            variable=new Variable(pcx);
            variable.parse(pcx);
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(variable!=null){
            variable.semanticCheck(pcx);
            this.setCType(variable.getCType());
            this.setConstant(variable.isConstant());
        }else if(primaryMult!=null){
            primaryMult.semanticCheck(pcx);
            this.setCType(primaryMult.getCType());
            this.setConstant(primaryMult.isConstant());
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; primary starts");
        if (variable != null) {
            variable.codeGen(pcx);
        }else if(primaryMult!=null){
            primaryMult.codeGen(pcx);
        }
        o.println(";;; primary completes");
    }
}
