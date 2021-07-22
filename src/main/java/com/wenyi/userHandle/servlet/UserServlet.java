package com.wenyi.userHandle.servlet;

import com.wenyi.sun.servlet.HttpServlet;
import com.wenyi.tomcat.core.HttpRequest;
import com.wenyi.tomcat.core.HttpResponse;
import com.wenyi.tomcat.enumeration.Grade;
import com.wenyi.tomcat.utils.LogUtil;
import com.wenyi.userHandle.entity.User;
import com.wenyi.userHandle.service.UserService;
import com.wenyi.userHandle.service.impl.UserServiceImpl;

import java.io.PrintWriter;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.userHandle.servlet;
 * @author: Administrator;
 * @date: 2021/4/14 0:02;
 * @Description: sun中的规范实现接口
 */
public class UserServlet implements HttpServlet {
    UserService userService = new UserServiceImpl();
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        //因为tomcat不知道 请求的数据具体是什么，因此这里需要用户自己操作 请求数据
        //然后再操作响应方式 而sun规范接口 有个操作的方式接口，用户必须实现该接口才能操作
        String flag = httpRequest.getParameter("flag");

        //用户输入的对象
        String username = httpRequest.getParameter("username");
        String password = httpRequest.getParameter("password");
        //封装成对象
        User user = new User(username,password);

        if ("userRegister".equals(flag)){
            int result = userService.insertUser(user);
            LogUtil.getInstance().log("数据库中影响的数据条数:"+result, Grade.MESSAGE_CH);

            //获取响应的打印流
            PrintWriter out = httpResponse.getOut();
            if (result>0){
                LogUtil.getInstance().log("注册成功",Grade.MESSAGE_CH);
                out.println("<script>location.href='/view/login/index.html';</script>");  //登录成功首页
            }else {
                LogUtil.getInstance().log("注册失败",Grade.MESSAGE_CH);
                out.println("<script>alert('账号或密码错误!');location.href='/view/register/index.html';</script>");
            }
        }else if ("userLogin".equals(flag)){
            User loginUser = userService.login(user);
            LogUtil.getInstance().log("数据库中的对象为"+loginUser, Grade.MESSAGE_CH);

            //获取响应的打印流
            PrintWriter out = httpResponse.getOut();
            if (loginUser!=null){
                LogUtil.getInstance().log("登录成功",Grade.MESSAGE_CH);
                out.println("<script>location.href='/index.html';</script>");  //登录成功首页
            }else {
                LogUtil.getInstance().log("登录失败",Grade.MESSAGE_CH);
                out.println("<script>alert('账号或密码错误!');location.href='/view/login/index.html';</script>");
            }
        }
    }
}
