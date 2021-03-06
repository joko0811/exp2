package lang.c.parse;

import java.util.ArrayList;
import java.io.PrintStream;
import lang.*;
import lang.c.*;

public class Program extends CParseRule {
	// program ::= { statement } EOF
    private ArrayList<CParseRule> statementList;

	public Program(CParseContext pcx) {
		statementList =new ArrayList<CParseRule>();
	}
	public static boolean isFirst(CToken tk) {
		return Statement.isFirst(tk);
	}
	public void parse(CParseContext pcx) throws FatalErrorException {
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		while(Statement.isFirst(tk)){
			CParseRule statement = new Statement(pcx);
			statement.parse(pcx);
			statementList.add(statement);
			tk=ct.getCurrentToken(pcx);
		}
		if (tk.getType() != CToken.TK_EOF) {
			pcx.fatalError(tk.toExplainString() + "プログラムの最後にゴミがあります");
		}
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
	    for(int i=0;i<statementList.size();i++){
	        statementList.get(i).semanticCheck(pcx);
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		PrintStream o = pcx.getIOContext().getOutStream();
		o.println(";;; program starts");
		o.println("\t. = 0x100");
		o.println("\tJMP\t__START\t; ProgramNode: 最初の実行文へ");
		// ここには将来、宣言に対するコード生成が必要
		if (statementList != null) {
			o.println("__START:");
			o.println("\tMOV\t#0x1000, R6\t; ProgramNode: 計算用スタック初期化");
			for(int i=0;i<statementList.size();i++){
				Statement statement = (Statement) statementList.get(i);
				statement.codeGen(pcx);
			}
		}
		o.println("\tHLT\t\t\t; ProgramNode:");
		o.println("\t.END\t\t\t; ProgramNode:");
		o.println(";;; program completes");
	}
}
