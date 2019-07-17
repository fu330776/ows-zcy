package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
@ApiModel
public class WithdrawsVo {
    public Long withdrawId;

    @ApiModelProperty(value = "提现流水号")
    @Column(name = "withdraw_number")
    public String withdrawNumber;

    @ApiModelProperty(value = "提现金额")
    @Column(name = "withdraw_money")
    public float withdrawMoney;

    @ApiModelProperty(value = "提现人")
    @Column(name = "user_id")
    public Long userId;

    @ApiModelProperty(value = "是否提现：1、未提现 2、已提现")
    @Column(name = "is_withdraw")
    public Integer isWithdraw;

    public Date addtime;

    @ApiModelProperty(value = "打款时间")
    public Date paytime;

    public String userName;
    @ApiModelProperty(value = "银行账号")
    public  String bankNumber;

    public  String userCardholderName;
    public  String    userCardholderPhone;
    public  String  userCardholderIdcard;
}
