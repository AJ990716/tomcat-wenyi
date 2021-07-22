package com.wenyi.tomcat.enumeration;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.file;
 * @author: Administrator;
 * @date: 2021/4/13 19:26;
 * @Description: 枚举设置LOG文件位置
 */
public enum Path {
    LOG_PATH("./src/main/java/com/wenyi/tomcat/logs/log.txt");
    private String path;
    Path(String path){
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
