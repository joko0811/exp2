package lang.c.parse;
import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class UnsignedFactor extends CParseRule {
	// unsignedFactor ::= factorAmp | number | LPAR expression RPAR
	private CToken lPar, rPar;
	private CParseRule number;

	public UnsignedFactor(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return (FactorAmp.isFirst(tk)||Number.isFirst(tk)||tk.getType() == CToken.TK_LPAR);
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		// ここにやってくるときは、必ずisFirst()が満たされている
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		if (FactorAmp.isFirst(tk)) {
			number = new FactorAmp(pcx);
			number.parse(pcx);
		}else if(Number.isFirst(tk)){
			number = new Number(pcx);
			number.parse(pcx);
		}else{ // LPAR expression RPAR
			lPar = tk;
			tk = ct.getNextToken(pcx);
			if(Expression.isFirst(tk)){
				number = new Expression(pcx);
				number.parse(pcx);
				tk = ct.getCurrentToken(pcx);
				if(tk.getType()==CToken.TK_RPAR){
					rPar =tk;
					tk = ct.getNextToken(pcx);
				}else{
					pcx.fatalError(tk.toExplainString() + "(の後ろに)がありません");
				}
			}
		}
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (number != null) {
			number.semanticCheck(pcx);
			setCType(number.getCType());		// number の型をそのままコピー
			setConstant(number.isConstant());	// number は常に定数
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; unsigned factor starts");
		if (number != null) { number.codeGen(pcx); }
		o.println(";;; unsigned factor completes");
	}
}