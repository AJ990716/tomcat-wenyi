package com.wenyi;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wenyi.tomcat.enumeration.Grade;
import com.wenyi.tomcat.utils.LogUtil;
import com.wenyi.tomcat.utils.ServerUtil;
import com.wenyi.userHandle.entity.User;
import com.wenyi.userHandle.utils.jdbcutils.JdbcUtil;
import com.wenyi.userHandle.utils.jdbcutils.MyBeanHandler;
import com.wenyi.userHandle.utils.jdbcutils.MyQueryRunner;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi;
 * @author: Administrator;
 * @date: 2021/4/13 18:21;
 * @Description: 测试文件路径
 */
public class FunctionTest {
    /**
      * @author: Administrator
      * @createTime: 2021/4/13 18:24
      * @Description: 测试获取端口
      */
    @Test
    public void testServerUtil(){
        int port = ServerUtil.getPort();
        System.out.println("获取的端口: "+port);
    }
    /**
      * @author: Administrator
      * @createTime: 2021/4/14 18:52
      * @Description: 测试日志功能
      */
    @Test
    public void testLogUtil() throws IOException {
        LogUtil logUtil1 = LogUtil.getInstance();
        LogUtil logUtil = LogUtil.getInstance("yyyy-MM-dd HH:mm:ss:SSS");
        LogUtil instance = LogUtil.getInstance("1231312","ss");
        System.out.println(logUtil==logUtil1);
        System.out.println(logUtil==instance);

        logUtil.log("测试文本", Grade.TEST_CH);
    }

    /**
      * @author: Administrator
      * @createTime: 2021/4/14 18:54
      * @Description: 测试c3p0 获取是否成功
      */
    @Test
    public void testC3P0() throws SQLException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        System.out.println(dataSource);
        Connection connection = dataSource.getConnection();
        Assert.assertNotNull(connection);
    }

    /**
     * 测试jdbcUtil功能
     * @throws SQLException
     */
    @Test
    public void testJdbcUtil() throws SQLException {
        MyQueryRunner myQueryRunner = new MyQueryRunner(JdbcUtil.getDataSource());
        /*
        int update = myQueryRunner.update("INSERT INTO user(username,password) VALUES(?,?)",
                "admin", "aj990716");
        Assert.assertEquals(1,update);
        */
        JdbcUtil.startTransaction();
        JdbcUtil.getTransPreparedStatement("INSERT INTO user(username,password) VALUES('admin','aj990716')");
        JdbcUtil.getTransPreparedStatement("INSERT INTO user(username,password) VALUES('admin','admin')");
        JdbcUtil.rollback();//数据回滚
    }

    /**
     * 语句出现问题单独执行query
     */
    @Test
    public void testJdbcUtilQuery() throws Exception {
        MyQueryRunner myQueryRunner = new MyQueryRunner(JdbcUtil.getDataSource());
        User query = myQueryRunner.query("SELECT * FROM user WHERE username = 'admin' AND password = 'aj990716'",
                new MyBeanHandler<>(User.class));
        System.out.println(query);
        query = myQueryRunner.query("SELECT * FROM user WHERE username = ? and password = ?",
                new MyBeanHandler<>(User.class), "admin", "aj990716");
        System.out.println(query);
    }
}
