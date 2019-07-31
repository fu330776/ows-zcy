package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.InstructionsEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface InstructionsMapper  extends  MyMapper<InstructionsEntity>{
    @Insert({
            "<script>",
            "insert into zcy_Instructions(<if test='id !=null'> id,</if> Instructions) values(",
            "<if test='id !=null'> #{id,jdbcType=BIT},</if>,#{Instructions,jdbcType=LONGVARCHAR} )",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int Insert(InstructionsEntity entity);

    @Update({
            "<script>",
            "update zcy_Instructions set  Instructions = #{Instructions,jdbcType=LONGVARCHAR} where id=#{id,jdbcType=BIT}",
            "</script>"
    })
    int Update(InstructionsEntity entity);

    @Select({
            "<script>",
            "select * from zcy_Instructions where id=1",
            "</script>"
    })
    InstructionsEntity Get();
}
