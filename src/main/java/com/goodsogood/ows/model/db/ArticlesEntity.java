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
@Table(name = "zcy_articles")
/**
 * 文章表
 * */
public class ArticlesEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_articles.article_id}")
    @NotNull(message = "{NotBlank.zcy_articles.article_id}")
    @Column(name = "article_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long articleId;


    @ApiModelProperty(value = "标题")
    @Column(name = "article_title")
    public String title;

    @ApiModelProperty(value = "内容")
    @Column(name = "article_content")
    public String content;

    @ApiModelProperty(value = "文章类型：1、行业动态 2、新技术新产品")
    @Column(name = "article_type")
    public Integer type;

    public Date addtime;
}
