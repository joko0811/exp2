package lang.c.parse;

import lang.*;
import lang.c.*;

public class ConditionFactor extends CParseRule{
    // conditionFactor ::= TRUE | FALSE | expression | LPAR condition RPAR
    private CParseRule conditionFactor;
    private CToken lpar,rpar;

    private boolean flag;

    public ConditionFactor(CParseContext pcx){}
    public static boolean isFirst(CToken tk) {
        return (Expression.isFirst(tk)||tk.getType()==CToken.TK_LPAR||tk.getType()==CToken.TK_TRUE||tk.getType()==CToken.TK_FALSE);
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();
        CToken tk = ct.getCurrentToken(pcx);

        if(tk.getType()==CToken.TK_LPAR){
            lpar = tk;
            tk = ct.getNextToken(pcx);
            if(Condition.isFirst(tk)){
                conditionFactor = new Condition(pcx);
                conditionFactor.parse(pcx);
                tk = ct.getCurrentToken(pcx);
                if(tk.getType()==CToken.TK_RPAR){
                   rpar = tk;
                   tk = ct.getNextToken(pcx);
                }else{
                    pcx.fatalError(tk.toExplainString()+"括弧が閉じていません");
                }
            }else{
                pcx.fatalError(tk.toExplainString()+"\"(\"の後はconditionです");
            }
        }else if(Expression.isFirst(tk)){
            conditionFactor = new Expression(pcx);
            conditionFactor.parse(pcx);
        }else if(tk.getType()==CToken.TK_TRUE){
            flag=true;
            tk=ct.getNextToken(pcx);
        }else if(tk.getType()==CToken.TK_FALSE){
            flag=false;
            tk=ct.getNextToken(pcx);
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {

    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {

    }
}
