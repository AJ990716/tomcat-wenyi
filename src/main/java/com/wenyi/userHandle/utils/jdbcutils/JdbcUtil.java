package com.wenyi.userHandle.utils.jdbcutils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.userHandle.utils.jdbcutil;
 * @author: Administrator;
 * @date: 2021/4/14 16:46;
 * @Description: 创建jdbc数据库连接的工具类
 */
public class JdbcUtil {
    //数据源 c3p0的实例
    private static DataSource dataSource = new ComboPooledDataSource();

    //处理多线程的事务连接安全问题  防止多个线程操作同一个事务
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    public static DataSource getDataSource(){
        return dataSource;
    }

    /**
     * 获取数据源的普通连接
     * @return
     */
    public static Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("获取连接失败");
    }

    /**
     * 获取事务连接
     * @return
     */
    public static Connection getTransConnection(){
        Connection connection = threadLocal.get();
        if (connection==null){
            //如果该线程无conn对象 则获取一个新的连接对象 并填充到该线程集合中
            connection = getConnection();
            threadLocal.set(connection);
        }
        return connection;
    }

    /**
     * 获取事务预处理对象
     * @param sql
     * @return
     */
    public static PreparedStatement getTransPreparedStatement(String sql){
        try {
            return getTransConnection().prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("获取事务预处理对象失败");
    }

    /**
     * 开启事务
     */
    public static void startTransaction(){
        try {
            Connection transConnection = getTransConnection();
            if (transConnection.getAutoCommit())
                //如果未开启事务则 将事务开启
                transConnection.setAutoCommit(false);
            else
                throw new RuntimeException("请不要重复开启事务");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public static void commit(){
        Connection connection = threadLocal.get();
        if (connection==null)
            throw new RuntimeException("请先开启事务");
        try {
            //提交事务
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交事务
     */
    public static void rollback(){
        Connection connection = threadLocal.get();
        if (connection==null)
            throw new RuntimeException("请先开启事务");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭事务连接
     */
    public static void closeTransConnection(){
        Connection connection = threadLocal.get();
        if (connection!=null) {
            try {
                //如果线程事务连接不是null 则关闭连接并且 将该线程资源从集合中移除
                connection.close();
                threadLocal.remove();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
