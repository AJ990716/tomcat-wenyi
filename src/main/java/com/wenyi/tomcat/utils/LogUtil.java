package com.wenyi.tomcat.utils;

import com.wenyi.tomcat.enumeration.Grade;
import com.wenyi.tomcat.enumeration.Path;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.tomcat.utils;
 * @author: Administrator;
 * @date: 2021/4/13 18:37;
 * @Description: 日志工具 处理日志的存储与打印
 */
public class LogUtil {
    //volatile：因为每一个线程有自己的内存 主线程也有自己的内存
    //这个关键字 保证将本内存赋值的logUtil传给主线程
    private static volatile LogUtil logUtil;
    private static SimpleDateFormat simpleDateFormat;
    private static File file;
    private LogUtil(String dateFormat,String filePath){
        simpleDateFormat = new SimpleDateFormat(dateFormat);
        file = new File(filePath);
    }

    /**
     * 单例设计模式 多线程获取对象
     * @param date
     * @return 返回创建的单例对象
     */
    public static LogUtil getInstance(String... date){
        if (logUtil==null){
            synchronized (LogUtil.class){
                if (logUtil==null){
                    if (date.length==0)
                        logUtil = new LogUtil("yyyy-MM-dd HH:mm:ss:SSS", Path.LOG_PATH.toString());
                    else if (date.length == 1)
                        logUtil = new LogUtil(date[0],Path.LOG_PATH.toString());
                    else
                        logUtil = new LogUtil(date[0],date[1]);
                }
            }
        }
        return logUtil;
    }

    /**
     * 输入消息信息 与消息等级 将日志消息写入文件 同时打印到控制台
     * @param message 消息信息
     * @param grade 消息等级
     * @throws
     */
    public void log(String message, Grade grade) {
        try {
            //获取文件的字符输出流
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            //将当前时间转换为日志需要的格式
            String date = simpleDateFormat.format(new Date());
            //构造日志格式
            String log = date+" "+grade+" ["+Thread.currentThread().getName()+"] "+message+"\n";
            //将日志写入文件
            bufferedWriter.write(log);
            //关闭流
            bufferedWriter.close();
            //打印日志到控制台
            System.out.println(log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
