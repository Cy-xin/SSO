package com.xin.sso.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xin.sso.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xkk
 * @date 2021/5/29/029 18:05:35
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

    /**
     * 查询当前用户是否存在
     * @param userName
     * @param userPassword
     * @return
     */
    User loginUser(@Param("userName") String userName, @Param("userPassword") String userPassword);
}
