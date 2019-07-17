package com.goodsogood.ows.model.vo;

import lombok.Data;

@Data
public class PayBean {
    private static final long serialVersionUID = -7551908500227408235L;

    /**
     * 用户订单号
     */
    private String orderNo;
    /**
     * 订单总金额
     */
    private String amount;
    /**
     * 用户实际 ip 地址
     */
    private String ip;

    /**
     * 支付方式
     *
     * 21: 微信 NATIVE 支付(二维码支付)
     * 22: 微信 JSAPI 支付
     * 23: 微信 H5 支付
     * 24: 微信 APP 支付
     * 25: 微信 小程序 支付
     */
    private int payType;

    /**
     * 微信 JSAPI/小程序 支付必传
     */
    private String openId;

}
