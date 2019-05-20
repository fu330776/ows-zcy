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
@Table(name = "zcy_accounts")
public class AccountsEntity extends BaseEntity {

    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_accounts.account_id}")
    @NotNull(message = "{NotBlank.zcy_accounts.account_id}")
    @Column(name = "account_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long AccountId;

    @ApiModelProperty(value = "登录账号")
    @Column(name = "phone")
    public  String phone;

    @ApiModelProperty(value ="密码" )
    @Column(name = "pass_word")
    public  String PassWord;

    @ApiModelProperty(value = "明文密码")
    @Column(name = "pass_word_laws")
    public  String PassWordLaws;

    @ApiModelProperty(value = "是否启禁用 1、启用 2、禁用")
    @Column(name = "enable")
    public  Integer Enable;

    @ApiModelProperty(value = "添加时间")
    public Date addtime;
}
