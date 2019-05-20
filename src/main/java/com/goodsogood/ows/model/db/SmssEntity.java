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

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "zcy_smss")
public class SmssEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_smss.sms_id}")
    @NotNull(message = "{NotBlank.zcy_smss.sms_id}")
    @Column(name = "sms_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long smsId;

    @ApiModelProperty(value = "电话号码")
    @Column(name = "sms_phone")
    public  String smsPhone;

    @ApiModelProperty(value = "短信类型 1、注册账号 2、修改密码")
    @Column(name = "sms_type")
    public  Integer smsType;

    @ApiModelProperty(value = "短信发送类型 1、文字 2、语音")
    @Column(name = "sms_send_type")
    public  Integer smsSendType;

    @ApiModelProperty(value = "验证码")
    @Column(name = "sms_code")
    public  String smsCode;

    @ApiModelProperty(value = "短信发送内容")
    @Column(name = "sms_content")
    public  String smsContent;

    @ApiModelProperty(value = "发送次数")
    @Column(name = "sms_send_frequency")
    public  Integer smsSendFrequency;

    @ApiModelProperty("发送时间（重复发送短信更新该字段）")
    public  Data addtime;

    @ApiModelProperty(value = "过期时间")
    @Column(name = "sms_expire_date")
    public  Data smsExpireDate;
}
