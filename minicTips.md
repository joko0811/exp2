# 構造

program ::= expression
expression ::= term { expressionAdd | expressionSub }
expressionAdd ::= PLUS term
expressionSub ::= MINUS term
term ::= factor { termMult | termDiv }
termMult ::= MULT factor
termDiv ::= DIV factor
factor ::= plusFactor | minusFactor | unsignedFactor
plusFactor ::= PLUS unsignedFactor
minusFactor ::= MINUS unsignedFactor （注）LPAR=’(’, RPAR=’)’
unsignedFactor ::= factorAmp | number | LPAR expression RPAR
number ::= NUM

# minicTips(実験1時点)

- /srcにプログラムが入ってる

- IDEAでビルドしたけどなんかエラーの詳細を教えてくれなかった

- 佐藤さんがecllipseだと詳細を教えてくれるって言ってたのでエクリプスにいこうした

- 最初なんかビルドできなかったけどなんかしたら治った。なにしたんだっけ

- /src/lang/c/MiniConpiler.javaにメインクラスがある
- 改行時に式の分割を行わない

## 字句解析

- 字句解析パートが存在せず、構文解析時にまとめて行っている。言語理論踏襲しろよ
- /src/lang/c/CTokenizer.javaのgetNextTokenで、取得したいときに一文字ずつ取得している
- parse時、tokenがnumberだったときはgetNextTokenで次文字を取得している
  - →必然的にパース後は数字以外の値を保持することになる
- 複数桁の数字はデフォルトでうまく取得してくれている

## 構文解析

- Program.java parse実行時にファイル内式すべてのパースが一度に実行される

## 手順（クソ雑）

0. MiniCompiler.javaにて最初のToken取得＆Program.parse実行
1. Program.Parse内にてExpression.parse(実質これが本体)の実行
   1. この時点で記号が来るはずなので各記号のparse実行
      1. 各記号の計算に必要なtokenを読む
      2. 構文木作成
   2. 全ての記号に対して1を繰り返す
2. 最後のTokenがEOFでなければエラー



## 意味解析

## コード生成

