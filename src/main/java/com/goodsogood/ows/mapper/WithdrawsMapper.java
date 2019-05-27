package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.WithdrawsEntity;
import com.goodsogood.ows.model.vo.WithdrawsVo;
import org.apache.ibatis.annotations.*;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Mapper
public interface WithdrawsMapper extends MyMapper<WithdrawsEntity> {

    @Insert({
            "<script>",
            "INSERT INTO zcy_withdraws(",
            "<if test='withdrawId !=null'>withdraw_id,</if>",
            "withdraw_number,withdraw_money,user_id,is_withdraw,addtime,paytime",
            ")VALUES(",
            "<if test='withdrawId !=null'>#{withdrawId,jdbcType=BIGINT}, </if>",
            "#{withdrawNumber,jdbcType=VARCHAR},#{withdrawMoney,jdbcType=DECIMAL},",
            "#{userId,jdbcType=BIGINT},#{isWithdraw,jdbcType=BIT},",
            "#{addtime,jdbcType=TIMESTAMP},#{paytime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "withdrawId", keyColumn = "withdraw_id")
    int Insert(WithdrawsEntity withdrawsEntity);


    @Update({
            "<script>",
            "UPDATE zcy_withdraws SET is_withdraw=2 where withdraw_id=#{withdrawId,jdbcType=BIGINT}",
            "</script>"
    })
    int Update(@Param(value = "withdrawId") Long withdrawId);


    @Select({
            "<script>",
            "SELECT SUM(withdraw_money) as money from zcy_withdraws where user_id=#{userId,jdbcType=BIGINT} and is_withdraw=1",
            "</script>"

    })
    BigDecimal GetNotSum(@Param(value = "userId") Long userId);

    @Select({
            "<script>",
            "SELECT SUM(withdraw_money) as money from zcy_withdraws where user_id=#{userId,jdbcType=BIGINT} and is_withdraw=2",
            "</script>"
    })
    BigDecimal GetTooSum(@Param(value = "userId") Long userId);

    @Select({
            "<script>",
            "SELECT withdraw_id as withdrawId,withdraw_number as withdrawNumber,",
            "withdraw_money as withdrawMoney,user_id as userId,is_withdraw as isWithdraw,addtime,paytime,",
            "(SELECT zu.user_name as userName FROM zcy_users zu where zu.user_id =user_id)userName",
            " FROM zcy_withdraws where user_id=#{userId,jdbcType=BIGINT}",
            "<if test='isw!=null'> and is_withdraw=#{isw,jdbcType=BIT} </if>",
            "</script>"

    })
    List<WithdrawsVo> Get(@Param(value = "userId") Long userId, @Param(value = "isw") Integer isw);

    @Select({
            "<script>",
            "SELECT withdraw_id as withdrawId,withdraw_number as withdrawNumber,",
            "withdraw_money as withdrawMoney,user_id as userId,is_withdraw as isWithdraw,addtime,paytime,",
            "(SELECT zu.user_name as userName FROM zcy_users zu where zu.user_id =user_id)userName",
            " FROM zcy_withdraws ",
            "<if test='isw!=null'> where is_withdraw=#{isw,jdbcType=BIT} </if>",
            "</script>"

    })
    List<WithdrawsVo> GetAdmin( @Param(value = "isw") Integer isw);

}
