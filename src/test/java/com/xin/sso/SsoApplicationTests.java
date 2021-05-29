package com.xin.sso;

import com.xin.sso.dao.UserDao;
import com.xin.sso.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SsoApplicationTests {

    @Autowired
    private UserDao userDao;

    @Test
    void contextLoads() {
        List<User> users = userDao.selectList(null);
        for (User user : users) {
            System.out.println(user.toString());
        }
    }

}
