package com.wenyi.tomcat.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.tomcat.utils;
 * @author: Administrator;
 * @date: 2021/4/13 20:39;
 * @Description: 根据url 进行判断与获取资源
 */
public class StaticResourcesUtils {
    /**
     * 判断传入的地址是否存在
     * @param url 传入的地址
     * @return
     */
    public static boolean isExist(String url) {
        String path = "./src/main/webapp"+url;
        File file = new File(path);
        return file.exists();
    }

    /**
     * 根据存在的url找到对应文件
     * @param url
     * @return
     */
    public static List<String> getStaticResources(String url) {
        List<String> list = new LinkedList<>();
        String path = "./src/main/webapp/"+url;
        BufferedReader bufferedReader = null;
        try {
            //获取字符输入流 读取静态资源
            bufferedReader = new BufferedReader(new FileReader(path));
            String line = bufferedReader.readLine();
            while (line!=null){
                list.add(line);
                line = bufferedReader.readLine();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
