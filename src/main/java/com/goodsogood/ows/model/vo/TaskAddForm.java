package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ApiModel
@Data
public class TaskAddForm {
    @ApiModelProperty(value = "任务书名称")
    @Column(name = "task_name")
    public String taskName;

    @ApiModelProperty(value = "任务内容")
    @Column(name = "task_content")
    public String taskContent;

    @ApiModelProperty(value = "任务PDF文件")
    @Column(name = "task_file_url")
    public String taskFileUrl;

    @ApiModelProperty(value = "任务金额")
    @Column(name = "task_money")
    public float taskMoney;
    @ApiModelProperty(value = "执行人")
    @Column(name = "user_id")
    public Long userId;

    @ApiModelProperty(value = "是否完成 1、未完成  2、已完成")
    @Min(value = 1, message = "{Min.zcy_tasks.is_fulfill}")
    public Integer is_fulfill;

    @ApiModelProperty(value = "完成进度百分比（最大100%）")
    @Max(value = 100, message = "{Max.zcy_tasks.schedule}")
    public float schedule;
    @ApiModelProperty(value = "是否已领取酬劳 1、未领取 2、已领取")
    public Integer is_pay;

    @ApiModelProperty(value = "总完成天数")
    @Column(name = "task_completion_days")
    public Integer taskCompletionDays;

    @ApiModelProperty(value = "已完成天数")
    @Column(name = "task_completed_days")
    public Integer taskCompletedDays;

    @ApiModelProperty(value = "1、个人任务书，2、企业委托书")
    @Column(name = "task_type")
    public Integer taskType;
}
