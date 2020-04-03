package com.brown_chicken.diary01;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DisplayActivity extends Activity
        implements View.OnClickListener, Dialog.OnClickListener {
    private DBHelper helper;        //DBHelperクラス
    private EditText diaryEdit;     //EditTextクラス
    private Diary diary;            //Diaryクラス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //activity_display.xmlに作成した内容を画面に表示
        setContentView(R.layout.activity_display);

        //インデントを取得する
        Intent intent = getIntent();

        //インテントからBundleを取得する
        Bundle bundle = intent.getExtras();

        //Bundleから「CODE」というキーで設定している文字列を取得する
        String code = bundle.getString("CODE");

        //DBHelperクラスのオブジェクトを生成する
        helper = new DBHelper(this);

        //年月日情報を表示するTextViewのオブジェクトを取得する
        TextView ymdText = (TextView)findViewById(R.id.ymdText);

        //年月日情報を表示するTextViewにコードの年月日情報を表示する
        String year = code.substring(0, 4);
        String month = code.substring(4, 6);
        String day = code.substring(6);

        ymdText.setText(year + "年" + month + "月" + day + "日");

        //コードから日記情報（Diaryオブジェクト）を取得する
        diary = getDiary(code);

        //「登録」ボタンのオブジェクトを取得し、イベントリスナ登録をする
        Button registButton = (Button)findViewById(R.id.registButton);
        registButton.setOnClickListener(this);

        //「編集」ボタンのオブジェクトを取得し、イベントリスナ登録をする
        Button updateButton = (Button)findViewById(R.id.updateButton);
        updateButton.setOnClickListener(this);

        //「削除」ボタンのオブジェクトを取得し、イベントリスナ登録をする
        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        //「戻る」ボタンのオブジェクトを取得し、イベントリスナ登録をする
        Button backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        //指定のコードのDiaryがなければ、「登録」ボタンを押せるようにする
        registButton.setEnabled(diary == null ? true : false);

        //指定のコードのDiaryがあれば、「編集」ボタンを押せるようにする
        updateButton.setEnabled(diary != null ? true : false);

        //指定のコードのDiaryがあれば、「削除」ボタンを押せるようにする
        deleteButton.setEnabled(diary != null ? true : false);

        //指定のコードのDiaryがなければ、メンバ変数contentがからのDiaryオブジェクトを生成
        diary = diary == null ? new Diary(code, "") : diary;

        //日記内容表示用EditTextのオブジェクトを取得
        diaryEdit = (EditText)findViewById(R.id.diaryEdit);

        //Diaryオブジェクトのメンバ変数contentの内容を表示
        diaryEdit.setText(diary.getContent());
    }

    @Override
    public void onClick(View view) {
        //Intentクラスのオブジェクトを生成
        Intent intent = new Intent();

        //Diaryのメンバ変数contentにEditTextに入力された文字列を設定
        diary.setContent(diaryEdit.getText().toString());

        //ボタンのIDを取得
        int id = view.getId();

        //EditTextに入力された文字を格納する変数
        String s = "";

        switch (id) {
            case R.id.registButton :    //「登録」ボタンを押した場合
                //EditTextに入力された文字を取得
                s = diaryEdit.getText().toString();

                if (!s.equals("")) {    //EditTextが入力されている場合
                    if (registDiary()) {    //登録成功
                        intent.putExtra("SUCCESS_MESSAGE", "日記を登録しました。");
                        setResult(RESULT_OK, intent);
                    } else {    //登録失敗
                        intent.putExtra("CANCELED_MESSAGE", "日記の登録に失敗しました。");
                        setResult(RESULT_CANCELED, intent);
                    }
                } else {    //EditTextが未入力の場合
                    showAlert();    //警告表示
                    return;
                }

                break;
            case R.id.updateButton :    //「編集」ボタンを押した場合
                //EditTextに入力された文字を取得
                s = diaryEdit.getText().toString();

                if (!s.equals("")) {    //EditTextが入力されている場合
                    if (updateDiary()) {    //更新成功
                        intent.putExtra("SUCCESS_MESSAGE", "日記を更新しました。");
                        setResult(RESULT_OK, intent);
                    } else {    //更新失敗
                        intent.putExtra("CANCELED_MESSAGE", "日記の更新に失敗しました。");
                        setResult(RESULT_CANCELED, intent);
                    }
                } else {    //EditTextが未入力の場合
                    showAlert();    //警告表示
                    return;
                }

                break;

            case R.id.deleteButton :    //「削除」ボタンを押した場合
                if (deleteDiary()) {    //削除成功
                    intent.putExtra("SUCCESS_MESSAGE", "日記を削除しました。");
                    setResult(RESULT_OK, intent);
                } else {    //削除失敗
                    intent.putExtra("CANCELED_MESSAGE", "日記の削除に失敗しました。");
                    setResult(RESULT_CANCELED, intent);
                }

                break;
            case R.id.backButton :  //「戻る」ボタンを押した場合
                intent.putExtra("CANCELED_MESSAGE", "キャンセルしました。");
                setResult(RESULT_CANCELED, intent);

                break;
        }

        //DisplayActivityを終了
        finish();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    private void showAlert() {
        //AlertDialog.Builderクラスのオブジェクトを生成
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //ダイアログのタイトルを設定
        builder.setTitle("警告");

        //ダイアログのメッセージを設定
        builder.setMessage("文字が入力されていません。");

        //ダイアログにポジティブ・ボタン「OK」を設定
        builder.setPositiveButton("OK", this);

        //ダイアログを表示
        builder.show();
    }

    private boolean registDiary() {
        //SQLiteDatabaseクラスを取得する。（書込み用）
        SQLiteDatabase db = helper.getReadableDatabase();

        //Diariesテーブルに登録するレコードの設定準備
        ContentValues value = new ContentValues();
        value.put("code", diary.getCode());
        value.put("content", diary.getContent());

        //Diariesテーブルに登録
        boolean judge = db.insert("Diaries", null, value) != -1 ? true : false;

        //データベースをクローズ
        db.close();

        //登録結果情報を返す
        return judge;
    }

    private boolean updateDiary() {
        //SQLiteDatabaseクラスを取得する。（書込み用）
        SQLiteDatabase db = helper.getReadableDatabase();

        //Diariesテーブルに変更するレコードの設定準備
        ContentValues value = new ContentValues();
        value.put("code", diary.getCode());
        value.put("content", diary.getContent());

        //Diaryのメンバ変数codeの値のレコードを変更できるように条件を指定
        String condition = "code = '" + diary.getCode() + "'";

        //Diariesテーブルを変更
        boolean judge = db.update("Diaries", value, condition, null) != -1 ? true : false;

        //データベースをクローズ
        db.close();

        //変更結果情報を返す
        return judge;
    }

    private boolean deleteDiary() {
        //SQLiteDatabaseクラスを取得する。（書込み用）
        SQLiteDatabase db = helper.getReadableDatabase();

        //Diaryのメンバ変数codeの値のレコードを削除できるように条件を指定
        String condition = "code = '" + diary.getCode() + "'";

        //Diariesテーブルを削除
        boolean judge = db.delete("Diaries", condition, null) != -1 ? true : false;

        //データベースをクローズ
        db.close();

        //削除結果情報を返す
        return judge;
    }

    private Diary getDiary(String code) {
        Diary diary = null;

        //SQLiteDatabaseクラスを取得する。（読込み用）
        SQLiteDatabase db = helper.getReadableDatabase();

        //Diariesテーブルからデータを取得する列を設定
        String[] columns = {"code", "content"};

        //引数のcodeのレコードを取得するように条件設定
        String selection = "code = '" + code + "'";

        //Diariesテーブルからレコードを取得
        Cursor cursor = db.query("Diaries", columns, selection, null, null, null, null);

        if (cursor.moveToNext()) {  //レコードが存在した場合の処理
            //レコードから取得したcodeとcontentを元にDiaryオブジェクトを生成
            diary = new Diary(cursor.getString(0), cursor.getString(1));
        }

        return diary;
    }
}
