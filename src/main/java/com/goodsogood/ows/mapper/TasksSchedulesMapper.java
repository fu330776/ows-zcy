package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.TasksSchedulesEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TasksSchedulesMapper extends MyMapper<TasksSchedulesEntity> {

    @Insert({
            "<script>",
            "INSERT INTO zcy_tasks_schedules",
            "(",
            "<if test='taskScheduleId !=null'>task_schedule_id,</if>",
            "cycle,order_index,task_id,task_schedule_content",
            ") VALUES(",
            "<if test='taskScheduleId !=null'>#{taskScheduleId,jdbcType=BIGINT},</if>",
            "#{cycle,jdbcType=BIT},#{orderIndex,jdbcType=BIT},#{taskId,jdbcType=BIGINT}",
            "#{content,jdbcType=VARCHAR})",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "taskScheduleId", keyColumn = "task_schedule_id")
    int Insert(TasksSchedulesEntity entity);


    @Select({
            "<script>",
            "SELECT",
            "task_schedule_id as taskScheduleId,task_id as taskId,",
            "cycle,order_index as orderIndex, task_schedule_content as content",
            "from zcy_tasks_schedules WHERE task_id=#{taskId,jdbcType=BIGINT}",
            "</script>"
    })
    List<TasksSchedulesEntity> Get(@Param(value = "taskId") Long taskId);

    @Delete({
            "<script>",
            "DELETE FROM zcy_tasks_schedules WHERE task_id=#{taskId,jdbcType=BIGINT}",
            "</script>"
    })
    int Delete(@Param(value = "taskId") Long taskId );

}
