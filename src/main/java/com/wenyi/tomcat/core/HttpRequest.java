package com.wenyi.tomcat.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.tomcat.core;
 * @author: Administrator;
 * @date: 2021/4/13 20:27;
 * @Description: 对http协议进行处理
 */
public class HttpRequest {
    //封装Http请求协议的内容
    public List<String> requestList = new ArrayList<>();
    //封装用户的请求参数（动态请求）
    public Map<String,String> paramMap = new HashMap<>();
    /**
     * 将获取的HTML 请求头信息 存储在requestList集合中
     * @param line
     */
    public void addRequestInfo(String line){
        requestList.add(line);
    }

    /**
     * 封装用户请求过来的参数 封装到Map集合中
     * @param url 将get请求的地址进行解析
     */
    public void parseParam(String url) {
        //查找是否有参数
        int index = url.indexOf("?");
        if (index!=-1){
            //处理有参数
            String params = url.split("\\?")[1];
            if (url.contains("&")){
                //表示有多个参数
                String[] split = params.split("&");
                for (String param:split) {
                    String[] values = param.split("=");
                    paramMap.put(values[0],values[1]);
                }
            }else {
                //只要一个参数
                String[] values = params.split("=");
                paramMap.put(values[0],values[1]);
            }
        }
    }

    /**
     * 根据Map的key值找value值 如果找不到 则默认放回""
     * @param name
     * @return
     */
    public String getParameter(String name){
        boolean b = paramMap.containsKey(name);
        return b?paramMap.get(name):"";
    }
}
