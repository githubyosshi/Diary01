package com.brown_chicken.diary01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    //アクセス修飾子、データ型、変数名
    private static String DB_NAME = "sqlite";   //データベース名
    private static int DB_VERSION = 1;  //バージョン

    //引数content、メンバ変数DB_NAMEとDB_VERSIONをスーパークラスのコンストラクタに渡す
    //データベースを作成
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //テーブル作成用SQL文の設定
        String sql = "CREATE TABLE Diaries (";
        sql += "code TEXT PRIMARY KEY,";
        sql += "content TEXT NOT NULL);";

        //テーブル作成用SQL文を実行
        database.execSQL(sql);
    }


    //データベースのバージョンが変わった時に呼び出されるが、今回は何もしない
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }
}
