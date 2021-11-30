package com.yjxxt.manager.config;


import com.yjxxt.manager.interceptors.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ManagerConfig extends WebMvcConfigurerAdapter {

/*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bootstrap/**").addResourceLocations("classpath:/bootstrap/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/bootstrap/");
        registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/bootstrap/");
        registry.addResourceHandler("/bootstrap/**").addResourceLocations("classpath:/bootstrap/");
    }*/

    @Bean
    public LoginInterceptor loginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/bootstrap/**",
                        "/css/**","/dist/**","/images/**",
                        "/img/**","js/**","/plugins/**")
                .excludePathPatterns("/login")
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/logout");
    }
}
