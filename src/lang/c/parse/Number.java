package lang.c.parse;

import java.io.PrintStream;

import lang.*;
import lang.c.*;

public class Number extends CParseRule {
	// number ::= NUM
	private CToken num;
	public Number(CParseContext pcx) {
	}
	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_NUM;
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		num = tk;
		tk = ct.getNextToken(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		String number = num.getText();
		if(number.charAt(0)=='0'){
			if(number.charAt(1)=='x'){
				int hex = Integer.decode(number);
				if(hex<0 || hex>0xffff) {
					pcx.fatalError(num.toExplainString()+"表現できる範囲外の値です");
				}
			}else{
				int octal=Integer.decode(number);
				if(octal< 0 || octal>0177777) {
					pcx.fatalError(num.toExplainString()+"表現できる範囲外の値です");
				}
			}
		}else{
			int decimal=Integer.parseInt(number);
			if(decimal<-32768||decimal>32767) {
				pcx.fatalError(num.toExplainString()+"表現できる範囲外の値です");
			}
		}
		this.setCType(CType.getCType(CType.T_int));
		this.setConstant(true);
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; number starts");
		if (num != null) {
			o.println("\tMOV\t#" + num.getText() + ", (R6)+\t; Number: 数を積む<" + num.toExplainString() + ">");
		}
		o.println(";;; number completes");
	}
}
