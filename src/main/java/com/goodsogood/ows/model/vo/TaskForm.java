package com.goodsogood.ows.model.vo;

import com.goodsogood.ows.model.db.TasksEntity;
import com.goodsogood.ows.model.db.TasksSchedulesEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
@Data
@ApiModel
public class TaskForm {
    public TasksEntity entity;
    public List<TasksSchedulesEntity> entities;
}
