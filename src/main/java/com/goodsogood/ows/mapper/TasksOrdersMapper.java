package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.TasksOrdersEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TasksOrdersMapper extends MyMapper<TasksOrdersEntity> {


    @Insert({
            "<script>",
            "INSERT INTO (",
            "<if test='taskOrderId !=null'>task_order_id,</if>",
            "task_id,user_id,task_money,addtime",
            ")VALUES(",
            "<if test='taskOrderId !=null'>#{taskOrderId,jdbcType=BIGINT},</if>",
            "#{taskId,jdbcType=BIGINT},#{userId,jdbcType=BIGINT},",
            "#{taskMoney,jdbcType=FLOAT},#{addtime,JdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "taskOrderId", keyColumn = "task_order_id")
    int Insert(TasksOrdersEntity tasksOrdersEntity);


}
