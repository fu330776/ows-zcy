package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@ApiModel
@Data
public class FundsAddForm {

    @ApiModelProperty(value = "用户唯一标识")
    public Long userId;
    @ApiModelProperty(value = "名称")
    public String title;
    @ApiModelProperty(value = "内容")
    public String introduction;
    @ApiModelProperty(value = "身份：1、个人 2、医疗结构")
    public Integer identity;
    @ApiModelProperty(value = "1:梦计划，2：医创杯")
    public  int type;
}


