package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.DemandsEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Mapper
public interface DemandsMapper extends MyMapper<DemandsEntity> {


    @Select({
            "<script>",
            "SELECT  demand_id as demandId, demand_type as demandType , demand_name as demandName,",
            "demand_content as demandContent ,is_contact as isContact ,user_id as userId,addtime",
            "FROM zcy_demands WHERE user_id=#{userId,jdbcType=BIGINT} and demand_type=#{demandType,jdbcType=BIT}",
            "<if test='isContact !=0'> and is_contact=#{isContact,jdbcType=BIT}</if>",
            "</script>"
    })
    List<DemandsEntity> Get(@Param(value = "userId") Long userId, @Param(value = "demandType") Integer demandType
            , @Param(value = "isContact") Integer isContact);

    @Insert({
            "<script>",
            "insert into zcy_demands(",
            "<if test='demandId !=null'> demand_id,</if>",
            "demand_type,demand_name,demand_content,is_contact,user_id,addtime)",
            "Values (",
            "<if test='demandId !=null'> #{demandId,jdbcType=BIGINT},</if>",
            "#{demandType,jdbcType=BIT},#{demandName,jdbcType=VARCHAR},",
            "#{demandContent,jdbcType=VARCHAR},#{isContact,jdbcType=BIT},",
            "#{userId,jdbcType=BIGINT},#{addtime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "demandId", keyColumn = "demand_id")
    int Insert(DemandsEntity demandsEntity);

    @Update({
            "<script>",
            "update zcy_demands set is_contact=#{isContact,jdbcType=BIT} where demand_id=#{demandId,jdbcType=BIGINT}",
            "</script>"
    })
    int UpdateContact(@Param(value = "demandId") Long demandId, @Param(value = "isContact") Integer isContact);

    @Update({
            "<script>",
            "update zcy_demands set ",
            "<if test='demandName !=null'> demand_name=#{demandName,jdbcType=VARCHAR},</if>",
            "<if test='demandContent !=null'>demand_content=#{demandContent,jdbcType=VARCHAR}</if>",
            "where demand_id=#{demandId,jdbcType=BIGINT}",
            "</script>"
    })
    int UpdateContent(@Param(value = "demandId") Long demandId, @Param(value = "demandName") String demandName,
                      @Param(value = "demandContent") String demandContent);

    @Select({
            "<script>",
            "SELECT  demand_id as demandId, demand_type as demandType , demand_name as demandName,",
            "demand_content as demandContent ,is_contact as isContact ,user_id as userId,addtime",
            "FROM zcy_demands WHERE  demand_type=#{demandType,jdbcType=BIT}",
            "<if test='isContact !=null'> and is_contact=#{isContact,jdbcType=BIT}</if>",
            "</script>"
    })
    List<DemandsEntity> GetTypeAll(@Param(value = "demandType") Integer demandType,@Param(value = "isContact") Integer isContact);
}
