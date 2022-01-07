package lang.c.parse;

import lang.*;
import lang.c.*;

public class ConditionAnd extends CParseRule{
    // conditionAnd ::= AND conditionSimple
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
        if(ConditionTerm.isFirst(tk)){
            right = new ConditionTerm(pcx);
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

    }
}
