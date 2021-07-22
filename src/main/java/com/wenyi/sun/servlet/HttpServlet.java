package com.wenyi.sun.servlet;

import com.wenyi.tomcat.core.HttpRequest;
import com.wenyi.tomcat.core.HttpResponse;

/** sun设置规范
 * @project: tomcat-wenyi;
 * @package: com.wenyi.sun.servlet;
 * @author: Administrator;
 * @date: 2021/4/13 23:48;
 * @Description: tomcat与用户 操作的规范
 */
public interface HttpServlet {
    void service(HttpRequest httpRequest, HttpResponse httpResponse);
}
