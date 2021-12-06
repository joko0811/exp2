package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class FactorAmp extends CParseRule {
	// factorAmp ::= AMP ( number | primary )
	private CToken amp;
	private CParseRule number,primary;
	public FactorAmp(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_AMP;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		amp = tk;
		tk = ct.getNextToken(pcx);
		if(Number.isFirst(tk)){
			number = new Number(pcx);
			number.parse(pcx);
		}else if(Primary.isFirst(tk)){
			primary=new Primary(pcx);
			primary.parse(pcx);
		}else{
			pcx.fatalError(tk.toExplainString() + "&の後ろはnumber又はprimaryです");
		}
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (number != null) {
			number.semanticCheck(pcx);
			setCType(CType.getCType(CType.T_pint));
			setConstant(number.isConstant());
		}else if(primary!=null){
			primary.semanticCheck(pcx);
			if(((Primary)primary).getPrimaryMult() instanceof PrimaryMult){
				pcx.fatalError("&*varのような記述は禁止されています");
			}else if(primary.getCType().isCType(CType.T_pint)){
				pcx.fatalError("ポインタのポインタは禁止されています");
			}else{
				setCType(CType.getCType(CType.T_pint));
				setConstant(primary.isConstant());
			}
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; factorAmp starts");
		if (number != null) {
			number.codeGen(pcx);
		} else if (primary != null) {
			primary.codeGen(pcx);
		}
		o.println(";;; factorAmp completes");
	}
}