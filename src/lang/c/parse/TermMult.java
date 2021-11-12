package lang.c.parse;

import lang.FatalErrorException;
import lang.c.*;

import java.io.PrintStream;

public class TermMult extends CParseRule {
	// term ::= factor
    private CToken op;
	private CParseRule right,left;
	public TermMult(CParseContext pcx, CParseRule left) {
		this.left = left;
	}
	public static boolean isFirst(CToken tk) {
		return tk.getType()==CToken.TK_MULT;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		// ここにやってくるときは、必ずisFirst()が満たされている
		CTokenizer ct = pcx.getTokenizer();
		op = ct.getCurrentToken(pcx);
		// *の次の字句を読む
		CToken tk = ct.getNextToken(pcx);
		if(Factor.isFirst(tk)){
			right = new Factor(pcx);
			right.parse(pcx);
		} else {
			pcx.fatalError(tk.toExplainString() + "*の後ろはfactorです");
		}
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		final int s[][] = {
				//		T_err			T_int			T_pint
				{	CType.T_err,	CType.T_err,	CType.T_err  },	// T_err
				{	CType.T_err,	CType.T_int,	CType.T_err },	// T_int
				{	CType.T_err,	CType.T_err,	CType.T_err },	// T_pint
		};
		if (left != null && right != null) {
			left.semanticCheck(pcx);
			right.semanticCheck(pcx);
			int lt = left.getCType().getType();		// *の左辺の型
			int rt = right.getCType().getType();	// *の右辺の型
			int nt = s[lt][rt];						// 規則による型計算
			if (nt == CType.T_err) {
				pcx.fatalError(op.toExplainString() + "左辺の型[" + left.getCType().toString() + "]と右辺の型[" + right.getCType().toString() + "]はかけることができません");
			}
			this.setCType(CType.getCType(nt));		// factor の型をそのままコピー
			this.setConstant(left.isConstant()&&right.isConstant());
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		PrintStream o = pcx.getIOContext().getOutStream();
		if (left != null && right != null) {
			left.codeGen(pcx);		// 左部分木のコード生成を頼む
			right.codeGen(pcx);		// 右部分木のコード生成を頼む
			o.println("\tJSR\tMUL\t; TermMult: MULサブルーチンを呼び出し、引数除去・結果の取り出しを行う<" + op.toString() + ">");
			o.println("\tSUB\t#2, R6\t; TermMult: 引数除去");
			o.println("\tMOV\tR1, (R6)+\t; TermMult:");
		}
	}
}
