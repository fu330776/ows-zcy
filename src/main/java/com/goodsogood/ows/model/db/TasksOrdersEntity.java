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
@Table(name = "zcy_tasks_orders")
public class TasksOrdersEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_tasks_orders.task_order_id}")
    @NotNull(message = "{NotBlank.zcy_tasks_orders.task_order_id}")
    @Column(name = "task_order_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long taskOrderId;

    @ApiModelProperty(value = "任务ID")
    @Column(name = "task_id")
    public Long taskId;

    @ApiModelProperty(value = "领取人")
    @Column(name = "user_id")
    public Long userId;

    @ApiModelProperty(value = "金额")
    @Column(name = "task_money")
    public float taskMoney;

    public Date addtime;

    @ApiModelProperty(value = "是否领取 1：未领取 2：已领取")
    @Column(name = "is_receive")
    public  Integer isReceive;

}
