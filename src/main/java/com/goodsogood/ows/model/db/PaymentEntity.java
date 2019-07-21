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
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "zcy_payment")
/**
 *  支付记录表
 */
public class PaymentEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_payment.payment_id}")
    @NotNull(message = "{NotBlank.zcy_payment.payment_id}")
    @Column(name = "payment_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long paymentId;

    @ApiModelProperty(value = "订单号")
    @Column(name = "order_no")
    public String orderNo;

    @ApiModelProperty(value = "订单金额")
    @Column(name = "payment_total_money")
    public double totalMoney;

    @ApiModelProperty(value = "支付金额")
    @Column(name = "payment_real_money")
    public double realMoney;

    @ApiModelProperty(value = "订单类型 1：idea 确权 2：委托书")
    @Column(name = "payment_type")
    public Integer type;
    @ApiModelProperty(value = "对应类型的唯一标识")
    @Column(name = "good_id")
    public Long goodId;

    @ApiModelProperty(value = "对应标题")
    @Column(name = "good_title")
    public String goodName;

    @ApiModelProperty(value = "操作支付人")
    @Column(name = "user_id")
    public Long userId;
    @ApiModelProperty(value = "支付人名称")
    @Column(name = "user_name")
    public String userName;

    @ApiModelProperty(value = "是否支付")
    @Column(name = "pay_status")
    public String Status;

    @ApiModelProperty(value = "支付方式")
    @Column(name = "pay_way")
    public String payWay;

    @ApiModelProperty(value = "数量")
    @Column(name = "payment_number")
    public Integer number;

    @ApiModelProperty(value = "微信订单号")
    @Column(name = "wx_order_no")
    public String wxOrderNo;

    @ApiModelProperty(value = "支付时间")
    @Column(name = "payment_time")
    public Date payTime;

    @ApiModelProperty(value = "添加时间")
    @Column(name = "payment_addtime")
    public Date addTime;
}
