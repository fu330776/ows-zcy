package com.goodsogood.ows.configuration;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.MultipartConfigElement;

@Configuration
public class MultipartConfiguration {
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大25M,DataUnit提供5中类型B,KB,MB,GB,TB
        factory.setMaxFileSize("25MB");
        /// 设置总上传数据总大小30M
        factory.setMaxRequestSize("30MB");
        return factory.createMultipartConfig();
    }
}
