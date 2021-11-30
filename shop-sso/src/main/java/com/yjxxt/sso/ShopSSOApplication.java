package com.yjxxt.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yjxxt.sso.mapper")
public class ShopSSOApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopSSOApplication.class,args);
    }
}
