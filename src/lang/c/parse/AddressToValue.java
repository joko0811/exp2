package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class AddressToValue extends CParseRule {
    // addressToValue ::= primary
    private CParseRule primary;
    public AddressToValue(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return Primary.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        primary=new Primary(pcx);
        primary.parse(pcx);
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(primary!=null){
            primary.semanticCheck(pcx);
            this.setCType(primary.getCType());
            this.setConstant(primary.isConstant());
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; addressToValue starts");
        if (primary != null) {
            primary.codeGen(pcx);
            o.println("\tMOV\t-(R6), R0\t; AddressToValue: 積んだ変数アドレスを取り出し、参照先の値を取り出し、積む");
            o.println("\tMOV\t(R0), (R6)+\t; AddressToValue: ");
        }
        o.println(";;; addressToValue completes");
    }
}
