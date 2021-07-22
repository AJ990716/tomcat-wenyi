package com.wenyi.tomcat.core;

import java.io.PrintWriter;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.tomcat.core;
 * @author: Administrator;
 * @date: 2021/4/13 23:42;
 * @Description: 封装响应的输出流
 */
public class HttpResponse {
    private PrintWriter out;
    public HttpResponse(PrintWriter out){
        this.out = out;
    }

    public PrintWriter getOut() {
        out.println("HTTP/1.1 200 ok");  //正常返回
        out.println("Content-Type:text/html;charset=utf-8"); // 响应报头
        out.println("");//空行
        return out;
    }
}
