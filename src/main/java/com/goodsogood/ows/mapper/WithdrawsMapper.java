package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.WithdrawsEntity;
import com.goodsogood.ows.model.vo.WithdrawSumVo;
import com.goodsogood.ows.model.vo.WithdrawsVo;
import jdk.nashorn.internal.objects.annotations.Setter;
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

    @Update({
            "<script>",
            "UPDATE zcy_withdraws SET is_withdraw=2 where user_id=#{userId,jdbcType=BIGINT} and is_withdraw=1",
            "</script>"
    })
    int UpdateList(@Param(value = "userId") Long userId);


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
            "select zw.withdraw_id as withdrawId,zw.withdraw_number as withdrawNumber,",
            "zw.withdraw_money as withdrawMoney,zw.user_id as userId,zw.is_withdraw as isWithdraw,zw.addtime,zw.paytime,zu.user_Name as userName",
            ",zu.user_bank_card_number as bankNumber",
            ",zu.user_cardholder_name as userCardholderName,zu.user_cardholder_phone as userCardholderPhone,zu.user_cardholder_idcard as userCardholderIdcard",
            "from zcy_withdraws zw LEFT JOIN zcy_users zu ON zw.user_id=zu.user_id",
            " where zw.user_id=#{userId,jdbcType=BIGINT}",
            "<if test='isw!=null'> and zw.is_withdraw=#{isw,jdbcType=BIT} </if>",
            "</script>"

    })
    List<WithdrawsVo> Get(@Param(value = "userId") Long userId, @Param(value = "isw") Integer isw);

    @Select({
            "<script>",
            "select zw.withdraw_id as withdrawId,zw.withdraw_number as withdrawNumber,",
            "zw.withdraw_money as withdrawMoney,zw.user_id as userId,zw.is_withdraw as isWithdraw,zw.addtime,zw.paytime,zu.user_Name as userName",
            ",zu.user_bank_card_number as bankNumber",
            ",zu.user_cardholder_name as userCardholderName,zu.user_cardholder_phone as userCardholderPhone,zu.user_cardholder_idcard as userCardholderIdcard",
            "from zcy_withdraws zw LEFT JOIN zcy_users zu ON zw.user_id=zu.user_id",
            "<if test='isw!=null'> where zw.is_withdraw=#{isw,jdbcType=BIT} </if>",
            "</script>"
    })
    List<WithdrawsVo> GetAdmin(@Param(value = "isw") Integer isw);


    @Select({
            "<script>",
            "SELECT user_id as userId,",
            "SUM(withdraw_money) as money,",
            "is_withdraw as isPay,",
            "(SELECT zu.user_name as userName FROM zcy_users zu where zu.user_id =zw.user_id)userName",
            " FROM zcy_withdraws zw where is_withdraw=1 GROUP BY user_id",
            "</script>"
    })
    List<WithdrawSumVo> GetSumAdmin();


}
