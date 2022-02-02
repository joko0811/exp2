# 実験Ⅱ miniCV説明書

## 概要

2021年度情報科学実験Ⅱ

実験6実施時

## 構造

### 現状

```
program ::= { statement } EOF
statement ::= statementAssign | statementIfElse | statementWhile | statementInput | statementOutput
statementIfElse ::= statementIf [ [ { ELSE statementIf } ] [ ELSE statementExecution ] ]
statementIf ::= IF statementCondition statementExecution
statementExecution ::= LCUR [ { statement } ] RCUR
statementWhile ::= WHILE statementCondition statementExecution
statementInput ::= INPUT primary SEMI
statementOutput ::= OUTPUT expression SEMI
statementCondition ::= LPAR condition RPAR
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
condition ::= TRUE | FALSE | expression ( conditionLT | conditionLE | conditionGT | conditionGE | conditionEQ | conditionNE )
conditionLT ::= LT expression
conditionLE ::= LE expression
conditionGT ::= GT expression
conditionGE ::= GE expression
conditionEQ ::= EQ expression
conditionNE ::= NE expression
（注）LT=’<’, LE=’<=’, GT=’>’, GE=’>=’, EQ=’==’, NE=’!=’
```

### H8追加箇所

```
condition ::= conditionStatement  {conditionLT | conditionLE | conditionGT | conditionGE | conditionEQ | conditionNE} 
conditionLT ::= LT conditionStatement 
conditionLE ::= LE conditionStatement 
conditionGT ::= GT conditionStatement 
conditionGE ::= GE conditionStatement 
conditionEQ ::= EQ conditionStatement 
conditionNE ::= NE conditionStatement 
conditionStatement ::= conditionExpression {conditionOr}
conditionOr ::= OR conditionExpression
conditionExpression ::= conditionTerm {conditionAnd}
conditionAnd :: AND conditionTerm
conditionTerm ::= conditionFactor|conditionNot
conditionNot ::= NOT conditionFactor
conditionFactor ::= TRUE | FALSE | expression
```

## TODO

```
program ::= expression
expression ::= term { expressionAdd | expressionSub }
expressionAdd ::= PLUS term
expressionSub ::= MINUS term
term ::= factor { termMult | termDiv }
termMult ::= MULT factor
termDiv ::= DIV factor
factor ::= plusFactor | minusFactor | unsignedFactor
plusFactor ::= PLUS unsignedFactor
minusFactor ::= MINUS unsignedFactor
unsignedFactor ::= factorAmp | number | LPAR expression RPAR
factorAmp ::= AMP number
number ::= NUM
```



- {}について見直す
- 下位要素が複数ある場合のsemanticCheckでの型つけについて見直す
