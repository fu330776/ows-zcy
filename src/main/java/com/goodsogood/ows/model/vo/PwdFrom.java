package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class PwdFrom {
    public  String code;
    public  String Pwd;
    public  String NewPwd;
    public  String Phone;
    public  Long userId;
}
