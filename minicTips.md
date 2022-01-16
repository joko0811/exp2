# 実験Ⅱ miniCV説明書

## 概要

2021年度情報科学実験Ⅱ

実験9実施時

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
condition ::= conditionNot | unsignedCondition
conditionNot ::= NOT unsignedCondition
unsignedCondition ::= conditionTerm {conditionAnd | conditionOr}
conditionAnd :: AND conditionTerm
conditionOr ::= OR conditionTerm
conditionTerm ::= conditionFactor {conditionLT | conditionLE | conditionGT | conditionGE | conditionEQ | conditionNE} 
conditionLT ::= LT conditionFactor
conditionLE ::= LE conditionFactor
conditionGT ::= GT conditionFactor
conditionGE ::= GE conditionFactor
conditionEQ ::= EQ conditionFactor
conditionNE ::= NE conditionFactor
conditionFactor ::= TRUE | FALSE | expression | LPAR condition RPAR
```

### H9追加箇所

```
fatalError投げてる箇所を全部三つのエラーに分類しなおす（くそだるい）
```

