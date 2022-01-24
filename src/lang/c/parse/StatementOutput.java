package lang.c.parse;

import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class StatementOutput extends CParseRule {
    // statementOutput ::= OUTPUT expression SEMI
    private CParseRule expression;
    private CToken op,semi;

    public StatementOutput(CParseContext pcx) {
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType()== CToken.TK_OUTPUT;
    }
    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        op = ct.getCurrentToken(pcx);

        CToken tk = ct.getNextToken(pcx);
        if(Expression.isFirst(tk)){
            expression = new Expression(pcx);
            expression.parse(pcx);

            tk = ct.getCurrentToken(pcx);
            if(tk.getType()==CToken.TK_SEMI){
                semi=tk;
                tk=ct.getNextToken(pcx);
            }else{
                pcx.fatalError(tk.toExplainString()+"outputの行にセミコロンがありません");
            }
        }else{
            pcx.fatalError(tk.toExplainString()+"outputの後にexpressionがありません");
        }

    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(expression!=null){
            expression.semanticCheck(pcx);
            this.setCType(expression.getCType());
            this.setConstant(expression.isConstant());
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        o.println(";;; statementOutput starts");
        if(expression!=null){
            expression.codeGen(pcx);
            o.println("\tMOV\t-(R6), R0\t; statementOutput: expressionを#FFE0に出力する");
            o.println("\tMOV\t#0xFFE0, R1\t; statementOutput:");
            o.println("\tMOV\tR0, (R1)\t; statementOutput:");
        }
        o.println(";;; statementOutput completes");
    }
}
