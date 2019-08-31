package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class TipsForm {
    @ApiModelProperty(value = "用户唯一标识")
    public Long userId;
    @ApiModelProperty(value = "0：提示 1：取消提示")
    public int isNum;
}
