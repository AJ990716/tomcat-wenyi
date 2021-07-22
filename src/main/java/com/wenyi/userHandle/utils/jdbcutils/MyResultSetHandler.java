package com.wenyi.userHandle.utils.jdbcutils;

import java.sql.ResultSet;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.userHandle.utils.jdbcutils;
 * @author: Administrator;
 * @date: 2021/4/14 17:30;
 * @Description: 接口方法 抽象为处理ResultSet 为T对象
 * 重点还是具体的处理对象
 */
public interface MyResultSetHandler<T> {
    /**
     * 接口方法 抽象为处理ResultSet 为T对象
     * 重点还是具体的处理对象
     * @param resultSet 获取的查询结果值
     * @return
     * @throws Exception
     */
    T handler(ResultSet resultSet) throws Exception;
}
