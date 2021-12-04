# 実験Ⅱ miniCV説明書

## 概要

2021年度情報科学実験Ⅱ

実験4実施時

## 構造

### 現状

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

### ex4追加箇所

```
unsignedFactor ::= factorAmp | number | LPAR expression RPAR | addressToValue
factorAmp ::= AMP ( number | primary )
addressToValue ::= primary
primary ::= primaryMult | variable
primaryMult ::= MULT variable
variable ::= ident [ array ]
array ::= LBRA expression RBRA （注）LBRA=’[’, RBRA=’]’
ident ::= IDENT （注)’_’ も英字として扱うこと
```

## TODO

- [ ] 8進数、16進数の大きさチェックをsemanticCheckに移動する
- [ ] 
