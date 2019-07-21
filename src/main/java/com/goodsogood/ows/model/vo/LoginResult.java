package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class LoginResult {
    public  Boolean isb;
    public  String msg;
}
