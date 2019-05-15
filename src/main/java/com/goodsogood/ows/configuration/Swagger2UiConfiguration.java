package com.goodsogood.ows.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author xuliduo
 * @date 07/02/2018
 * @description class Swagger2UiConfiguration
 */
@Configuration
@EnableSwagger2
public class Swagger2UiConfiguration extends WebMvcConfigurerAdapter {
    @Value("${swagger.show}")
    private boolean swaggerShow = false;

    @Bean
    public Docket api() {
//        //设置请求头
//        List<Parameter> pars = new ArrayList<>();
//
//        ParameterBuilder token = new ParameterBuilder();
//        token.name("_tk").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false);
//        pars.add(token.build());
//
//        ParameterBuilder uid = new ParameterBuilder();
//        uid.name("_uid").description("用户id").modelRef(new ModelRef("string")).parameterType("header").required(false);
//        pars.add(uid.build());
//
//        ParameterBuilder name = new ParameterBuilder();
//        name.name("_un").description("用户名").modelRef(new ModelRef("string")).parameterType("header").required(false);
//        pars.add(name.build());
//
//        ParameterBuilder type = new ParameterBuilder();
//        type.name("_type").description("1-企业|2-组织").modelRef(new ModelRef("string")).parameterType("header").required(false);
//        pars.add(type.build());
//
//        ParameterBuilder oid = new ParameterBuilder();
//        oid.name("_oid").description("企业或者组织id").modelRef(new ModelRef("string")).parameterType("header").required(false);
//        pars.add(oid.build());
//
//        ParameterBuilder oidType = new ParameterBuilder();
//        oidType.name("_org_type").description("组织类型").modelRef(new ModelRef("int")).parameterType("header").required(false);
//        pars.add(oidType.build());
//
//        ParameterBuilder oidName = new ParameterBuilder();
//        oidName.name("_org_name").description("组织名称").modelRef(new ModelRef("string")).parameterType("header").required(false);
//        pars.add(oidName.build());
//
//        ParameterBuilder cl = new ParameterBuilder();
//        cl.name("_cl").description("渠道").modelRef(new ModelRef("string")).parameterType("header").required(false);
//        pars.add(cl.build());

        //Register the controllers to swagger
        //Also it is configuring the Swagger Docket
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerShow)
                .groupName("OWS-zcy")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.goodsogood.ows.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("OWS-zcy")
                .description("OWS-zcy")
                .version("0.0.0.1")
                .build();
    }

    // spring boot 2 才需要
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        if (this.swaggerShow) {
//            registry.addResourceHandler("swagger-ui.html")
//                    .addResourceLocations("classpath:/META-INF/resources/");
//
//            registry.addResourceHandler("/webjars/**")
//                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
//        }
//    }
}
