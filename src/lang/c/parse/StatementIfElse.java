package lang.c.parse;

import java.io.PrintStream;
import java.util.ArrayList;
import lang.*;
import lang.c.*;

public class StatementIfElse extends CParseRule {
    // statementIfElse ::= statementIf [ [ { ELSE statementIf } ] [ ELSE statementExecution ] ]
    private CParseRule statementIf,statementExecution;
    private ArrayList<CParseRule> statementIfs;
    private ArrayList<CToken> tkElse;

    private int seq;

    public StatementIfElse(CParseContext pcx) {
        statementIfs = new ArrayList<CParseRule>();
        tkElse = new ArrayList<CToken>();
    }
    public static boolean isFirst(CToken tk) {
        return StatementIf.isFirst(tk);
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        boolean elseflag=false;

        CTokenizer ct = pcx.getTokenizer();
        CToken tk;
        statementIf = new StatementIf(pcx);
        statementIf.parse(pcx);

        tk = ct.getCurrentToken(pcx);
        while(tk.getType()==CToken.TK_ELSE){
            tkElse.add(tk);
            tk = ct.getNextToken(pcx);

            if(StatementIf.isFirst(tk)){
                CParseRule tmpStatementIf = new StatementIf(pcx);
                tmpStatementIf.parse(pcx);
                statementIfs.add(tmpStatementIf);
                tk = ct.getCurrentToken(pcx);
            }else if(StatementExecution.isFirst(tk)){
                if(!elseflag){
                    statementExecution = new StatementExecution(pcx);
                    statementExecution.parse(pcx);
                    tk = ct.getCurrentToken(pcx);
                    elseflag = true;
                }else{
                    pcx.warning(tk.toExplainString()+"if構文中に一つ以上のelse句は記述できないためスキップしました");
                }
            }else{
                pcx.recoverableError(tk.toExplainString()+"elseの後に来る語句が不正です");
                break;
            }

        }

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(statementIf!=null){
            statementIf.semanticCheck(pcx);
            if(!statementIfs.isEmpty()){
                for(int i=0;i<statementIfs.size();i++){
                    statementIfs.get(i).semanticCheck(pcx);
                }
            }
            if(statementExecution!=null){
                statementExecution.semanticCheck(pcx);
            }
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; statementIfElse starts");
        if(statementIf!=null){
            int tagnum=0;
            seq = pcx.getSeqId();
            o.println("IF" + seq + ":\t\t\t\t; statementIf:");

            StatementIf firstStatementIf = (StatementIf)statementIf;
            firstStatementIf.setSeqTag(seq,tagnum);
            firstStatementIf.codeGen(pcx);
            o.println("\tJMP\tENDIF" + seq + "\t\t; statementIfElse:");

            if(!statementIfs.isEmpty()){
                for(int i=0;i<statementIfs.size();i++){
                    tagnum++;
                    StatementIf tmpStatementIf=(StatementIf)(statementIfs.get(i));
                    tmpStatementIf.setSeqTag(seq,tagnum);
                    tmpStatementIf.codeGen(pcx);
                    o.println("\tJMP\tENDIF" + seq + "\t\t; statementIfElse:");
                }
            }

            tagnum++;
            o.println("ELSE" + seq + "_" + tagnum + ":\t\t\t\t; statementIfElse:");
            if(statementExecution!=null){
                statementExecution.codeGen(pcx);
            }
            o.println("ENDIF" + seq + ":\t\t\t\t; statementIfElse:");
        }
        o.println(";;; statementIfElse completes");
    }
}
