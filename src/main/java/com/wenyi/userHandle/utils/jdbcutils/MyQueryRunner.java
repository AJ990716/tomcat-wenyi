package com.wenyi.userHandle.utils.jdbcutils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.userHandle.utils.jdbcutil;
 * @author: Administrator;
 * @date: 2021/4/14 17:23;
 * @Description: 我的queryRunner 处理sql语句对象
 */
public class MyQueryRunner {
    //QueryRunner需要的数据源对象
    private DataSource dataSource;

    /**
     * 构造时需要一个数据源
     * @param dataSource
     */
    public MyQueryRunner(DataSource dataSource){
        this.dataSource = dataSource;
    }

    /**
     * 用来处理DML语句 增删改
     * @param sql   增删改sql语句
     * @param params 给sql语句中的占位符赋值的值
     * @return  返回在数据库中影响的行数
     * @throws SQLException
     */
    public int update(String sql,Object... params) throws SQLException {
        int result = -1;
        //获取数据源连接
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //将传入的参数赋给预处理对象 参数赋值给sql的占位符
        generateParams(preparedStatement,params);
        //执行增删改
        result = preparedStatement.executeUpdate();
        //关闭连接 返回修改的条目
        close(null,preparedStatement,connection);
        return result;
    }

    /**
     * 处理DQL语句 查询
     * @param sql 查询语句
     * @param handler 查询结果处理器
     * @param params 给sql语句中的占位符赋值的值
     * @param <T> 返回指定的结果
     * @return
     */
    public <T> T query(String sql,MyResultSetHandler<T> handler,Object... params) throws Exception{
        T obj = null;
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //执行之前将传入的参数赋值给sql的占位符
        generateParams(preparedStatement,params);
        ResultSet resultSet = preparedStatement.executeQuery();
        //使用传入的handler来将结果处理成我们想要的结果
        obj = handler.handler(resultSet);
        //关闭连接 返回处理后的结果对象
        close(resultSet,preparedStatement,connection);
        return obj;
    }

    /**
     * 循环传入的参数 分别赋值给预处理对象
     * @param preparedStatement
     * @param params
     */
    private void generateParams(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        int length = params.length;
        for (int i = 0; i < length; i++) {
            //数据库是以1为开始
            preparedStatement.setObject(i+1,params[i]);
        }
    }

    /**
     * 关闭所有资源
     * @param rs
     * @param pstmt
     * @param conn
     */
    public static void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        if (rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                if (pstmt!=null){
                    try {
                        pstmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally {
                        if (conn!=null) {
                            try {
                                conn.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
