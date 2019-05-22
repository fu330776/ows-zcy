package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.PatentsEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PatentsMapper extends MyMapper<PatentsEntity> {


    @Insert({
            "<script>",
            "INSERT INTO zcy_patents(",
            "<if test='patentId !=null' >patent_id,</if> ",
            "patent_title,patent_content,user_id,patent_type,patent_money,patent_stamp,is_need_pay,is_pay,addtime,pay_time",
            ")",
            " VALUES( ",
            "<if test='patentId !=null' >#{patent_id,jdbcType=BIGINT},</if>",
            "#{patentTitle,jdbcType=VARCHAR},#{patentContent,jdbcType=VARCHAR},",
            "#{userId,jdbcType=BIGINT},#{patentType,jdbcType=BIT},#{patentMoney,jdbcType=DECIMAL},",
            "#{patentStamp,jdbcType=VARCHAR},#{isNeedPay,jdbcType=BIT},#{isPay,jdbcType=BIT},",
            "#{addtime,jdbcType=TIMESTAMP},#{pay_time,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "patentId", keyColumn = "patent_id")
    int Insert(PatentsEntity patentsEntity);

    @Select({
            "<script>",
            "SELECT patent_id,patent_title,patent_content,user_id,patent_type,patent_money,patent_stamp,is_need_pay,is_pay,addtime,pay_time ",
            "FROM zcy_patents where user_id=#{id,jdbcType=BIGINT} and patent_type=#{type,jdbcType=BIT}",
            "</script>"
    })
    List<PatentsEntity> Get(@Param(value = "id") Long id, @Param(value = "type") Integer type);


    @Select({

            "<script>",
            "SELECT patent_id,patent_title,patent_content,user_id,patent_type,patent_money,patent_stamp,is_need_pay,is_pay,addtime,pay_time ",
            "FROM zcy_patents where patent_type=#{type,jdbcType=BIT}",
            "</script>"
    })
    List<PatentsEntity> GetByType(@Param(value = "type") Integer type);

    @Select({
            "<script>",
            "SELECT patent_id,patent_title,patent_content,user_id,patent_type,patent_money,patent_stamp,is_need_pay,is_pay,addtime,pay_time ",
            "FROM zcy_patents where patent_id=#{id,jdbcType=BIGINT}",
            "</script>"
    })
    PatentsEntity GetById(@Param(value = "id") Long id);

}
