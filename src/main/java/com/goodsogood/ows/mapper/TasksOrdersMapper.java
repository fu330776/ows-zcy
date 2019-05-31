package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.TasksOrdersEntity;
import com.goodsogood.ows.model.vo.TaskOrderVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TasksOrdersMapper extends MyMapper<TasksOrdersEntity> {


    @Insert({
            "<script>",
            "INSERT INTO zcy_tasks_orders(",
            "<if test='taskOrderId !=null'>task_order_id,</if>",
            "task_id,user_id,task_money,addtime,is_receive",
            ")VALUES(",
            "<if test='taskOrderId !=null'>#{taskOrderId,jdbcType=BIGINT},</if>",
            "#{taskId,jdbcType=BIGINT},#{userId,jdbcType=BIGINT},",
            "#{taskMoney,jdbcType=DECIMAL},#{addtime,jdbcType=TIMESTAMP},#{isReceive,jdbcType=BIT}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "taskOrderId", keyColumn = "task_order_id")
    int Insert(TasksOrdersEntity tasksOrdersEntity);


    @Update({
            "<script>",
            "UPDATE zcy_tasks_orders SET is_receive=2 WHERE task_id=#{id,jdbcType=BIGINT}",
            "</script>"
    })
    int Update(@Param(value = "id") Long id);


    @Select({
            "<script>",
            "SELECT",
            "zto.addtime,zt.task_id as taskId, zt.task_name as taskName,",
            "zt.task_content as taskContent,zt.task_file_url as taskFileUrl,",
            "zt.task_money as taskMoney, zt.user_id as userId,zt.is_fulfill,",
            "zt.`schedule`,zt.is_pay,zto.task_order_id as taskOrderId,zto.is_receive as isReceive",
            "from  zcy_tasks_orders zto",
            "LEFT JOIN  zcy_tasks zt  on zto.task_id=zt.task_id",
            "WHERE  zto.user_id=#{userId,jdbcType=BIGINT} and  zt.is_fulfill=2  and zt.is_pay=2 and zt.task_type=#{type,jdbcType=BIT}" ,
            "</script>"

    })
    List<TaskOrderVo> Get(@Param(value = "userId") Long userId,@Param(value = "type") Integer type);
}
