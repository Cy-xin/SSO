package com.xin.sso.controller;

import com.xin.sso.dao.UserDao;
import com.xin.sso.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xkk
 * @date 2021/5/29/029 18:14:31
 */
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping("/selectUsers")
    public String selectUsers() {
        List<User> users = userDao.selectList(null);
        for (User user : users) {
            System.out.println(user.toString());
        }
        return null;
    }

}
