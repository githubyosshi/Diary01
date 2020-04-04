package com.brown_chicken.diary01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        Button nextMontButton = (Button)findViewById(R.id.nextMonthButton);
        nextMontButton.setOnClickListener(this);

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
            case R.id.lastMonthButton: //「前月」ボタンを押した場合
                //月の情報を−１する
                myCalendar.setLastMonth();

                //カレンダー再表示
                displayAll();

                break;
            case R.id.nextMonthButton: //「来月」ボタンを押した場合
                //月の情報を＋１する
                myCalendar.setNextMonth();

                //カレンダー再表示
                displayAll();

                break;
            default: //「日」ボタンを押した場合
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
    protected void omActivityResult(int requestCode, int resultCode, Intent intent) {
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
    
}
