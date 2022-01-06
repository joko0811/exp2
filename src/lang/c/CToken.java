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
	public static final int TK_LCUR			= 10;				//{
	public static final int TK_RCUR			= 11;				//}
	public static final int TK_RBAR			= 12;				//]
	public static final int TK_ASSIGN		= 13;				//=
	public static final int TK_SEMI			= 14;				//;
	public static final int TK_LT			= 15;				//<
	public static final int TK_LE			= 16;				//<=
	public static final int TK_GT			= 17;				//>
	public static final int TK_GE			= 18;				//>=
	public static final int TK_EQ			= 19;				//==
	public static final int TK_NE			= 20;				//!=
	public static final int TK_NOT			= 21;				//!
	public static final int TK_AND	  		= 22;				//&&
	public static final int TK_OR	   		= 23;				//||
	public static final int TK_TRUE			= 24;				//true
	public static final int TK_FALSE		= 25;				//false
	public static final int TK_IF			= 26;				//if
	public static final int TK_ELSE			= 27;				//else
	public static final int TK_WHILE		= 28;				//while
	public static final int TK_INPUT		= 29;				//input
	public static final int TK_OUTPUT		= 30;				//output

	public CToken(int type, int lineNo, int colNo, String s) {
		super(type, lineNo, colNo, s);
	}
}
