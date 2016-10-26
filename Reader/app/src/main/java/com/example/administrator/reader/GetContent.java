package com.example.administrator.reader;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/17.
 */
public class GetContent {
    public static List<String> subsection(InputStream inputStream) {
        List<String> novelList = new ArrayList<>();
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "Unicode");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer lines = new StringBuffer("");
        String line = null;
        try {
            //读取小说内容 并且手动添加\n
            for (; (line = reader.readLine()) != null; ) {
                lines.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String string = lines.toString();
        String result;
        Pattern p  = Pattern.compile("\\s第[一-十0-9]+[章节回卷集]\\s");
        Matcher matcher = p.matcher(string);
        Long count = 0L;
        int textStar,textEnd,textlength;
        String subString ;
        //获得第一个标题后面的起始位置
        if (matcher.find()){
            textStar = matcher.end();
            while (matcher.find()){
                count++;
                result = matcher.group();
                Log.i("=====#17","匹配"+count+"开始位置"+matcher.start()+"\t结束位置"+matcher.end());
                textEnd = matcher.start();
                subString = string.substring(textStar,textEnd);
                textStar = matcher.end();
                novelList.add(subString);
                //subStrign获得了每一章的内容
            }
        }
        return novelList;
    }
}
