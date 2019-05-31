package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel
@Data
public class PaynentPayForm {

//    /**
//     *  支付金额
//     */
//    @ApiModelProperty(value = "支付金额")
//    public BigDecimal realMoney;
    /**
     *  支付状态
     */
    @ApiModelProperty(value = "支付状态")
    public String Status;
    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    public String payWay;
    /**
     *  微信会址订单号
     */
    @ApiModelProperty(value = "微信会址订单号")
    public String wxOrderNo;

    /**
     *  支付时间
     */
    @ApiModelProperty(value = "支付时间")
    public Date payTime;
    /**
     *  我的订单号
     */
    @ApiModelProperty(value = "我的订单号")
    public String orderNo;
}
