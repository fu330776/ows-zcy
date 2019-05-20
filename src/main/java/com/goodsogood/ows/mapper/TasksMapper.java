package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.TasksEntity;
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
            "task_name,task_content,task_file_url,addtime,task_money,user_id,is_fulfill,schedule,is_pay",
            ")VALUES(",
            "<if test='taskId !=null'>#{taskId,jdbcType=BIGINT},</if>",
            "#{taskName,jdbcType=VARCHAR},#{taskContent,jdbcType=VARCHAR},",
            "#{taskFileUrl,jdbcType=VARCHAR},#{addtime,JdbcType=TIMESTAMP},",
            "#{taskMoney,jdbcType=FLOAT},#{userId,jdbcType=BIGINT},",
            "#{is_fulfill,jdbcType=BIT},#{schedule,jdbcType=FLOAT},",
            "#{is_pay,jdbcType=BIT}",
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
            "is_fulfill,`schedule`,is_pay,addtime,user_id as userId",
            " from zcy_tasks WHERE is_pay=#{is_pay,jdbcType=BIT}",
            "</script>"
    })
    List<TasksEntity> GetByAll(@Param(value = "is_pay") Integer is_pay);

    @Select({
            "<script>",
            "SELECT ",
            "task_id as taskId,task_name as taskName,task_content as taskContent,",
            "task_file_url as taskFileUrl,task_money as taskMoney,",
            "is_fulfill,`schedule`,is_pay,addtime,user_id as userId",
            " from zcy_tasks WHERE is_pay=#{is_pay,jdbcType=BIT} and user_id=#{userId,jdbcType=BIGINT} ",
            "</script>"
    })
    List<TasksEntity> GetByUserIdAll(@Param(value = "is_pay") Integer is_pay, @Param(value = "userId") Long userId);

    @Update({
            "<script>",
            "UPDATE zcy_tasks SET `schedule` =#{schedule,jdbcType=FLOAT} WHERE task_id=#{taskId,jdbcType=BIGINT}",
            "</script>"
    })
    int ModifySchedule(@Param(value = "taskId") Long taskId, @Param(value = "schedule") float schedule);

    @Update({
            "<script>",
            "UPDATE zcy_tasks SET is_fulfill =2 ,is_pay=2 WHERE task_id=#{taskId,jdbcType=BIGINT}",
            "</script>"
    })
    int ModifyFulfill(@Param(value = "taskId") Long taskId);
}
