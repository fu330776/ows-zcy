package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.MoneyEntity;

@Repository
@Mapper
public interface MoneyMapper extends MyMapper<MoneyEntity> {


    @Insert({
            "<script>",
            "INSERT INTO zcy_money(",
            "<if test='moneyId !=null'>money_id, </if>",
            "money_name,money_money,money_del,money_time",
            ")VALUES(",
            "<if test='moneyId !=null'>#{moneyId,jdbcType=BIGINT},</if>",
            "#{name,jdbcType=VARCHAR},#{money,jdbcType=DECIMAL},",
            "#{del,jdbcType=BIT},#{time,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    int Insert(MoneyEntity entity);
}
