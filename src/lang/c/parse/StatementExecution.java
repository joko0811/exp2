package lang.c.parse;

import java.io.PrintStream;
import java.util.ArrayList;
import lang.*;
import lang.c.*;

public class StatementExecution extends CParseRule {
    // statementExecution ::= LCUR [ { statement } ] RCUR
    private ArrayList<CParseRule> statements;
    private CToken lcur, rcur;

    public StatementExecution(CParseContext pcx) {
        statements = new ArrayList<CParseRule>();
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType()== CToken.TK_LCUR;
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        lcur = ct.getCurrentToken(pcx);

        CToken tk = ct.getNextToken(pcx);
        while(Statement.isFirst(tk)){
            CParseRule statement = new Statement(pcx);
            statement.parse(pcx);
            statements.add(statement);

            tk = ct.getCurrentToken(pcx);
        }

        if(tk.getType()==CToken.TK_RCUR){
            rcur =tk;
            tk = ct.getNextToken(pcx);
        }else{
            pcx.fatalError(tk.toExplainString()+"実行文の後に\"}\"がありません");
        }

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        for(int i=0;i<statements.size();i++) {
            CParseRule statement = statements.get(i);
            statement.semanticCheck(pcx);
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; statementExecution starts");
        for(int i=0;i<statements.size();i++) {
            CParseRule statement = statements.get(i);
            statement.codeGen(pcx);
        }
        o.println(";;; statementExecution completes");
    }
}
