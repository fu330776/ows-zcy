package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.FundsEntity;
import com.goodsogood.ows.model.vo.FundsVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FundsMapper extends MyMapper<FundsEntity> {

    @Insert({
            "<script>",
            "INSERT INTO zcy_funds(",
            "<if test='fundId !=null'>fund_id, </if>",
            "user_id,title,introduction,identity,addtime",
            ")VALUES(",
            "<if test='fundId !=null'>#{fundId,jdbcType=BIGINT}, </if>",
            "#{userId,jdbcType=BIGINT},#{title,jdbcType=VARCHAR},#{introduction,jdbcType=VARCHAR},#{identity,jdbcType=BIT},#{addtime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"

    })
    @Options(useGeneratedKeys = true, keyProperty = "fundId", keyColumn = "fund_id")
    int Insert(FundsEntity fundsEntity);


    @Select({
            "<script>",
            "SELECT ",
            "user_id,title,introduction,identity,addtime,fund_id ",
            "FROM zcy_funds",
            "WHERE user_id=#{userId,jdbcType=BIGINT}",
            "</script>"
    })
    List<FundsEntity> Get(@Param(value = "userId") Long userId);


    @Select({
            "<script>",
            "SELECT ",
            "user_id as userId,title,introduction,identity,addtime,fund_id as fundId,",
            "(SELECT zu.user_name FROM zcy_users zu where zu.user_id=zf.user_id)userName",
            "FROM zcy_funds zf ",
            "<if test='type !=null'> where zf.identity=#{type,jdbcType=BIT} </if>",
            "</script>"
    })
    List<FundsVo> GetAll(@Param(value = "type") Integer type);


    @Update({

            "<script>",
            "UPDATE zcy_funds SET ",
            "title=#{title,jdbcType=VARCHAR},introduction=#{introduction,jdbcType=VARCHAR}",
            "WHERE fund_id=#{fundId,jdbcType=BIGINT}",
            "</script>"

    })
    int Update(@Param(value = "funId") Long funid, @Param(value = "title") String title, @Param(value = "introduction") String introduction);
}
