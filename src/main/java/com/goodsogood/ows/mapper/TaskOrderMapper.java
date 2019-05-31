package com.goodsogood.ows.mapper;


import com.goodsogood.ows.model.db.TaskOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TaskOrderMapper extends MyMapper<TaskOrderEntity> {

    @Select({
            "<script>",
            "SELECT ",
            "zto.addtime,zt.task_id as taskId, zt.task_name as taskName,",
            "zt.task_content as taskContent,zt.task_file_url as taskFileUrl,",
            "zt.task_money as taskMoney, zt.user_id as userId,zt.is_fulfill,",
            "zt.`schedule`,zt.is_pay,zto.task_order_id as taskOrderId",
            "from  zcy_tasks zt ",
            "LEFT JOIN zcy_tasks_orders zto  on zto.task_id=zt.task_id",
            "WHERE zto.user_id=#{userid,jdbcType=BIGINT} and is_fulfill=2  and is_pay=2",
            "</script>"
    })
    List<TaskOrderEntity> Get(@Param(value = "userId") Long userId);
}
