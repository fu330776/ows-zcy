package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.DemandsEntity;
import com.goodsogood.ows.model.vo.DemandsVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Mapper
public interface DemandsMapper extends MyMapper<DemandsEntity> {


    @Select({
            "<script>",
            "SELECT  demand_id as demandId, demand_type as demandType , demand_name as demandName,",
            "demand_content as demandContent ,is_contact as isContact ,user_id as userId,addtime,picture,state,rank,demand_type_two,demand_type_three,",
            "(SELECT zu.phone from zcy_users zu where zu.user_id=zd.user_id)phone,",
            "(SELECT zu.user_name from zcy_users zu where zu.user_id=zd.user_id)userName",
            "FROM zcy_demands zd WHERE user_id=#{userId,jdbcType=BIGINT} and demand_type=#{demandType,jdbcType=BIT}",
            "<if test='isContact !=null'> and is_contact=#{isContact,jdbcType=BIT}</if>",
            "<if test='twoType != null'> and demand_type_two=#{twoType,jdbcType=VARCHAR} </if>",
            "<if test='threeType != null'> and demand_type_three=#{threeType,jdbcType=VARCHAR} </if>",
            "<if test='rank != null'> and rank=#{rank,jdbcType=VARCHAR} </if>",
            "<if test='name !=null'>and ( demand_name LIKE concat(CONCAT('%',#{name,jdbcType=VARCHAR},'%'))",
            " or demand_content like concat(CONCAT('%',#{name,jdbcType=VARCHAR},'%'))",
            ")</if>",
            "</script>"
    })
    List<DemandsVo> Get(@Param(value = "userId") Long userId
            , @Param(value = "demandType") Integer demandType
            , @Param(value = "isContact") Integer isContact
            , @Param(value = "name") String name
            ,@Param(value = "twoType") String twoType
            ,@Param(value = "threeType") String threeType
            ,@Param(value = "rank") String rank
                        );

    @Insert({
            "<script>",
            "insert into zcy_demands(",
            "<if test='demandId !=null'> demand_id,</if>",
            "demand_type,demand_name,demand_content,is_contact,user_id,addtime,picture,state,rank,demand_type_two,demand_type_three)",
            "Values (",
            "<if test='demandId !=null'> #{demandId,jdbcType=BIGINT},</if>",
            "#{demandType,jdbcType=BIT},#{demandName,jdbcType=VARCHAR},",
            "#{demandContent,jdbcType=VARCHAR},#{isContact,jdbcType=BIT},",
            "#{userId,jdbcType=BIGINT},#{addtime,jdbcType=TIMESTAMP},",
            "#{picture,jdbcType=VARCHAR},#{state,jdbcType=VARCHAR},",
            "#{rank,jdbcType=VARCHAR},#{demand_type_two,jdbcType=VARCHAR},",
            "#{demand_type_three,jdbcType=VARCHAR}",
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
            "<if test='rank != null'>,rank = #{rank,jdbcType=VARCHAR} </if> ",
            "where demand_id=#{demandId,jdbcType=BIGINT}",
            "</script>"
    })
    int UpdateContent(@Param(value = "demandId") Long demandId, @Param(value = "demandName") String demandName,
                      @Param(value = "demandContent") String demandContent,@Param(value = "rank") String rank);

    @Update({
            "<script>",
            "update zcy_demands set ",
            "demand_name=#{demandName,jdbcType=VARCHAR},",
            "demand_content=#{demandContent,jdbcType=VARCHAR},",
            "is_contact=#{isContact,jdbcType=BIT},",
            "picture=#{picture,jdbcType=VARCHAR}",
            "<if test='rank != null'>,rank=#{rank,jdbcType=VARCHAR} </if> ",
            "where demand_id=#{demandId,jdbcType=BIGINT}",
            "</script>"

    })
    int Update(@Param(value = "demandId") Long demandId, @Param(value = "demandName") String demandName,
               @Param(value = "demandContent") String demandContent, @Param(value = "isContact") Integer isContact
    ,@Param(value = "picture") String picture,@Param(value = "rank") String rank);

    @Select({
            "<script>",
            "select * from(",
            "SELECT  demand_id as demandId, demand_type as demandType , demand_name as demandName,",
            "demand_content as demandContent ,is_contact as isContact ,user_id as userId,addtime,picture,state,rank,demand_type_two,demand_type_three,",
            "(SELECT zu.phone from zcy_users zu where zu.user_id=zd.user_id)phone,",
            "(SELECT zu.user_name from zcy_users zu where zu.user_id=zd.user_id)userName",
            "FROM zcy_demands zd WHERE  demand_type=#{demandType,jdbcType=BIT} and isDel=1 ",
            " <if test='state != null'> and state =#{state,jdbcType=VARCHAR} </if> ",
            "<if test='isContact !=null'> and is_contact=#{isContact,jdbcType=BIT}</if>",
            "<if test='rank !=null'> and rank=#{rank,jdbcType=VARCHAR} </if>",
            "<if test='twoType != null'> and demand_type_two=#{twoType,jdbcType=VARCHAR} </if>",
            "<if test='threeType !=null'> and demand_type_three=#{threeType,jdbcType=VARCHAR} </if>",
            ") as zcy",
            "<if test='name !=null'> where (demandName like concat(concat('%',#{name,jdbcType=VARCHAR}),'%') ",
            " or demandContent like concat(concat('%',#{name,jdbcType=VARCHAR}),'%') ",
            " or phone like concat(concat('%',#{name,jdbcType=VARCHAR}),'%')",
            " or userName like concat(concat('%',#{name,jdbcType=VARCHAR}),'%')",
            ")</if>",
            "</script>"
    })
    List<DemandsVo> GetTypeAll(@Param(value = "demandType") Integer demandType
            , @Param(value = "isContact") Integer isContact
            ,@Param(value = "name") String name
            ,@Param(value = "state") String state
            ,@Param(value = "rank") String rank
            ,@Param(value = "twoType") String twoType
            ,@Param(value = "threeType") String threeType
                               );


    @Update({
            "<script>",
            "update zcy_demands set state=#{state,jdbcType=VARCHAR}  where demand_id=#{id,jdbcType=BIGINT}",
            "</script>"
    })
    int UpdateState(@Param(value = "id")Long id,@Param(value = "state") String state);

//    @Update({
//            "<script>",
//            " update zcy_demands set isDel=2,state='已撤销' where demand_id=#{id,jdbcType=BIGINT} and state='递交成功'",
//            "</script>"
//    })
    @Delete({
            "<script>",
            "delete from zcy_demands where demand_id=#{id,jdbcType=BIGINT} and state='递交成功' and isDel=1",
            "</script>"
    })
    int UpdateRevoke(@Param(value = "id") Long id);

}
