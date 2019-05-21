package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class LoginForm {

    public  String Phone;

    public  String Password;

    public  String code;

}
