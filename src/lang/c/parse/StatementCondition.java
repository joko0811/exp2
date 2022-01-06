package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class StatementCondition extends CParseRule {
    // statementCondition ::= LPAR condition RPAR
    private CParseRule condition;
    private CToken lpar, rpar;

    public StatementCondition(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType()== CToken.TK_LPAR;
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        lpar = ct.getCurrentToken(pcx);

        CToken tk = ct.getNextToken(pcx);
        if(Condition.isFirst(tk)){
            condition = new Condition(pcx);
            condition.parse(pcx);

            tk = ct.getCurrentToken(pcx);
            if(tk.getType()==CToken.TK_RPAR){
                rpar = tk;
                tk = ct.getNextToken(pcx);
            }else{
                pcx.fatalError(tk.toExplainString()+"条件の後に\")\"がありません");
            }
        }else{
            pcx.fatalError(tk.toExplainString()+"\"(\"の後に条件がありません");
        }

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(condition !=null){
            condition.semanticCheck(pcx);
            this.setCType(condition.getCType());
            this.setConstant(condition.isConstant());
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; statementCondition starts");
        if(condition !=null){
            condition.codeGen(pcx);
        }
        o.println(";;; statementCondition completes");
    }
}
