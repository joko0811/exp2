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

### condition

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

### H7追加箇所

```
statement ::= statementAssign | statementIf | statementWhile | statementInput | statementOutput
statementIf ::= IF statementCondition statementExecution  [ stamementElse ]
statementElse ::= ELSE statementExecution
statementWhile ::= WHILE statementCondition statementExecution
statementInput ::= INPUT primary SEMI
statementOutput ::= OUTPUT expression SEMI
statementCondition ::= LPAR condition RPAR
statementExecution ::= LCUR [ { statement } ] RCUR
```

## TODO
