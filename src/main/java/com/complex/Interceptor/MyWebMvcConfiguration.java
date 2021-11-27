package com.complex.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MyWebMvcConfiguration implements WebMvcConfigurer {
    private List<String> urls = new ArrayList<>();
    @Autowired
    LoginInterceptor loginHandlerInterceptor;

    public List<String> getUrls() {
        urls.add("/permission/web/login");    // login url请求
        urls.add("/user_index.html");
        urls.add("/index/login");
        urls.add("/img/*");
        urls.add("/static/**");
        urls.add("/js/*");
        urls.add("/css/*");
        return urls;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有的请求
        registry.addInterceptor(loginHandlerInterceptor).addPathPatterns("/**")
                .excludePathPatterns(getUrls());
    }

}

