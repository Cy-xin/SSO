package com.xin.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xkk
 */
@SpringBootApplication
@MapperScan("com.xin.sso.dao")
public class SsoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoApplication.class, args);
    }

}
