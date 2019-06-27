package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class PwdFrom {
    public  String code;
    public  String pwd;
    public  String newPwd;
    public  String phone;
    public  Long userId;
}
