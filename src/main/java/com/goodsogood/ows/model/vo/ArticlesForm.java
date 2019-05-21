package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
@Data
@ApiModel
public class ArticlesForm {

    public Long id;
    @ApiModelProperty(value = "标题")
    @Column(name = "article_title")
    public String title;

    @ApiModelProperty(value = "内容")
    @Column(name = "article_content")
    public String content;

    @ApiModelProperty(value = "文章类型：1、行业动态 2、新技术新产品")
    @Column(name = "article_type")
    public Integer type;
}
