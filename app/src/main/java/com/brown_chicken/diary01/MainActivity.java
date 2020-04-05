package com.brown_chicken.diary01;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static int REQUEST_CODE = 0;    //リクエストコード

    private DBHelper helper;        //DBHelperクラス
    private MyCalendar myCalendar;  //MyCalendarクラス
    private GridLayout gridLayout;  //GridLayoutクラス

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DBHelperクラスのオブジェクトを生成
        helper = new DBHelper(this);

        //「前月」ボタンのオブジェクトを取得とイベントリスナ登録
        Button lastMonthButton = (Button)findViewById(R.id.lastMonthButton);
        lastMonthButton.setOnClickListener(this);

        //「来月」ボタンのオブジェクトを取得とイベントリスナ登録
        Button nextMonthButton = (Button)findViewById(R.id.nextMonthButton);
        nextMonthButton.setOnClickListener(this);

        //MyCalendarクラスのオブジェクトを取得
        myCalendar = MyCalendar.getInstance();

        //GridLayoutのオブジェクトを取得
        gridLayout = (GridLayout)findViewById(R.id.gridLayout1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayAll();
    }

    @Override
    public void onClick(View view) {
        //押されたボタンのIDを取得
        int id = view.getId();

        switch (id) {
            case R.id.lastMonthButton : //「前月」ボタンを押した場合
                //月の情報を−１する
                myCalendar.setLastMonth();

                //カレンダー再表示
                displayAll();

                break;
            case R.id.nextMonthButton : //「来月」ボタンを押した場合
                //月の情報を＋１する
                myCalendar.setNextMonth();

                //カレンダー再表示
                displayAll();

                break;
            default : //「日」ボタンを押した場合
                //day.xmlのタグ情報を取得
                String code = (String) view.getTag();

                //「DisplayActivity.class」を呼び出せるようにインテントを生成
                Intent intent = new Intent(this, DisplayActivity.class);

                //タグ情報をインテントに「CODE」というキーで設定
                intent.putExtra("CODE", code);

                //戻り値となるインテントを取得できる形でアクティビティを起動
                startActivityForResult(intent, REQUEST_CODE);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //インテントに設定した情報を取得
        Bundle bundle = intent.getExtras();

        if (requestCode == REQUEST_CODE) {
            String s = "";

            if (resultCode == RESULT_OK) {
                s = bundle.getString("SUCCESS_MESSAGE");
            } else if (resultCode == RESULT_CANCELED) {
                s = bundle.getString("CANCELED_MESSAGE");
            }

            //トーストでメッセージ表示
            Toast t = Toast.makeText(this, s, Toast.LENGTH_SHORT);
            t.show();
        }
    }

    private void displayAll() {
        //GridLayout内に設定している全てのビューを削除
        gridLayout.removeAllViews();

        //年月を「○○○○年□□月」の書式で取得
        String ymData = myCalendar.getFormat(1);

        //年月表示用のTextViewを取得
        TextView ymText = (TextView)findViewById(R.id.ymText);

        //年月表示用のTextViewに年月を表示
        ymText.setText(ymData);

        //displayWeeksメソッドを呼び出す
        displayWeeks();

        //displayDaysメソッドを呼び出す
        displayDays();
    }

    private void displayWeeks() {
        String[] weeks = {"日", "月", "火", "水", "木", "金", "土"};

        for (String week : weeks) {
            //GridLayoutに設定する曜日情報表示用のビュー（week.xml）を取得
            View child = getLayoutInflater().inflate(R.layout.week, null);

            //week.xmlに設定した曜日表示用のTextViewを取得
            TextView text = (TextView)child.findViewById(R.id.weekText);

            //曜日表示用のTextViewに配列weeksの文字を設定
            text.setText(week);

            //GridLayout.LayoutParamsのオブジェクトを生成
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            //GridLayout.LayoutParamsオブジェクトの幅をコンテナのサイズに合わせる
            params.setGravity(Gravity.FILL_HORIZONTAL);

            //GridLayoutにビュー（weeks.xml）を追加
            gridLayout.addView(child);
        }
    }

    private void displayDays() {
        //getCodeListメソッドを呼び出しコードリストを取得
        ArrayList<String> codeList = getCodeList();

        //設定された月の最大日数を取得
        int max = myCalendar.getMax();

        //設定された曜日の情報を取得
        int week = myCalendar.getWeek();

        for (int i = 1; i <= max; i++) {    //１〜月の最大日数まで繰り返す
            //GridLayoutに設定する日および状態表示用のビュー（day.xml）を取得
            View child = getLayoutInflater().inflate(R.layout.day, null);

            //day.xmlに設定した日表示用のTextViewを取得
            TextView dayText = (TextView) child.findViewById(R.id.dayText);

            //日表示用のTextViewに日情報を設定
            dayText.setText(Integer.toString(i));

            //日情報を元に年月日情報を取得
            String ymdData = myCalendar.getCode(i);

            //day.xmlに設定した状態表示用のTextViewを取得
            TextView dispText = (TextView) child.findViewById(R.id.dispText);

            //コードリストに一致する年月日情報が存在すれば
            //状態表示用のTextViewに文字列「＊日記あり」を表示
            dispText.setText(codeList.contains(ymdData) ? "＊日記あり" : "");

            //day.xmlに年月日情報をタグとして設定
            child.setTag(ymdData);

            //day.xmlを押した時のイベントリスナ登録
            child.setOnClickListener(this);

            //GridLayout.LayoutParamsのオブジェクトを生成
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            if (i == 1) {
                //「１」を表示する開始位置を設定
                params.rowSpec = GridLayout.spec(1);    //「１」行目
                params.columnSpec = GridLayout.spec(week - 1);  //「week - 1」列目
            }

            //GridLayout.LayoutParamsオブジェクトの幅をコンテナのサイズに合わせる
            params.setGravity(Gravity.FILL_HORIZONTAL);

            //GridLayoutにビュー（days.xmlとパラメータ）を追加
            gridLayout.addView(child, params);
        }
    }

    private ArrayList<String> getCodeList() {
        //年月日情報のコードの文字列を格納用ArrayList
        ArrayList<String> codeList = new ArrayList<String>();

        //年月を「○○○○□□」の書式で取得
        String ymData = myCalendar.getFormat(2);

        //SQLiteDatabaseクラスを取得（読込み用）
        SQLiteDatabase database = helper.getReadableDatabase();

        //Diariesテーブルからデータを取得する列を設定
        String[] columns = {"CODE"};

        //年月部分が一致するレコードを取得するように条件設定
        String selection = "code LIKE '" + ymData + "%'";

        //Diariesテーブルから条件に一致したcode情報を取得
        Cursor cursor = database.query("Diaries", columns, selection, null, null, null, null);

        //取得したcodeをArrayListに順次格納
        while (cursor.moveToNext()) {
            codeList.add(cursor.getString(0));
        }
        //コードリストを返す
        return codeList;
    }
}
