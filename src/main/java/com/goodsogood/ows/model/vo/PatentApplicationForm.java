package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 专利申请
 */
@Data
@ApiModel
public class PatentApplicationForm {
    public Long userid;
    public String title;
    public String content;
    @ApiModelProperty(value = "图片")
    public  String picture;

}
