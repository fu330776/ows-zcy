package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@ApiModel
@Data
public class NewsVo {
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
