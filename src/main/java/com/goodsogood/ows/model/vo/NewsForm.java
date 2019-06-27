package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@ApiModel
@Data
public class NewsForm {
    @ApiModelProperty(value = "标题")
    @Column(name = "news_title")
    public  String title;
    @ApiModelProperty(value = "内容")
    @Column(name = "news_content")
    public String content;
    @ApiModelProperty(value = "描述")
    @Column(name = "news_describe")
    public String describe;
    @ApiModelProperty(value = "唯一标识")
    @Column(name = "news_id")
    public Long newsId;
}
