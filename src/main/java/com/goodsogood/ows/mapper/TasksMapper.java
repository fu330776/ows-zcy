package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.TasksEntity;
import com.goodsogood.ows.model.vo.TaskListForm;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Mapper
public interface TasksMapper extends MyMapper<TasksEntity> {

    @Insert({
            "<script>",
            "INSERT INTO zcy_tasks(",
            "<if test='taskId !=null'>task_id, </if>",
            "task_name,task_content,task_file_url,addtime,task_money,user_id,is_fulfill,schedule,is_pay, task_completion_days,task_completed_days,task_type",
            ")VALUES(",
            "<if test='taskId !=null'>#{taskId,jdbcType=BIGINT},</if>",
            "#{taskName,jdbcType=VARCHAR},#{taskContent,jdbcType=VARCHAR},",
            "#{taskFileUrl,jdbcType=VARCHAR},#{addtime,JdbcType=TIMESTAMP},",
            "#{taskMoney,jdbcType=FLOAT},#{userId,jdbcType=BIGINT},",
            "#{is_fulfill,jdbcType=BIT},#{schedule,jdbcType=FLOAT},",
            "#{is_pay,jdbcType=BIT},#{taskCompletionDays,jdbcType=BIT},#{taskCompletedDays,jdbcType=BIT},",
            "#{taskType,jdbcType=BIT}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "taskId", keyColumn = "task_id")
    int Insert(TasksEntity tasksEntity);

    @Select({
            "<script>",
            "SELECT ",
            "task_id as taskId,task_name as taskName,task_content as taskContent,",
            "task_file_url as taskFileUrl,task_money as taskMoney,",
            "is_fulfill,`schedule`,is_pay,addtime,user_id as userId,task_completion_days as taskCompletionDays,task_completed_days as taskCompletedDays,task_type as taskType",
            " from zcy_tasks WHERE",
            " is_pay=#{is_pay,jdbcType=BIT} and ",
            "task_type=#{type,jdbcType=BIT}",
            "</script>"
    })
    //<if test='is_pay !=null'></if>
    List<TasksEntity> GetByAll(@Param(value = "is_pay") Integer is_pay, @Param(value = "type") Integer type);

    @Select({
            "<script>",
            "SELECT ",
            "task_id as taskId,task_name as taskName,task_content as taskContent,",
            "task_file_url as taskFileUrl,task_money as taskMoney,",
            "is_fulfill,`schedule`,is_pay,addtime,user_id as userId,task_completion_days as taskCompletionDays,task_completed_days as taskCompletedDays,task_type as taskType",
            " from zcy_tasks WHERE is_pay=#{is_pay,jdbcType=BIT} and user_id=#{userId,jdbcType=BIGINT} ",
            "</script>"
    })
    List<TasksEntity> GetByUserIdAll(@Param(value = "is_pay") Integer is_pay, @Param(value = "userId") Long userId);

    @Select({
            "<script>",
            "SELECT ",
            "task_id as taskId,task_name as taskName,task_content as taskContent,",
            "task_file_url as taskFileUrl,task_money as taskMoney,",
            "is_fulfill,`schedule`,is_pay,addtime,user_id as userId,task_completion_days as taskCompletionDays,task_completed_days as taskCompletedDays,task_type as taskType",
            "from zcy_tasks WHERE  task_id=#{taskId,jdbcType=BIGINT}",
            "</script>"
    })
    TasksEntity GetByTaskId(@Param(value = "taskId") Long taskId);

    @Select({
            "<script>",
            "SELECT ",
            "task_id as taskId,task_name as taskName,task_content as taskContent,",
            "task_file_url as taskFileUrl,task_money as taskMoney,",
            "is_fulfill,`schedule`,is_pay,addtime,user_id as userId,task_completion_days as taskCompletionDays,task_completed_days as taskCompletedDays,task_type as taskType,",
            "(SELECT zu.user_name FROM zcy_users zu where zu.user_id = zt.user_id) as userName",
            " from zcy_tasks zt WHERE",
            "task_type=#{type,jdbcType=BIT}",
            "</script>"
    })
    List<TaskListForm> GetAll(@Param(value = "type") Integer type);


    @Update({
            "<script>",
            "UPDATE  zcy_tasks SET",
            "task_file_url=#{taskFileUrl,jdbcType=VARCHAR},task_money=#{taskMoney,jdbcType=DECIMAL}, task_completion_days=#{taskCompletionDays,jdbcType=BIT},task_completed_days=#{taskCompletedDays,jdbcType=BIT}",
            "where task_id=#{taskId,jdbcType=BIT} and task_type=2",
            "</script>"
    })
    int Update(TasksEntity tasksEntity);


    @Update({
            "<script>",
            "UPDATE  zcy_tasks SET",
            "task_completion_days=#{taskCompletionDays,jdbcType=BIT},",
            "task_completed_days= #{taskCompletedDays,jdbcType=BIT},",
            "WHERE task_id=#{taskId,jdbcType=BIGINT}",
            "</script>"
    })

    int PutTaskUpdate(@Param(value = "taskId") Long taskId, @Param(value = "taskCompletionDays") Integer taskCompletionDays, @Param(value = "taskCompletedDays") Integer taskCompletedDays);



}
