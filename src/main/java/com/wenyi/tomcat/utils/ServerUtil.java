package com.wenyi.tomcat.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.tomcat.utils;
 * @author: Administrator;
 * @date: 2021/4/13 17:28;
 * @Description: 处理server.xml的配置文件
 */
public class ServerUtil {
    /**
     * 使用dom4j解析xml文件并且返回端口
     * @return 返回端口
     */
    public static int getPort(){
        try {
            //创建一个SAXReader解析器
            SAXReader saxReader = new SAXReader();
            //读取配置文件
            File file = new File("./src/main/resources/conf/server.xml");
            Document document = saxReader.read(file);
            //获取根节点
            Element root = document.getRootElement();
            //获取指定Connector节点的属性port
            Element connector = root.element("Connector");
            String port = connector.attribute("port").getText();
            if (port!=null)
                return Integer.parseInt(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 80;
    }
}
