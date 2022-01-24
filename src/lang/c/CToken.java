package lang.c;

import lang.SimpleToken;

public class CToken extends SimpleToken {
	public static final int TK_PLUS			= 2;				// +
	public static final int TK_MINUS		= 3;				//-
	public static final int TK_AMP			= 4;				//&
	public static final int TK_MULT			= 5;				//*
	public static final int TK_DIV			= 6;				///
	public static final int TK_LPAR			= 7;				//(
	public static final int TK_RPAR			= 8;				//)
	public static final int TK_LBAR			= 9;				//[
	public static final int TK_RBAR			= 10;				//]
	public static final int TK_ASSIGN		= 11;				//=
	public static final int TK_SEMI			= 12;				//;
	public static final int TK_LT			= 13;				//<
	public static final int TK_LE			= 14;				//<=
	public static final int TK_GT			= 15;				//>
	public static final int TK_GE			= 16;				//>=
	public static final int TK_EQ			= 17;				//==
	public static final int TK_NE			= 18;				//!=
	public static final int TK_TRUE			= 19;				//true
	public static final int TK_FALSE		= 20;				//false
	public static final int TK_IF			= 21;				//if
	public static final int TK_ELSE			= 22;				//else
	public static final int TK_WHILE		= 23;				//while
	public static final int TK_INPUT		= 24;				//input
	public static final int TK_OUTPUT		= 25;				//output
	public static final int TK_LCUR			= 26;				//{
	public static final int TK_RCUR			= 27;				//}

	public CToken(int type, int lineNo, int colNo, String s) {
		super(type, lineNo, colNo, s);
	}
}
