package com.wenyi.userHandle.dao;

import com.wenyi.userHandle.entity.User;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.userHandle.dao.impl;
 * @author: Administrator;
 * @date: 2021/4/14 18:46;
 * @Description:
 */
public interface UserDao {
    /**
     * 连接数据库 处理sql语句 返回影响条目
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 连接数据库 将查询的数据处理成对象返回
     * @param user
     * @return
     */
    User login(User user);
}
