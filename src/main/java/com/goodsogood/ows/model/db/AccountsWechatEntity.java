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
@Table(name = "zcy_accounts_wechat")
public class AccountsWechatEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_accounts_wechat.wechat_id}")
    @NotNull(message = "{NotBlank.zcy_accounts_wechat.wechat_id}")
    @Column(name = "wechat_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public  Long wechatId;

    @ApiModelProperty(value = "账号ID")
    @Column(name = "account_id")
    public  Long accountId;

    @ApiModelProperty(value = "微信OPPENID")
    @Column(name = "wechat_openn_id")
    public  String wechatOpennId;

    @ApiModelProperty(value = "微信名")
    @Column(name = "wechat_name")
    public  String wechatName;

    public Date addtime;
}
