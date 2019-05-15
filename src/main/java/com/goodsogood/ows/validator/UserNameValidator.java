package com.goodsogood.ows.validator;

import com.goodsogood.ows.validator.constraints.CheckUserName;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author xuliduo
 * @date 2018/5/30
 * @description class UserNameValidator
 */
public class UserNameValidator implements ConstraintValidator<CheckUserName, String> {

    @Override
    public void initialize(CheckUserName checkUserName) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        ConstraintValidatorContextImpl context = (ConstraintValidatorContextImpl) constraintValidatorContext;
        String start = (String) context.getConstraintDescriptor().getAttributes().get("start");
        //null时不进行校验
        return value == null || !value.startsWith(start);
    }
}
