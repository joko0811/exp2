package lang.c;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import lang.*;

public class CTokenizer extends Tokenizer<CToken, CParseContext> {
	@SuppressWarnings("unused")
	private CTokenRule	rule;
	private int			lineNo, colNo;
	private char		backCh;
	private boolean		backChExist = false;

	public CTokenizer(CTokenRule rule) {
		this.rule = rule;
		lineNo = 1; colNo = 1;
	}

	private InputStream in;
	private PrintStream err;

	private char readChar() {
		char ch;
		if (backChExist) {
			ch = backCh;
			backChExist = false;
		} else {
			try {
				ch = (char) in.read();
			} catch (IOException e) {
				e.printStackTrace(err);
				ch = (char) -1;
			}
		}
		++colNo;
		if (ch == '\n')  { colNo = 1; ++lineNo; }
//		System.out.print("'"+ch+"'("+(int)ch+")");
		return ch;
	}
	private void backChar(char c) {
		backCh = c;
		backChExist = true;
		--colNo;
		if (c == '\n') { --lineNo; }
	}

	// 現在読み込まれているトークンを返す
	private CToken currentTk = null;
	public CToken getCurrentToken(CParseContext pctx) {
		return currentTk;
	}
	// 次のトークンを読んで返す
	public CToken getNextToken(CParseContext pctx) {
		in = pctx.getIOContext().getInStream();
		err = pctx.getIOContext().getErrStream();
		currentTk = readToken();
//		System.out.println("Token='" + currentTk.toString());
		return currentTk;
	}
	private CToken readToken() {
		CToken tk = null;
		char ch;
		int  startCol = colNo;
		int  startLine = lineNo;
		StringBuffer text = new StringBuffer();

		int state = 0;
		int subState = 0;
		boolean accept = false;
		while (!accept) {
			switch (state) {
			case 0:					// 初期状態
				ch = readChar();
				if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
				} else if (ch == (char) -1) {	// EOF
					startCol = colNo - 1;
					state = 1;
				} else if (ch >= '0' && ch <= '9') {
					if(ch=='0') {
						text.append(ch);
						ch=readChar();
						if(ch=='x') {	//16進数
							text.append(ch);
							subState=16;
							startCol = colNo-2;
						}else {	//8進数
							backChar(ch);
							subState=8;
							startCol = colNo - 1;
						}
					}else {
						text.append(ch);
						startCol = colNo - 1;
					}
					state = 3;
				} else if (ch == '+') {
					startCol = colNo - 1;
					text.append(ch);
					state = 4;
				}else if (ch == '-'){
					startCol = colNo - 1;
					text.append(ch);
					state = 5;
				}else if (ch == '/') {
					state = 6;
					text.append(ch);
					startCol = colNo - 1;
					startLine = lineNo;
					ch = readChar();
					if(ch =='/') {
						subState=1;
					}else if(ch == '*') {
						subState=2;
					}else {
						subState=0;
					}
					backChar(ch);
				}else if(ch=='&'){
					startCol = colNo - 1;
					text.append(ch);
					state = 7;
				}else if(ch=='*'){
					startCol = colNo - 1;
					text.append(ch);
					state=8;
				}else if(ch=='('){
					startCol = colNo - 1;
					text.append(ch);
					state=9;
				}else if(ch==')'){
					startCol = colNo - 1;
					text.append(ch);
					state=10;
				}else if(ch=='['){
					startCol = colNo -1;
					text.append(ch);
					state=11;
				}else if(ch==']'){
					startCol = colNo -1;
					text.append(ch);
					state=12;
				}else if(Character.isLetter(ch)){
					startCol = colNo -1;
					text.append(ch);
					state=13;
				}else if(ch=='='){
					startCol = colNo -1;
					text.append(ch);
					state=14;
				}else if(ch==';'){
					startCol = colNo -1;
					text.append(ch);
					state=15;
				}else if(ch=='<'){
					startCol = colNo -1;
					text.append(ch);
					state=16;
				}else if(ch=='>'){
					startCol = colNo -1;
					text.append(ch);
					state=17;
				}else if(ch=='!'){
					startCol = colNo -1;
					text.append(ch);
					state=18;
				}else if(ch=='{'){
					startCol = colNo -1;
					text.append(ch);
					state=19;
				}else if(ch=='}'){
					startCol = colNo -1;
					text.append(ch);
					state=20;
				}else{			// ヘンな文字を読んだ
					startCol = colNo - 1;
					text.append(ch);
					state = 2;
				}
				break;
			case 1:					// EOFを読んだ
				tk = new CToken(CToken.TK_EOF, lineNo, startCol, "end_of_file");
				accept = true;
				break;
			case 2:					// ヘンな文字を読んだ
				tk = new CToken(CToken.TK_ILL, lineNo, startCol, text.toString());
				accept = true;
				break;
			case 3:					// 数（10進数）の開始
				ch = readChar();
				switch(subState) {
				case 0:
					if (Character.isDigit(ch)) {
						text.append(ch);
					}else {
						backChar(ch);	// 数を表さない文字は戻す（読まなかったことにする）
						tk = new CToken(CToken.TK_NUM, lineNo, startCol, text.toString());
						accept=true;
					}
					break;
				case 8:
					if(ch >= '0' && ch <= '7') {
						text.append(ch);
					}else {
						backChar(ch);	// 数を表さない文字は戻す（読まなかったことにする）
						tk = new CToken(CToken.TK_NUM, lineNo, startCol, text.toString());
						accept=true;
					}
					break;
				case 16:
					if(Character.isDigit(ch)) {
						text.append(ch);
					}else if(ch >= 'a' && ch <= 'f') {
						text.append(ch);
					}else {
						backChar(ch);	// 数を表さない文字は戻す（読まなかったことにする）
						if(!text.toString().equals("0x")) {
							tk = new CToken(CToken.TK_NUM, lineNo, startCol, text.toString());
						}else {
							tk = new CToken(CToken.TK_ILL, lineNo, startCol, text.toString());
						}
						accept=true;
					}
					break;
					
				}
				break;
			case 4:					// +を読んだ
				tk = new CToken(CToken.TK_PLUS, lineNo, startCol, "+");
				accept = true;
				break;
			case 5:					// -を読んだ
				tk = new CToken(CToken.TK_MINUS, lineNo, startCol, "-");
				accept = true;
				break;
			case 6:					// /を読んだ
				ch = readChar();
				switch(subState) {
				case 0:
				    backChar(ch);
					tk = new CToken(CToken.TK_DIV, lineNo, startCol, "/");
					accept = true;
					break;
				case 1:
					if (ch == '\n'||ch == '\r'||ch==(char) -1) {
						text.delete(0,text.length());
						state = 0;
						subState = 0;
					}
					break;
				case 2:
					if(ch==(char) -1) {	//コメント途中でEOF
						tk = new CToken(CToken.TK_ILL, startLine, startCol, text.toString());
						accept = true;
					}else if(ch=='*') {
						text.append(ch);
						ch = readChar();
						if(ch=='/') {
							text.delete(0,text.length());
							state = 0;
						}else {
							backChar(ch);
						}
					}
					break;
				}
				break;
			case 7:					// &を読んだ
				tk = new CToken(CToken.TK_AMP, lineNo, startCol, "&");
				accept = true;
				break;
			case 8:					// *を読んだ
				tk = new CToken(CToken.TK_MULT, lineNo, startCol, "*");
				accept = true;
				break;
			case 9:					// (を読んだ
				tk = new CToken(CToken.TK_LPAR, lineNo, startCol, "(");
				accept = true;
				break;
			case 10:					// )を読んだ
				tk = new CToken(CToken.TK_RPAR, lineNo, startCol, ")");
				accept = true;
				break;
			case 11:
				tk = new CToken(CToken.TK_LBAR, lineNo, startCol, "[");
				accept = true;
				break;
			case 12:
				tk = new CToken(CToken.TK_RBAR, lineNo, startCol, "]");
				accept = true;
				break;
			case 13: //識別子
				ch = readChar();
				if(Character.isLetterOrDigit(ch)||ch=='_'){
					text.append(ch);
				}else{
					backChar(ch);
					// 切り出した字句が登録済みキーワードかどうか判定する
					Integer id = (Integer) rule.get(text.toString());
					tk = new CToken(((id == null) ? CToken.TK_IDENT : id.intValue()), lineNo, startCol, text.toString());
					accept = true;
				}
			    break;
			case 14: //=
				ch=readChar();
				if(ch=='='){
					text.append(ch);
					tk = new CToken(CToken.TK_EQ, lineNo, startCol, "==");
				}else{
					tk = new CToken(CToken.TK_ASSIGN, lineNo, startCol, "=");
					backChar(ch);
				}
				accept = true;
				break;
			case 15:
				tk = new CToken(CToken.TK_SEMI, lineNo, startCol, ";");
				accept = true;
				break;
			case 16:	// <
				ch=readChar();
				if(ch=='='){
					text.append(ch);
					tk=new CToken(CToken.TK_LE, lineNo, startCol, "<=");
				}else{
					tk=new CToken(CToken.TK_LT, lineNo, startCol, "<");
					backChar(ch);
				}
				accept=true;
				break;
			case 17:	// >
				ch=readChar();
				if(ch=='='){
					text.append(ch);
					tk=new CToken(CToken.TK_GE, lineNo, startCol, ">=");
				}else{
					tk=new CToken(CToken.TK_GT, lineNo, startCol, ">");
					backChar(ch);
				}
				accept=true;
				break;
			case 18:	// !
				ch=readChar();
				if(ch=='='){
					text.append(ch);
					tk=new CToken(CToken.TK_NE, lineNo, startCol, "!=");
				}else{
					tk = new CToken(CToken.TK_ILL, lineNo, startCol, text.toString());
					backChar(ch);
				}
				accept=true;
				break;
			case 19:	// {
				tk = new CToken(CToken.TK_LCUR, lineNo, startCol, "{");
				accept = true;
				break;
			case 20:	// }
				tk = new CToken(CToken.TK_RCUR, lineNo, startCol, "}");
				accept = true;
				break;
			}
		}
		return tk;
	}
}
