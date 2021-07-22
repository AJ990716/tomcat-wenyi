package com.wenyi.tomcat.core;

import com.wenyi.sun.servlet.HttpServlet;
import com.wenyi.tomcat.enumeration.Grade;
import com.wenyi.tomcat.utils.LogUtil;
import com.wenyi.tomcat.utils.StaticResourcesUtils;
import com.wenyi.tomcat.utils.WebXmlUtil;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.tomcat.core;
 * @author: Administrator;
 * @date: 2021/4/13 20:03;
 * @Description: 核心类 用于处理每一次的 socket 套接字
 */
public class RequestHandler implements Runnable {
    private Socket socket;
    //将成功连接的套接字获取进行赋值
    public RequestHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            //获取对方的info 并且写入日志 打印到控制台
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Access socket info:\n\taccess address: ").append(socket.getInetAddress())
                    .append("\n\taccess port: ").append(socket.getPort())
                    .append("\n\trequest port: ").append(socket.getLocalPort());
            LogUtil.getInstance().log(stringBuilder.toString(), Grade.USER_INFO_CH);

            //获取客服端（浏览器）发送过来的HTTP请求 将字节输入流转换为字符缓冲流 便于逐行读取
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();

            //获取http协议的第一行：请求行中的url :解析获取的第一行用户请求的数据 GET /index.html HTTP/1.1
            String url = line.split(" ")[1];

            //存储 http请求的所有数据 A、 请求行 B、 消息报头 C、 空白行 D、 请求体
            stringBuilder.setLength(0);
            //将http请求的协议存储于 HttpRequest类中 便于用户使用处理
            HttpRequest httpRequest = new HttpRequest();
            while (line!=null&&!"".equals(line)){
                httpRequest.addRequestInfo(line);
                stringBuilder.append(line).append("\n\t");
                line = bufferedReader.readLine();
            }
            LogUtil.getInstance().log("请求报文（请求行、请求头信息、空白行、请求体）:\n\t"
                    +stringBuilder.toString(),Grade.HTTP_INFO_CH);
            if (url.endsWith("/"))
                url = "/view/login/index.html";
            LogUtil.getInstance().log("request url: "+url,Grade.MESSAGE_CH);

            //如果访问的是静态资源则直接将text\html资源返回给浏览器
            if (url.endsWith(".html")||url.endsWith(".htm")||url.endsWith("/")){
                if (StaticResourcesUtils.isExist(url)){
                    //如果文件存在则读取静态资源并返回给浏览器
                    List<String> list = StaticResourcesUtils.getStaticResources(url);
                    responseStaticResources(list);
                }else {
                    List<String> list = StaticResourcesUtils.getStaticResources("/view/error/404.html");
                    responseStaticResources(list);
                }
                //如果访问的是多媒体资源 则根据多媒体类型返回 资源给浏览器
            }else if (url.endsWith(".jpg")||url.endsWith(".mp3")){
                responseStaticResources(url);
            }else {
                //动态资源访问 /view/login/userRegister?username=admin&password=123456
                //将get请求的url解析 获取用户访问的参数信息 封装在httpRequest的map中
                httpRequest.parseParam(url);

                if (!url.endsWith(".ico")){
                    LogUtil.getInstance().log("request params is :"+httpRequest.paramMap,Grade.MESSAGE_CH);
                    //获取myweb.xml中的所有地址映射对象 封装成map
                    Map<String, Object> urlObj = WebXmlUtil.parserXml();
                    //根据地址获取 反射的对象 而这个反射的对象我们要想使用那么就得有个接口来规范这种连接

                    //面向接口编程
                    String url_pattern = url.split("\\?")[0];
                    //获取用户得反射对象 用规范接口对象表示 （用户必须遵循这个规范 实现接口） 运行期间为用户的对象
                    HttpServlet httpServlet = (HttpServlet)urlObj.get(url_pattern);

                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter out=new PrintWriter(outputStream,true);
                    HttpResponse httpResponse=new HttpResponse(out);
                    //为接口传值
                    httpServlet.service(httpRequest,httpResponse);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.getInstance().log(e.toString(),Grade.ERROR_CH);
        }
    }

    /**
     * 将多媒体文件响应到浏览器
     * @param url
     * @throws IOException
     */
    private void responseStaticResources(String url) throws IOException {
        String path = "./src/main/webapp/"+url;
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

        StringBuilder stringBuilder = new StringBuilder();
        String ok = "HTTP/1.1 200 ok\n\t";
        stringBuilder.append(ok);
        bufferedOutputStream.write(ok.getBytes());
        String type = "";
        if (url.endsWith(".jpg")){
            type = "Content-Type:image/jpg\n";
        }else if (url.endsWith(".mp3")){
            type = "Content-Type:audio/mpeg\n";
        }

        stringBuilder.append(type);
        LogUtil.getInstance().log("响应报文（状态行、消息报头、空白行、响应体）:\n\t"
                +stringBuilder.toString(),Grade.HTTP_INFO_CH);

        bufferedOutputStream.write(type.getBytes());
        bufferedOutputStream.write("\n".getBytes());
        byte[] bytes = new byte[1024];
        int len;
        while ((len = bufferedInputStream.read(bytes))!=-1){
            bufferedOutputStream.write(bytes,0,len);
        }
        bufferedOutputStream.close();
        bufferedInputStream.close();
    }

    /**
     * 将获取的相应体 响应给浏览器
     * @param list
     * @throws IOException
     */
    private void responseStaticResources(List<String> list) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        OutputStream outputStream = socket.getOutputStream();
        //获取web开发的打印流
        PrintWriter out = new PrintWriter(outputStream,true);

        String ok = "HTTP/1.1 200 ok";
        String type = "Content-Type:text/html;charset=utf-8";
        out.println(ok);//写响应 状态行  200 成功的状态  ok成功
        out.println(type); // 消息报头
        out.println("");//空行

        stringBuilder.append(ok).append("\n\t").append(type).append("\n\t");
        LogUtil.getInstance().log("响应报文（状态行、消息报头、空白行、响应体）:\n\t"
                +stringBuilder.toString(),Grade.HTTP_INFO_CH);

        //响应体
        for (String line:list) {
            out.println(line);
        }
        out.close();
    }
}
