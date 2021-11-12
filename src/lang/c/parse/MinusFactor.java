package lang.c.parse;
import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class MinusFactor extends CParseRule {
    // minusFactor ::= MINUS unsignedFactor
	private CToken op;
	private CParseRule unsignedFactor;
	public MinusFactor(CParseContext pcx) {}
	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_MINUS;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		// ここにやってくるときは、必ずisFirst()が満たされている
		CTokenizer ct = pcx.getTokenizer();
		op = ct.getCurrentToken(pcx);
		CToken tk = ct.getNextToken(pcx);
		if (UnsignedFactor.isFirst(tk)) {
			unsignedFactor = new UnsignedFactor(pcx);
			unsignedFactor.parse(pcx);
		}
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (unsignedFactor != null) {
			unsignedFactor.semanticCheck(pcx);
			setCType(unsignedFactor.getCType());		// unsignedFactor の型をそのままコピー
			setConstant(unsignedFactor.isConstant());	// unsignedFactor は常に定数
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; minus factor starts");
		if (unsignedFactor != null) {
			unsignedFactor.codeGen(pcx);
			o.println("\tMOV\t-(R6), R0\t; MinusFactor: 二の補数をとる<" + op.toString() + ">");
			o.println("\tXOR\t#0xFFFF, R0\t; MinusFactor:");
			o.println("\tADD\t#1, R0\t; MinusFactor:");
			o.println("\tMOV\tR0, (R6)+\t; MinusFactor:");
		}
		o.println(";;; minus factor completes");
	}
}