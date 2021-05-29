package com.xin.sso.service;

import com.xin.sso.entity.User;
import org.springframework.stereotype.Service;

/**
 * @author xkk
 * @date 2021/5/29/029 18:04:27
 */
@Service
public interface UserService {

    /**
     * 查询当前用户是否存在
     * @param userName
     * @param userPassword
     * @return
     */
    User loginUser(String userName, String userPassword);

}
