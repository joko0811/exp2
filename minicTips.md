# 実験Ⅱ miniCV説明書

## 概要

2021年度情報科学実験Ⅱ

実験6実施時

## 構造

### 現状

```
program ::= { statement } EOF
statement ::= statementAssign
statementAssign ::= primary ASSIGN expression SEMI
expression ::= term { expressionAdd | expressionSub }
expressionAdd ::= PLUS term
expressionSub ::= MINUS term
term ::= factor { termMult | termDiv }
termMult ::= MULT factor
termDiv ::= DIV factor
factor ::= plusFactor | minusFactor | unsignedFactor
plusFactor ::= PLUS unsignedFactor
minusFactor ::= MINUS unsignedFactor
unsignedFactor ::= factorAmp | number | LPAR expression RPAR | addressToValue
factorAmp ::= AMP ( number | primary )
addressToValue ::= primary
primary ::= primaryMult | variable
primaryMult ::= MULT variable
variable ::= ident [ array ]
array ::= LBRA expression RBRA
ident ::= IDENT
number ::= NUM
```

### ex6追加箇所

```
condition ::= TRUE | FALSE | expression ( conditionLT | conditionLE | conditionGT
| conditionGE | conditionEQ | conditionNE )
conditionLT ::= LT expression
conditionLE ::= LE expression
conditionGT ::= GT expression
conditionGE ::= GE expression
conditionEQ ::= EQ expression
conditionNE ::= NE expression
（注）LT=’<’, LE=’<=’, GT=’>’, GE=’>=’, EQ=’==’, NE=’!=’
```

## TODO
