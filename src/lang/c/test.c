// 構文解析＆意味解析テスト

if (true||false) {
  i_a=1;
} else {
   i_a=2;
}

if (i_a==3&&true) {
   i_a=3;
}

if (i_a==3&&true||i_a==2||i_a==1) {
   i_a=3;
}
