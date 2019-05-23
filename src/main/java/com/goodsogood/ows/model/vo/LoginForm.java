package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class LoginForm {

    public  String phone;

    public  String password;

    public  String code;

}
