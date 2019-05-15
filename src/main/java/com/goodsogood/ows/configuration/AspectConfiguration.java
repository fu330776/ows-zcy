package com.goodsogood.ows.configuration;

import com.goodsogood.log4j2cm.aop.HttpLogAspect;
import com.goodsogood.log4j2cm.component.MonitorProps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author xuliduo
 * @date 26/01/2018
 * @description 为aop配置一个自动代理
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan
public class AspectConfiguration {
    /**
     * 加载 yml 配置文件中 monitor-props 节点的配置信息
     *
     * @return MonitorProps
     */
    @Bean
    public MonitorProps monitorProps() {
        return new MonitorProps();
    }

    /**
     * 获得aop对象，让EnableAspectJAutoProxy进行自动代理
     *
     * @return HttpLogAspect
     */
    @Bean
    public HttpLogAspect httpLogAspect() {
        return new HttpLogAspect(monitorProps());
    }
}
