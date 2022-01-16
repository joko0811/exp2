package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class StatementInput extends CParseRule {
    // statementInput ::= INPUT primary SEMI
    private CParseRule primary;
    private CToken op,semi;

    public StatementInput(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType()== CToken.TK_INPUT;
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        op = ct.getCurrentToken(pcx);

        CToken tk = ct.getNextToken(pcx);
        if(Primary.isFirst(tk)){
            try{
                primary = new Primary(pcx);
                primary.parse(pcx);
            }catch(RecoverableErrorException e){
                tk = ct.skipTo(pcx,CToken.TK_SEMI);
                pcx.info(tk.toExplainString()+tk.toString()+"まで構文解析をスキップしました");
            }

            tk = ct.getCurrentToken(pcx);
            if(tk.getType()==CToken.TK_SEMI){
                semi=tk;
                tk=ct.getNextToken(pcx);
            }else{
               pcx.warning(tk.toExplainString()+"inputの行に\";\"がないため補いました");
            }
        }else{
            pcx.recoverableError(tk.toExplainString()+"inputの後にprimaryがありません");
        }

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(primary!=null){
            primary.semanticCheck(pcx);
            this.setCType(primary.getCType());
            if(primary.isConstant() == true) {
                pcx.warning("定数は読み込めません");
            } else {
                setConstant(primary.isConstant());
            }
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; statementInput starts");
        if(primary!=null){
            primary.codeGen(pcx);
            o.println("\tMOV\t-(R6), R0\t; statementInput: primaryに#FFE0の値を入力する");
            o.println("\tMOV\t#0xFFE0, R1\t; statementInput:");
            o.println("\tMOV\t(R1), (R0)\t; statementInput:");
        }
        o.println(";;; statementInput completes");
    }
}
