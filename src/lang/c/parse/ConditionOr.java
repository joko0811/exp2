package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class ConditionOr extends CParseRule{
    // conditionOr ::= OR conditionSimple
    private CParseRule left,right;
    private CToken op;

    public ConditionOr(CParseContext pcx,CParseRule left){
        this.left=left;
    }
    public static boolean isFirst(CToken tk) {
        return tk.getType()==CToken.TK_OR;
    }

    public void parse(CParseContext pcx) throws FatalErrorException {
        CTokenizer ct = pcx.getTokenizer();

        op = ct.getCurrentToken(pcx);
        CToken tk = ct.getNextToken(pcx);
        if(ConditionTerm.isFirst(tk)){
            right = new ConditionTerm(pcx);
            right.parse(pcx);
        }else{
            pcx.fatalError(tk.toExplainString()+"\"||\"の後はconditionです");
        }
    }

    public void semanticCheck(CParseContext pcx) throws FatalErrorException {
        if(left!=null&&right!=null){
            left.semanticCheck(pcx);
            right.semanticCheck(pcx);
            setCType(right.getCType());
            setConstant(right.isConstant());
            if(left.getCType().getType()!=CType.T_bool){
                pcx.fatalError(left.toString()+"orの左辺がbool値ではありません");
            }
            if(right.getCType().getType()!=CType.T_bool){
                pcx.fatalError(right.toString()+"orの右辺がbool値ではありません");
            }
        }
    }

    public void codeGen(CParseContext pcx) throws FatalErrorException {
        PrintStream o = pcx.getIOContext().getOutStream();
        if (left != null && right != null) {
            left.codeGen(pcx);		// 左部分木のコード生成を頼む
            right.codeGen(pcx);		// 右部分木のコード生成を頼む
            o.println("\tMOV\t-(R6), R0\t; ConditionOr: ２数を取り出して、論理和をとり、積む<" + op.toString() + ">");
            o.println("\tMOV\t-(R6), R1\t; ConditionOr:");
            o.println("\tOR\tR1, R0\t; ConditionOr:");
            o.println("\tMOV\tR0, (R6)+\t; ConditionOr:");
        }
    }
}
