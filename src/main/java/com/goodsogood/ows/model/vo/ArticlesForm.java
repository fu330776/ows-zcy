package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel
public class ArticlesForm {
    @ApiModelProperty(value = "标题")
    public String title;

    @ApiModelProperty(value = "内容")
    public String content;
}
