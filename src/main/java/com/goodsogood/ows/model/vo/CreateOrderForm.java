package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CreateOrderForm {
    public  Long gid;
    public  int type;
    public  String openId;

    @ApiModelProperty(value = " /**\n" +
            "     * 支付方式\n" +
            "     *\n" +
            "     * 21: 微信 NATIVE 支付(二维码支付)\n" +
            "     * 22: 微信 JSAPI 支付\n" +
            "     * 23: 微信 H5 支付\n" +
            "     * 24: 微信 APP 支付\n" +
            "     * 25: 微信 小程序 支付\n" +
            "     */")
    public int payType;
}
