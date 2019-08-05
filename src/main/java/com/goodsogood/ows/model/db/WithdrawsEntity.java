package com.goodsogood.ows.model.db;

import com.goodsogood.ows.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "zcy_withdraws")
public class WithdrawsEntity extends BaseEntity {

    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_withdraws.withdraw_id}")
    @NotNull(message = "{NotBlank.zcy_withdraws.withdraw_id}")
    @Column(name = "withdraw_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long withdrawId;

    @ApiModelProperty(value = "提现流水号")
    @Column(name = "withdraw_number")
    public String withdrawNumber;

    @ApiModelProperty(value = "提现金额")
    @Column(name = "withdraw_money")
    public double withdrawMoney;

    @ApiModelProperty(value = "提现人")
    @Column(name = "user_id")
    public Long userId;

    @ApiModelProperty(value = "是否提现：1、未提现 2、已提现")
    @Column(name = "is_withdraw")
    public Integer isWithdraw;

    public Date addtime;

    @ApiModelProperty(value = "打款时间")
    public Date paytime;

    @ApiModelProperty(value = "0: 无状态 1：未受理，2：已受理  4：提现完成")
    public Integer status;

}
