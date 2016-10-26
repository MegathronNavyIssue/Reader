package com.example.administrator.reader;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/28.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    List<String> novelList = new ArrayList<>();
    List<String> eachPageList = new ArrayList<>();
    int novel_page = 0;//小说章节计数器
    int chapter_page = 0;//每章页数计数器
    int novel_page_c ;//总章节数
    int chapter_page_c ;//某章节总页数
    int time = 1;
    int page = 1;
    int row, col, width, height;
    Button read;
    Button last;
    Button next;
    TextView textView1;
    TextView textView2;
    LinearLayout layout;
    LinearLayout button_list;
    Map<Integer, String> list = new HashMap<Integer, String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        read = (Button) findViewById(R.id.button1);
        width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        height = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        layout = (LinearLayout) findViewById(R.id.linearLayout2);
        last = (Button) findViewById(R.id.button_last);
        next = (Button) findViewById(R.id.button_next);
        button_list = (LinearLayout) findViewById(R.id.button_list);
        read.setOnClickListener(this);
        last.setOnClickListener(this);
        next.setOnClickListener(this);

        /*
        请求权限
        需要在AndroidManifest中声明
        <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        */
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
          this.requestPermissions(permission, 1);
    }

    //requestPermissions需要重写onRequestPermissionsResult
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //计算屏幕的高度
        height = layout.getMeasuredHeight();
        row = (height - last.getMeasuredHeight()) / (textView1.getLineHeight());
        //计算屏幕能显示多少行
        col = (int) (width / (textView1.getTextSize() + textView1.getScaleX()));
        //将小说内容转换为InputStream
        InputStream inputStream = getResources().openRawResource(R.raw.long_nove2);
        //novelList存放的是每一章的内容
        novelList = GetContent.subsection(inputStream);
        //获得总章节数
        novel_page_c = novelList.size();
        //eachPageList获得每一章的内容
        //novelList.get(0) 获得第1章的内容，未排版
        //Read_method.getString()用来排版
        eachPageList = Read_method.getString(novelList.get(0),row,col);
        //获得第一章的总页数
        chapter_page_c = eachPageList.size();
    }


    //小说功能入口
    private void setDate() {
        //读取小说内容转换为inputStream


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击
            case R.id.button1:
                if (read.getVisibility() == View.VISIBLE) {
                    read.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    //设置第1章第1页的内容
                    textView1.setText(Read_method.getString(novelList.get(0),row,col).get(0));
                    String print = "第" + String.valueOf(time) + "页";
                    textView2.setText(print);
                }
                break;
            //点击“上一页”
            case R.id.button_last:
                //当前处于第一章第一页时
                if (novel_page == 0 && chapter_page ==0) {
                    Toast.makeText(MainActivity.this, "已是第一页！", Toast.LENGTH_SHORT).show();
                }
                //某一章不为第一页时
                else if (chapter_page!=0){
                    chapter_page--;
                    textView1.setText(eachPageList.get(chapter_page));
                    String print = "第" + String.valueOf(page) + "页";
                    textView2.setText(print);
                }
                //某一章的第一页时
                else if(chapter_page==0 && novel_page!= 0){
                    //读取上一章的内容
                    novel_page--;
                    eachPageList = Read_method.getString(novelList.get(novel_page),row,col);
                    //获得该章节的页数
                    chapter_page_c = eachPageList.size();
                    //点击前一页后设置上一章最后一页的内容
                    textView1.setText(eachPageList.get(chapter_page_c-1));
                    chapter_page = chapter_page_c-1;
                }
                break;
            //点击“下一页”
            case R.id.button_next:
                //最后一章，最后一页
                if (novel_page == novel_page_c && chapter_page ==chapter_page_c) {
                    Toast.makeText(MainActivity.this, "已是最后一页！", Toast.LENGTH_SHORT).show();
                }
                else if(novel_page != novel_page_c && chapter_page<chapter_page_c-1){
                    chapter_page++;
                    textView1.setText(eachPageList.get(chapter_page));
                    String print = "第" + String.valueOf(page) + "页";
                    textView2.setText(print);
                }
                //该章节最后一页
                else if(chapter_page ==chapter_page_c -1 ){
                    novel_page++;
                    eachPageList = Read_method.getString(novelList.get(novel_page),row,col);
                    //获得该章节的页数
                    chapter_page_c = eachPageList.size();
                    chapter_page = 0;
                    //点击后一页后设置下一章第一页的内容
                    textView1.setText(eachPageList.get(chapter_page));
                }
                break;
        }
    }
}

