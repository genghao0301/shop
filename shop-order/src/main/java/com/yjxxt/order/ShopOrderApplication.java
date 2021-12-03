package com.yjxxt.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yjxxt.order.mapper")
public class ShopOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopOrderApplication.class,args);
    }
}
