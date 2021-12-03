package com.yjxxt.order.config;


import com.yjxxt.order.interceptors.OrderLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class OrderConfig extends WebMvcConfigurerAdapter {

    @Bean
    public OrderLoginInterceptor loginInterceptor(){
        return new OrderLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/images/**","/js/**");

    }
}
