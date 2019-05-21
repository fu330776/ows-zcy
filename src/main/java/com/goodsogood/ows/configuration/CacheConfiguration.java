package com.goodsogood.ows.configuration;


import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfiguration {
    public static Map<String, Object> cache = new HashMap<>();
    public static Date GetDate(Long times) {
        Long nowValue =new Date().getTime();//date的毫秒数
        Long afterHour = nowValue + times;//date加一个小时的毫秒数

        Date date = new Date(afterHour);
        return  date;
    }
}
