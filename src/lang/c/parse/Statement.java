package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class Statement extends CParseRule {
    // statement ::= statementAssign | statementIfElse | statementWhile | statementInput | statementOutput
    private CParseRule statement;

    public Statement(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return (StatementAssign.isFirst(tk)||StatementIfElse.isFirst(tk)||StatementWhile.isFirst(tk)||StatementInput.isFirst(tk)||StatementOutput.isFirst(tk));
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);
        if(StatementAssign.isFirst(tk)){
            statement=new StatementAssign(pcx);
        }else if(StatementIfElse.isFirst(tk)){
            statement=new StatementIfElse(pcx);
        }else if(StatementWhile.isFirst(tk)) {
            statement=new StatementWhile(pcx);
        }else if(StatementInput.isFirst(tk)){
            statement=new StatementInput(pcx);
        }else if(StatementOutput.isFirst(tk)){
            statement=new StatementOutput(pcx);
        }
        if(statement!=null){
            statement.parse(pcx);
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(statement!=null){
            statement.semanticCheck(pcx);
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; statement starts");
        if(statement!=null){
            statement.codeGen(pcx);
        }
        o.println(";;; statement completes");
    }
}
