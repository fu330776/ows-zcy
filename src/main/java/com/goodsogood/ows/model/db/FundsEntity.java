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
@Table(name = "zcy_funds")
public class FundsEntity extends BaseEntity {

    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_funds.fund_id}")
    @NotNull(message = "{NotBlank.zcy_funds.fund_id}")
    @Column(name = "fund_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long fundId;

    @ApiModelProperty(value = "用户ID")
    @Column(name = "user_id")
    public Long userId;

    @ApiModelProperty(value = "项目名称")
    public String title;

    @ApiModelProperty(value = "项目简介")
    public String introduction;

    @ApiModelProperty(value = "身份：1、个人 2、医疗结构")
    public int identity;

    @ApiModelProperty(value = "添加时间")
    public Date addtime;

}
