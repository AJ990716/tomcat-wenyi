package com.wenyi.userHandle.dao.impl;

import com.wenyi.userHandle.dao.UserDao;
import com.wenyi.userHandle.entity.User;
import com.wenyi.userHandle.utils.jdbcutils.JdbcUtil;
import com.wenyi.userHandle.utils.jdbcutils.MyBeanHandler;
import com.wenyi.userHandle.utils.jdbcutils.MyQueryRunner;

import java.sql.SQLException;
import java.util.List;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.userHandle.dao;
 * @author: Administrator;
 * @date: 2021/4/14 0:05;
 * @Description: 处理与数据库的交互
 */
public class UserDaoImpl implements UserDao {
    private MyQueryRunner myQueryRunner = new MyQueryRunner(JdbcUtil.getDataSource());

    /**
     * 连接数据库 处理sql语句 返回影响条目
     * @param user
     * @return
     */
    public int insertUser(User user){
        try {
            return myQueryRunner.update("INSERT INTO user VALUES (null,?,?)",user.getUsername(),user.getPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 连接数据库 将查询的数据处理成对象返回
     * @param user
     * @return
     */
    public User login(User user){
        try {
            User query = myQueryRunner.query("SELECT * FROM user WHERE username = ? and password = ?",
                    new MyBeanHandler<>(User.class), user.getUsername(), user.getPassword());
            return query;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
