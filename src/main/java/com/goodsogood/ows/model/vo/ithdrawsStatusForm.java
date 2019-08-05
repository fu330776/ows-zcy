package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel
public class ithdrawsStatusForm {
    @ApiModelProperty(value = "用户id")
    public  Long userId;
    @ApiModelProperty(value = "指定提现记录编号")
    public Long withdraw_id;
    @ApiModelProperty(value = "状态：1：未受理，2：已受理  4：提现完成',")
    public int status;
    @ApiModelProperty(value = "金额")
    public BigDecimal money;
}
