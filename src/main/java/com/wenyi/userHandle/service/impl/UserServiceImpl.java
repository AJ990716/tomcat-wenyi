package com.wenyi.userHandle.service.impl;

import com.wenyi.userHandle.dao.UserDao;
import com.wenyi.userHandle.dao.impl.UserDaoImpl;
import com.wenyi.userHandle.entity.User;
import com.wenyi.userHandle.service.UserService;

/**
 * @project: tomcat-wenyi;
 * @package: com.wenyi.userHandle.service.impl;
 * @author: Administrator;
 * @date: 2021/4/14 18:56;
 * @Description:
 */
public class UserServiceImpl implements UserService {
    public UserDao  userDao = new UserDaoImpl();
    @Override
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public User login(User user) {
        return userDao.login(user);
    }
}
