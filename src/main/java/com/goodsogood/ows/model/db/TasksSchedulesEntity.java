package com.goodsogood.ows.model.db;

import com.goodsogood.ows.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "zcy_tasks_schedules")
public class TasksSchedulesEntity extends BaseEntity {

    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_tasks_schedules.task_schedule_id}")
    @NotNull(message = "{NotBlank.zcy_tasks_schedules.task_schedule_id}")
    @Column(name = "task_schedule_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long taskScheduleId;

    @ApiModelProperty(value = "周期天数")
    public Integer cycle;

    @ApiModelProperty(value = "排序")
    @Column(name = "order_index")
    public Integer orderIndex;

    @ApiModelProperty(value = "任务ID")
    @Column(name = "task_id")
    public Long taskId;

    @ApiModelProperty(value = "周期内容")
    @Column(name = "task_schedule_content")
    public String content;
}
