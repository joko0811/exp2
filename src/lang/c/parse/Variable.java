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
        if(ident != null){
            ident.semanticCheck(pcx);
            if(array != null){
                array.semanticCheck(pcx);
                if(ident.getCType().isCType(CType.T_aint)){
                    this.setCType(CType.getCType(CType.T_int));
                    this.setConstant(ident.isConstant());
                }else if(ident.getCType().isCType(CType.T_paint)){
                    this.setCType(CType.getCType(CType.T_pint));
                    this.setConstant(ident.isConstant());
                }else{
                    pcx.fatalError("配列の識別子が許容されていない型です");
                }
            }else{
                if(ident.getCType().isCType(CType.T_aint)||ident.getCType().isCType(CType.T_paint)){
                   pcx.fatalError("配列に添字が必要です");
                }
                this.setCType(ident.getCType());
                this.setConstant(ident.isConstant());
            }
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; number starts");
        if(ident!=null){
            ident.codeGen(pcx);
        }
        if (array != null) {
            array.codeGen(pcx);
            o.println("\tMOV\t-(R6), R0\t;  variable: 配列の添字を取り出して、identの番地に足し、スタックに積む");
            o.println("\tMOV\t-(R6), R1\t;  variable: ");
            o.println("\tADD\tR1, R0\t\t;   variable: ");
            o.println("\tMOV\tR0, (R6)+\t;  variable: ");
        }
        o.println(";;; number completes");
    }
}
