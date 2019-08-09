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
            "user_id,title,introduction,identity,addtime,is_success,types,applyMoney",
            ")VALUES(",
            "<if test='fundId !=null'>#{fundId,jdbcType=BIGINT}, </if>",
            "#{userId,jdbcType=BIGINT},#{title,jdbcType=VARCHAR},#{introduction,jdbcType=VARCHAR},#{identity,jdbcType=BIT},#{addtime,jdbcType=TIMESTAMP}",
            ",#{success,jdbcType=BIT},#{types,jdbcType=BIT},#{applyMoney,jdbcType=DECIMAL})",
            "</script>"

    })
    @Options(useGeneratedKeys = true, keyProperty = "fundId", keyColumn = "fund_id")
    int Insert(FundsEntity fundsEntity);


    @Select({
            "<script>",
            "SELECT ",
            "user_id as userId ,title,introduction,identity,addtime,fund_id as fundId,is_success as success,applyMoney",
            "FROM zcy_funds",
            "WHERE user_id=#{userId,jdbcType=BIGINT} and types=#{type,jdbcType=BIT}",
            "</script>"
    })
    List<FundsEntity> Get(@Param(value = "userId") Long userId,@Param(value = "type") int type);


    @Select({
            "<script>",
            "select * from(",
            "SELECT ",
            "user_id as userId,title,introduction,identity,addtime,fund_id as fundId,applyMoney,",
            "(SELECT zu.user_name FROM zcy_users zu where zu.user_id=zf.user_id) userName,is_success as success",
            ",(SELECT zu.phone FROM zcy_users zu where zu.user_id=zf.user_id) userPhone",
            "FROM zcy_funds zf ",
            "where zf.identity=#{type,jdbcType=BIT} and zf.types=#{types,jdbcType=BIT}",
            " <if test ='success !=null '> and is_success =#{success,jdbcType=BIT} </if>) as zcy ",
            "<if test='name !=null'> where (title like concat(concat('%',#{name,jdbcType=VARCHAR}),'%') ",
            "or userPhone like concat(concat('%',#{name,jdbcType=VARCHAR}),'%')",
            "or userName like concat(concat('%',#{name,jdbcType=VARCHAR}),'%')",
            ")</if>",
            "</script>"
    })
    List<FundsVo> GetAll(@Param(value = "type") Integer type,@Param(value = "name") String name,@Param(value = "success") Integer success
            ,@Param(value = "types") int types);


    @Update({
            "<script>",
            "UPDATE zcy_funds SET ",
            "<if test='title !=null'>title=#{title,jdbcType=VARCHAR},</if> ",
            "<if test='introduction !=null'>introduction=#{introduction,jdbcType=VARCHAR},</if> ",
            "<if test='success !=null'>is_success=#{success,jdbcType=BIT}</if>",
            "WHERE fund_id=#{funId,jdbcType=BIGINT}",
            "</script>"

    })
    int Update(@Param(value = "funId") Long funId,
               @Param(value = "title") String title,
               @Param(value = "introduction") String introduction,
               @Param(value = "success") int success );

    /**
     *  删除
     * @param funId
     * @return
     */
    @Delete({
            "<script>",
            "DELETE FROM zcy_funds where fund_id=#{funId,jdbcType=BIGINT} and is_success=1",
            "</script>"
    })
    int CustomDelete(@Param(value = "fundId") Long funId);
}
