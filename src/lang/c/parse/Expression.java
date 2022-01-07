package lang.c.parse;

import java.io.PrintStream;
import java.util.ArrayList;

import lang.*;
import lang.c.*;

public class Expression extends CParseRule {
	// expression ::= term { expressionAdd | expressionSub }
	private CParseRule term;
	private ArrayList<CParseRule> expressionList;

	public Expression(CParseContext pcx) {
		expressionList = new ArrayList<CParseRule>();
	}
	public static boolean isFirst(CToken tk) {
		return Term.isFirst(tk);
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		CTokenizer ct = pcx.getTokenizer();

		term = new Term(pcx);
		term.parse(pcx);
		CToken tk = ct.getCurrentToken(pcx);

		while (true) {
			CParseRule list;
			if(ExpressionAdd.isFirst(tk)) {
				list = new ExpressionAdd(pcx, term);
				list.parse(pcx);
				expressionList.add(list);
			}else if(ExpressionSub.isFirst(tk)) {
				list = new ExpressionSub(pcx, term);
				list.parse(pcx);
				expressionList.add(list);
			}else {
				break;
			}
			tk = ct.getCurrentToken(pcx);
		}
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (term != null) {
			term.semanticCheck(pcx);
			this.setCType(term.getCType());		// expression の型をそのままコピー
			this.setConstant(term.isConstant());
			for(int i = 0; i< expressionList.size(); i++){
				expressionList.get(i).semanticCheck(pcx);
			}
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; expression starts");
		if (term != null){
			term.codeGen(pcx);
			for(int i = 0; i< expressionList.size(); i++){
				expressionList.get(i).semanticCheck(pcx);
			}
		}
		o.println(";;; expression completes");
	}
}