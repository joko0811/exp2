package lang.c.parse;

import java.io.PrintStream;
import java.util.ArrayList;

import lang.*;
import lang.c.*;

public class Term extends CParseRule {
	// term ::= factor { termMult | termDiv }
	private CParseRule factor;
	private ArrayList<CParseRule> termList;

	public Term(CParseContext pcx) {
		termList = new ArrayList<CParseRule>();
	}
	public static boolean isFirst(CToken tk) {
		return Factor.isFirst(tk);
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
		CTokenizer ct = pcx.getTokenizer();

		factor = new Term(pcx);
		factor.parse(pcx);
		CToken tk = ct.getCurrentToken(pcx);

		while (true) {
			CParseRule list;
			if(ExpressionAdd.isFirst(tk)) {
				list = new ExpressionAdd(pcx, factor);
				list.parse(pcx);
				termList.add(list);
			}else if(ExpressionSub.isFirst(tk)) {
				list = new ExpressionSub(pcx, factor);
				list.parse(pcx);
				termList.add(list);
			}else {
				break;
			}
			tk = ct.getCurrentToken(pcx);
		}
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (factor != null) {
			factor.semanticCheck(pcx);
			this.setCType(factor.getCType());		// term の型をそのままコピー
			this.setConstant(factor.isConstant());
			for(int i = 0; i< termList.size(); i++){
				termList.get(i).semanticCheck(pcx);
			}
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; term starts");
		if (factor != null){
			factor.codeGen(pcx);
			for(int i = 0; i< termList.size(); i++){
				termList.get(i).semanticCheck(pcx);
			}
		}
		o.println(";;; term completes");
	}
}
