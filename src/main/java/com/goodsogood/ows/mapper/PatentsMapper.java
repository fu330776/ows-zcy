package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.PatentsEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PatentsMapper extends  MyMapper<PatentsEntity> {


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

}
