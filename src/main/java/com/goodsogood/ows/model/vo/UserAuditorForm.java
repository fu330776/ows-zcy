package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class UserAuditorForm {
    @ApiModelProperty(value = "医护唯一标识")
    public  Long userId;
    @ApiModelProperty(value = "1：取消 2：医护审核人员")
    public  Integer isNum;
}
