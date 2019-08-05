package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
@ApiModel
public class UserMoneyVo {
//    public BigDecimal noMoney;
//    public BigDecimal tooMoney;

    @ApiModelProperty(value = "当前可操作金额")
    public  double nowMoney;
    @ApiModelProperty(value = "历史收入")
    public double sumsMoney;
    @ApiModelProperty(value = "已提现金额")
    public double outsMoney;
    @ApiModelProperty(value = "提现中金额")
    public   double nosMoney;
}
