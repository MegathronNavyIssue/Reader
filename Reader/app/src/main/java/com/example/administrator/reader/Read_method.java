package com.example.administrator.reader;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/28.
 */
public class Read_method {

    public static List<String> getString(String novelContent, int row, int col) {
        InputStreamReader inputStreamReader = null;
        StringBuffer lines = new StringBuffer("");
        StringBuffer lines_page = new StringBuffer("");
        List<String> list = new ArrayList<>();
        String line = null;
        String line_tem = null;
            lines.append(novelContent);
            Go:
            for (int time = 1;;time++){
                //根据页面大小读取每页所需行数
                PAGE:
                for (int h = 1; h <= row; h++) {
                    //判断是否是文本末尾
                    if(lines.length()<=col){
                        lines_page.append(lines);
                        list.add(String.valueOf(lines_page)+"\n");
                        break Go;
                    }else{
                        //判断第一个字符是否是“\n”
                        if (lines.substring(0, col).charAt(0)=='\n' ){
                            //如果第一个字符是“\n”则删除
                            lines.deleteCharAt(0);
                        }
                        for (int tem = 0;tem<lines.substring(0, col).length();tem++){
                            if (lines.substring(0, col).charAt(tem)=='\n'){
                                lines_page.append(lines.substring(0, tem+1));
                                lines.delete(0, tem+1);
                                if (h==row){
                                    break PAGE;
                                }
                                tem = 0;
                                h++;
                            }
                        }
                        lines_page.append(lines.substring(0, col)+"\n");
                        lines.delete(0, col);
                    }
                }
                list.add(String.valueOf(lines_page));
                lines_page = new StringBuffer("");
            }
        return list;
    }
}
