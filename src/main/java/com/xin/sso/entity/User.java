package com.xin.sso.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author xkk
 * @date 2021/5/29/029 17:49:49
 */
@Data
public class User {
    private String id;
    private String userName;
    private String userAlias;
    private String userPassword;
    private String sex;
    private String userEmail;
    private String userPhone;
    private Date registerDate;
    private String userState;
    private String remark;
}
