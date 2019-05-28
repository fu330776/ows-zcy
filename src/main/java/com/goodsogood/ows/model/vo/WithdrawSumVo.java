package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class WithdrawSumVo {

    public  Long userId;

    public BigDecimal money;

    public  Integer isPay;

    public  String userName;

}
