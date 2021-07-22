package com.wenyi.tomcat.enumeration;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.file;
 * @author: Administrator;
 * @date: 2021/4/13 19:33;
 * @Description: 设置消息等级
 */
public enum Grade {
    MESSAGE,ERROR,TEST,USER_INFO,HTTP_INFO,
    MESSAGE_CH("消息"),ERROR_CH("错误"),TEST_CH("测试"),
    USER_INFO_CH("用户信息"),HTTP_INFO_CH("HTTP协议");
    private String message;
    Grade(){}
    Grade(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return message==null?super.toString():message;
    }
}
