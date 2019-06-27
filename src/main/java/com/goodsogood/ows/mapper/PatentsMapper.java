package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.PatentsEntity;
import com.goodsogood.ows.model.vo.PatentsVo;
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
            " ,picture,state  )",
            " VALUES( ",
            "<if test='patentId !=null' >#{patent_id,jdbcType=BIGINT},</if>",
            "#{patentTitle,jdbcType=VARCHAR},#{patentContent,jdbcType=VARCHAR},",
            "#{userId,jdbcType=BIGINT},#{patentType,jdbcType=BIT},#{patentMoney,jdbcType=DECIMAL},",
            "#{patentStamp,jdbcType=VARCHAR},#{isNeedPay,jdbcType=BIT},#{isPay,jdbcType=BIT},",
            "#{addtime,jdbcType=TIMESTAMP},#{pay_time,jdbcType=TIMESTAMP}",
            ",#{picture,jdbcType=VARCHAR},#{state,jdbcType=VARCHAR}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "patentId", keyColumn = "patent_id")
    int Insert(PatentsEntity patentsEntity);

    @Select({
            "<script>",
            "SELECT patent_id as patentId,patent_title as patentTitle,patent_content as patentContent,user_id as userId,patent_type as patentType, ",
            "patent_money as patentMoney,patent_stamp as patentStamp,is_need_pay as isNeedPay,is_pay as isPay,addtime,pay_time,picture,state,",
            "(SELECT phone from zcy_users zu where zu.user_id=z.user_id)phone",
            ",(SELECT zu.user_name from zcy_users zu where zu.user_id=z.user_id)userName",
            "FROM zcy_patents z where user_id=#{id,jdbcType=BIGINT} and patent_type=#{type,jdbcType=BIT}",
            "<if test='title !=null'> and patent_title LIKE CONCAT(CONCAT('%',#{title,jdbcType=VARCHAR},'%')) </if>",
            "</script>"
    })
    List<PatentsVo> Get(@Param(value = "id") Long id, @Param(value = "type") Integer type, @Param(value = "title") String title);


    @Select({

            "<script>",
            "SELECT patent_id as patentId,patent_title as patentTitle,patent_content as patentContent,user_id as userId,patent_type as patentType,",
            "patent_money as patentMoney,patent_stamp as patentStamp,is_need_pay as isNeedPay,is_pay as isPay,addtime,pay_time ,picture,state",
            ",(SELECT phone from zcy_users zu where zu.user_id=z.user_id)phone",
            ",(SELECT zu.user_name from zcy_users zu where zu.user_id=z.user_id)userName",
            "FROM zcy_patents z where patent_type=#{type,jdbcType=BIT}",
            "<if test='title !=null'> and patent_title LIKE CONCAT(CONCAT('%',#{title,jdbcType=VARCHAR},'%')) </if>",
            "</script>"
    })
    List<PatentsVo> GetByType(@Param(value = "type") Integer type, @Param(value = "title") String title);

    @Select({
            "<script>",
            "SELECT patent_id as patentId,patent_title as patentTitle,patent_content as patentContent,user_id as userId,patent_type as patentType,",
            "patent_money as patentMoney,patent_stamp as patentStamp,is_need_pay as isNeedPay,is_pay as isPay,addtime,pay_time,picture,state ",
            ",(SELECT phone from zcy_users zu where zu.user_id=z.user_id)phone",
            ",(SELECT zu.user_name from zcy_users zu where zu.user_id=z.user_id)userName",
            "FROM zcy_patents z where patent_type=#{type,jdbcType=BIT} and is_need_pay=1 and is_pay=#{isPay,jdbcType=BIT}",
            "<if test='title !=null'> and patent_title LIKE CONCAT(CONCAT('%',#{title,jdbcType=VARCHAR},'%')) </if>",
            "</script>"
    })
    List<PatentsVo> GetIdea(@Param(value = "type") Integer type, @Param(value = "isPay") Integer isPay, @Param(value = "title") String title);


    @Select({
            "<script>",
            "SELECT patent_id as patentId,patent_title as patentTitle,patent_content as patentContent,user_id as userId,patent_type as patentType,",
            "patent_money as patentMoney,patent_stamp as patentStamp,is_need_pay as isNeedPay,is_pay as isPay,addtime,pay_time,picture,state",
            ",(SELECT phone from zcy_users zu where zu.user_id=z.user_id)phone",
            ",(SELECT zu.user_name from zcy_users zu where zu.user_id=z.user_id)userName",
            "FROM zcy_patents z where patent_id=#{id,jdbcType=BIGINT}",
            "</script>"
    })
    PatentsVo GetById(@Param(value = "id") Long id);

    @Update({
            "<script>",
            "UPDATE zcy_patents SET is_pay=2 where patent_id=#{pid,jdbcType=BIGINT} ",
            "</script>"
    })
    int UpdateIdea(@Param(value = "pid") Long pid);


    @Update({
            "<script>",
            "Update zcy_patents set state=#{state,jdbcType=VARCHAR} where patent_id=#{id,jdbcType=BIGINT}",
            "</script>"
    })
    int UpdateState(@Param(value = "id") Long id,@Param(value = "state") String state);
}
