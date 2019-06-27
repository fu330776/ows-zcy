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
@Table(name = "zcy_news")
public class NewsEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_news.news_id}")
    @NotNull(message = "{NotBlank.zcy_news.news_id}")
    @Column(name = "news_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long newsId;
    @ApiModelProperty(value = "标题")
    @Column(name = "news_title")
    public  String title;
    @ApiModelProperty(value = "内容")
    @Column(name = "news_content")
    public String content;
    @ApiModelProperty(value = "描述")
    @Column(name = "news_describe")
    public String describes;
    @ApiModelProperty(value = "添加时间")
    @Column(name = "news_time")
    public Date time;
}
