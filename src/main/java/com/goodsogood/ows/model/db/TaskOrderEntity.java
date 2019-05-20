package com.goodsogood.ows.model.db;

import com.goodsogood.ows.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "zcy_TaskOrder")
public class TaskOrderEntity extends BaseEntity {

    @Column(name = "task_id")
    public Long taskId;

    @ApiModelProperty(value = "任务书名称")
    @Column(name = "task_name")
    public String taskName;

    @ApiModelProperty(value = "任务内容")
    @Column(name = "task_content")
    public String taskContent;

    @ApiModelProperty(value = "任务PDF文件")
    @Column(name = "task_file_url")
    public String taskFileUrl;

    public Data addtime;

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
    @Max(value = 100, message = "{Min.zcy_tasks.schedule}")
    public float schedule;
    @ApiModelProperty(value = "是否已领取酬劳 1、未领取 2、已领取")
    public Integer is_pay;

    @Column(name = "task_order_id")
    public Long taskOrderId;

}
