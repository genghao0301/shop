package com.yjxxt.portal.config;


import com.yjxxt.portal.interceptors.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器生效 配置类
 */
@Configuration
public class ManagerConfig extends WebMvcConfigurerAdapter {


    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**","/images/**",
                        "/js/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/logout");
    }
}
