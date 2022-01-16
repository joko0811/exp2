package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class StatementWhile extends CParseRule {
    //  statementWhile ::= WHILE statementCondition statementExecution
    private CParseRule statementCondition,statementExecution;
    private CToken op_while;

    private int seq;

    public StatementWhile(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType()== CToken.TK_WHILE;
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        op_while = ct.getCurrentToken(pcx);

        CToken tk = ct.getNextToken(pcx);
        if(StatementCondition.isFirst(tk)){
            statementCondition = new StatementCondition(pcx);
            statementCondition.parse(pcx);

            tk = ct.getCurrentToken(pcx);
            if(StatementExecution.isFirst(tk)){
                statementExecution = new StatementExecution(pcx);
                statementExecution.parse(pcx);
            }else{
                pcx.recoverableError(tk.toExplainString()+"whileの実行文がありません");
            }
        }else{
            pcx.recoverableError(tk.toExplainString()+"whileの条件文がありません");
        }

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(statementCondition!=null||statementExecution!=null){
            statementCondition.semanticCheck(pcx);
            statementExecution.semanticCheck(pcx);
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; statementWhile starts");
        if(statementCondition!=null||statementExecution!=null){
            seq = pcx.getSeqId();
            o.println("WHILE" + seq + ":\t\t\t; statementWhile:");
            statementCondition.codeGen(pcx);
            o.println("\tMOV\t-(R6), R0\t; statementWhile: スタックから条件文の真偽を取り出し、真のあいだ処理を行う");
            o.println("\tBRZ\tENDWHILE" + seq + "\t; statementWhile:");
            statementExecution.codeGen(pcx);
            o.println("\tJMP\tWHILE" + seq + "\t\t; statementWhile:");
            o.println("ENDWHILE" + seq + ":\t\t\t; statementWhile:");
        }
        o.println(";;; statementWhile completes");
    }
}
