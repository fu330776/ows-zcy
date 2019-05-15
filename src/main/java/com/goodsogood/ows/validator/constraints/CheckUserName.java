package com.goodsogood.ows.validator.constraints;

import com.goodsogood.ows.validator.UserNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author xuliduo
 * @date 2018/5/30
 * @description class checkUserName
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {UserNameValidator.class})
public @interface CheckUserName {
    //默认错误消息
    String message() default "{user.name.not.start}";

    String start() default "";

    //分组
    Class<?>[] groups() default {};

    //负载
    Class<? extends Payload>[] payload() default {};

    //指定多个时使用
    @Target({FIELD})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        CheckUserName[] value();
    }
}
