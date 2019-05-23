package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@ApiModel
@Data
public class FundsVo {

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

    public String userName;
}
