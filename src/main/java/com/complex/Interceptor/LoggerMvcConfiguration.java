package com.complex.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class LoggerMvcConfiguration implements WebMvcConfigurer {
    private List<String> urls = new ArrayList<>();
    @Autowired
    LoggerInterceptor logInterceptor;

    public List<String> getUrls() {
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
        registry.addInterceptor(logInterceptor).addPathPatterns("/**")
                .excludePathPatterns(getUrls());
    }

}

