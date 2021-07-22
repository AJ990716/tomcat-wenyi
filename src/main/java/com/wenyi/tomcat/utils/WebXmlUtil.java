package com.wenyi.tomcat.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.tomcat.utils;
 * @author: Administrator;
 * @date: 2021/4/13 23:07;
 * @Description: 用于解析web.xml 中所有地址与对象路径反射的对象 全部放入map的key和value中
 */
public class WebXmlUtil {
    /**
     * 将web.xml 中所有地址与对象路径反射的对象 全部放入map的key和value中
     * String表示访问地址,Object表示反射的对象
     * @return 返回map对象String表示访问地址,Object表示反射的对象
     */
    public static Map<String,Object> parserXml() throws Exception {
        Map<String,Object> map = new HashMap<>();
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("./src/main/webapp/WEB-INF/web.xml"));
        Element root = document.getRootElement();
        Element servlet = root.element("servlet");
        //获取反射地址
        String clazzUrl = servlet.element("servlet-class").getText();
        Element mapping = root.element("servlet-mapping");
        //获取地址
        String url = mapping.element("url-pattern").getText();
        //反射创建对象
        Object obj = Class.forName(clazzUrl).newInstance();
        map.put(url,obj);
        return map;
    }
}
