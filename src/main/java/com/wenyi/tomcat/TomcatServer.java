package com.wenyi.tomcat;

import com.wenyi.tomcat.core.RequestHandler;
import com.wenyi.tomcat.enumeration.Grade;
import com.wenyi.tomcat.utils.LogUtil;
import com.wenyi.tomcat.utils.ServerUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi;
 * @author: Administrator;
 * @date: 2021/4/13 17:19;
 * @Description: 网络编程 模仿tomcat服务
 * 配置我的服务器
 * 1、读取配置文件server.xml 获取监听端口
 * 2、创建记录日志的工具类 记录日志
 * 3、多线程开启监听
 * 4、对每一个套接字 创建一个类单独来处理该套接字
 */
public class TomcatServer {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        try{
            long startTime = System.currentTimeMillis();

            //获取端口:port
            int port = ServerUtil.getPort();
            //创建服务器 并指定端口:port
            serverSocket = new ServerSocket(port);
            //将监听的端口，存储到日志，并打印到控制台
            LogUtil.getInstance().log("tomcat Server is Starting, Listen port: "+port, Grade.MESSAGE_CH);

            long endTime = System.currentTimeMillis();
            LogUtil.getInstance().log("Server startup in "+(endTime-startTime)+" ms",Grade.MESSAGE_CH);

            //获取线程池来处理 监听得到的套接字
            ExecutorService threadPool = Executors.newFixedThreadPool(10);
            while (true){
                //开启监听
                Socket socket = serverSocket.accept();
                //每来一个请求开启一个线程来处理监听事件 使用核心类来处理套接字
                threadPool.submit(new RequestHandler(socket));
            }
        }catch (IOException e){
            e.printStackTrace();
            LogUtil.getInstance().log(e.toString(),Grade.ERROR_CH);
        }
    }
}
