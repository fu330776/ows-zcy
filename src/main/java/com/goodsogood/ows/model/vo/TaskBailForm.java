package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
@Data
@ApiModel
public class TaskBailForm {

    public  Integer taskCompletionDays;

    public  Integer taskCompletedDays;

    public  Long taskId;
}
