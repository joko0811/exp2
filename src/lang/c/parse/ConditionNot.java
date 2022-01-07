package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class ConditionNot extends CParseRule{
    // conditionNot ::= NOT unsignedCondition
    private CParseRule condition;
    private CToken op;

    public ConditionNot(CParseContext pcx){}
    public static boolean isFirst(CToken tk) {
        return tk.getType()==CToken.TK_NOT;
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);
        if(UnsignedCondition.isFirst(tk)){
            condition = new UnsignedCondition(pcx);
            condition.parse(pcx);
        }else{
            pcx.fatalError(tk.toExplainString()+"\"!\"の後はunsignedConditionです");
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(condition!=null){
            condition.semanticCheck(pcx);
            if(condition.getCType().getType()==CType.T_bool){
                setCType(condition.getCType());
                setConstant(condition.isConstant());
            }else{
                pcx.fatalError(condition.toString()+"\"!\"の右辺はbool値である必要があります");
            }
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        if (condition != null) {
            condition.codeGen(pcx);
            o.println("\tMOV\t-(R6), R0\t; ExpressionNot: 数を取り出して、否定をとり、積む<" + op.toString() + ">");
            o.println("\tXOR\t#0x0001, R0\t; ExpressionNot:");
            o.println("\tMOV\tR0, (R6)+\t; ExpressionNot:");
        }
    }
}