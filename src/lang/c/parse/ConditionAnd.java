package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class ConditionAnd extends CParseRule{
    // conditionAnd :: AND conditionExpression
    private CParseRule left,right;
    private CToken op;

    public ConditionAnd(CParseContext pcx,CParseRule left){
        this.left=left;
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType()==CToken.TK_AND;
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);
        if(ConditionExpression.isFirst(tk)){
            right = new ConditionExpression(pcx);
            right.parse(pcx);
        }else{
            pcx.fatalError(tk.toExplainString()+"\"&&\"の後はconditionです");
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(left!=null&&right!=null){
            left.semanticCheck(pcx);
            right.semanticCheck(pcx);
            setCType(right.getCType());
            setConstant(right.isConstant());
            if(left.getCType().getType()!=CType.T_bool){
                pcx.fatalError(left.toString()+"andの左辺がbool値ではありません");
            }
            if(right.getCType().getType()!=CType.T_bool){
                pcx.fatalError(right.toString()+"andの右辺がbool値ではありません");
            }
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        if (left != null && right != null) {
            left.codeGen(pcx);        // 左部分木のコード生成を頼む
            right.codeGen(pcx);        // 右部分木のコード生成を頼む
            o.println("\tMOV\t-(R6), R0\t; ConditionAnd: ２数を取り出して、論理積をとり、積む<" + op.toString() + ">");
            o.println("\tMOV\t-(R6), R1\t; ConditionAnd:");
            o.println("\tAND\tR1, R0\t\t; ConditionAnd:");
            o.println("\tMOV\tR0, (R6)+\t; ConditionAnd:");
        }

    }
}
