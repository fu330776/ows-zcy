package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
@Data
@ApiModel
public class AuthVo {

    public  String userid;
    public  String token;
    public Date  time;
}
