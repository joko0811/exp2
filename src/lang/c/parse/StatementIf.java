package lang.c.parse;

import java.io.PrintStream;
import java.util.ArrayList;
import lang.*;
import lang.c.*;

public class StatementIf extends CParseRule {
    // statementIf ::= IF statementCondition statementExecution
    private CParseRule statementCondition,statementExecution;
    private CToken tkIf;

    private int seq,tag;
    public void setSeqTag(int seq, int tag){
        this.seq=seq;this.tag=tag;
    }

    public StatementIf(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType()== CToken.TK_IF;
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        tkIf = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);

        if(StatementCondition.isFirst(tk)){
            statementCondition = new StatementCondition(pcx);
            statementCondition.parse(pcx);
            tk = ct.getCurrentToken(pcx);

            if(StatementExecution.isFirst(tk)){
                statementExecution = new StatementExecution(pcx);
                statementExecution.parse(pcx);
                tk = ct.getCurrentToken(pcx);

            }else{
                pcx.fatalError(tk.toExplainString()+"ifの実行文がありません");
            }
        }else{
            pcx.fatalError(tk.toExplainString()+"ifの条件文がありません");
        }

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(statementCondition!=null&&statementExecution!=null){
            statementCondition.semanticCheck(pcx);
            statementExecution.semanticCheck(pcx);
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; statementIf starts");
        if(statementCondition!=null){
            statementCondition.codeGen(pcx);
            o.println("ELSE" + seq + "_" + tag + ":\t\t\t\t; statementIf:");
            o.println("\tMOV\t-(R6), R0\t; statementIf: スタックから条件文の真偽を取り出し、真ならif文の処理、偽ならelse文の処理をする");
            o.println("\tBRZ\tELSE" + seq + "_" + (tag+1) + "\t\t\t\t; statementIf:");
            if(statementExecution!=null){
                statementExecution.codeGen(pcx);
            }
        }
        o.println(";;; statementIf completes");
    }

}
