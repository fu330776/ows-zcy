package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.WithdrawsEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

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
}
