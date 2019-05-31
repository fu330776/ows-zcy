package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@ApiModel
@Data
public class FundsPutForm {
    @ApiModelProperty(value = "唯一标识")
    public Long fundId;
    @ApiModelProperty(value = "名称")
    public String title;
    @ApiModelProperty(value = "内容")
    public String introduction;
    @ApiModelProperty(value = "1:未审核 2:通过")
    public  int success;
}
