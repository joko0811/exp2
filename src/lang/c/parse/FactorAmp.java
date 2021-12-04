package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class FactorAmp extends CParseRule {
	// factorAmp ::= AMP ( number | primary )
	private CToken amp;
	private CParseRule number;
	public FactorAmp(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return (tk.getType() == CToken.TK_AMP || Primary.isFirst(tk));
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		if(tk.getType()==CToken.TK_AMP){
			amp = tk;
			tk = ct.getNextToken(pcx);
			if(Number.isFirst(tk)){
				number = new Number(pcx);
				number.parse(pcx);
			}else{
				pcx.fatalError(tk.toExplainString() + "&の後ろはnumberです");
			}
		}else if(Primary.isFirst(tk)){
			number=new Primary(pcx);
			number.parse(pcx);
		}
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (number != null) {
			number.semanticCheck(pcx);
			setCType(CType.getCType(CType.T_pint));
			setConstant(number.isConstant());	// number は常に定数
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; factorAmp starts");
		if (number != null) {
			number.codeGen(pcx);
		}
		o.println(";;; factorAmp completes");
	}
}