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

- [x] 8進数、16進数の大きさチェックをsemanticCheckに移動する
- [ ] 添字を記述する部分— つまり、“[” と“]” の間に書く式— の型がint でなければならない
- [ ] 配列名のところに出てくる識別子はint[] 型またはint*[] 型でなければならないこと
- [ ] ポインタ参照（例えば、*p）では、p がint であってはいけないこと
- [ ] &*varの意味（Cでは使えない）
- [ ] instanceofを用いて「factorAmp の子節点にprimary がつながっているとき、その下にはprimaryMultクラスのオブジェクトが来てはいけない」ことを記述
  - [ ] PrimaryMult.isFirst() がtrue のときは文法エラーとする
- [ ] したがって、子節点が整数型int2のときに、親節点の型を整数へのポインタ型int *へと変える型計算規則を持つことにな
