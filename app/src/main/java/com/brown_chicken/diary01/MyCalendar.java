package com.brown_chicken.diary01;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyCalendar {
    //アクセス修飾子、データ型、変数名
    private static MyCalendar myCalendar = new MyCalendar();
    private Calendar calendar;

    private MyCalendar() {
        //Calendarのインスタンスを取得
        calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR); //現在の「年」を取得
        int month = calendar.get(Calendar.MONTH);   //現在の「月」を取得
        int day = 1;    //月の先頭の日を「１」に設定

        //Calendarに「現在の年月」と「月の先頭の日」を設定
        calendar.set(year, month, day);
    }

    public static MyCalendar getInstance() {
        //メンバ変数myCalendarを返す
        return myCalendar;
    }

    public void setNextMonth() {
        //現在のカレンダーの月の情報を＋１する
        calendar.add(Calendar.MONTH, 1);
    }

    public void setLastMonth() {
        //現在のカレンダーの月情報を−１にする
        calendar.add(Calendar.MONTH, -1);
    }

    public int getMax() {
        //現在のカレンダーの月の最大日を取得し、それを返す
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public int getWeek() {
        //現在のカレンダーの曜日情報を取得し、それを返す
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public String getCode(int day) {
        //年は４桁、月日は２桁で表示するよに書式を設定
        DecimalFormat df1 = new DecimalFormat("0000");
        DecimalFormat df2 = new DecimalFormat("00");

        //現在の年月と引数dayの値の情報を指定の書式で返す
        return df1.format(calendar.get(Calendar.YEAR))
                + df2.format(calendar.get(Calendar.MONTH) + 1)
                + df2.format(day);
    }

    public String getFormat(int ch) {
        //現在の年月情報を引数chの値により書式を設定して返す
        SimpleDateFormat sdf = new SimpleDateFormat(ch == 1 ? "yyyy年MM月" : "yyyyMM");
        return sdf.format(calendar.getTime());
    }
}

