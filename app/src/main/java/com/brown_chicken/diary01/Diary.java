package com.brown_chicken.diary01;

import java.io.Serializable;

public class Diary implements Serializable {
    //メンバー変数
    private String code;    //アクセス修飾子、データ型、変数名：コード
    private String content; //アクセス修飾子、データ型、変数名：内容

    //コンストラクタ
    public Diary() {}       //引数がない時のコンストラクターは処理を行わない

    public Diary(String code, String content) {
        this.code = code;           //第一引数をメンバ変数にセット
        this.content = content;     //第二引数をメンバ変数にセット
    }

    //セッタ＆ゲッタ
    public String getCode() {
        return code;           //メンバ変数codeを返す
    }

    public void setCode(String code) {
        this.code = code;     //引数codeをメンバ変数codeに設定
    }

    public String getContent() {
        return content;     //メンバ変数contentを返す
    }

    public void setContent(String content) {
        this.content = content;  //引数contentをメンバ変数codeに設定
    }
}
