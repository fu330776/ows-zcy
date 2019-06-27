package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
@ApiModel
public class EntrustForm {

    public String taskName;
    public  String taskContent;
    public  Integer taskType;
    public Long userId;
    @ApiModelProperty(value = "任务PDF文件")
    @Column(name = "task_file_url")
    public String taskFileUrl;
}
