package com.xin.sso.service.impl;

import com.xin.sso.dao.UserDao;
import com.xin.sso.entity.User;
import com.xin.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xkk
 * @date 2021/5/29/029 18:04:52
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User loginUser(String userName, String userPassword) {
        User user = userDao.loginUser(userName, userPassword);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }
}
