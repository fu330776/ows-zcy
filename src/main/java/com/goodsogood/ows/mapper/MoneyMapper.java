package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.MoneyEntity;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

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

    @Update({
            "<script>",
            "UPDATE zcy_money SET money_money=#{money,jdbcType=DECIMAL} WHERE money_id=#{id,jdbcType=BIGINT}",
            "</script>",
    })
    int Update(@Param(value = "id") Long id, @Param(value = "money") BigDecimal money);

    @Select({
            "<script>",
            "select money_id as moneyId,money_name as name,money_money as money ,money_del as del,money_time as time from zcy_money where money_del=1",
            "</script>"
    })
    List<MoneyEntity> Get();

    @Select({
            "<script>",
            "select money_id as moneyId,money_name as name,money_money as money ,money_del as del,money_time as time from zcy_money where money_del=1 and money_name=#{name,jdbcType=VARCHAR}",
            "</script>"
    })
    MoneyEntity GetFind(@Param(value = "name") String name);
}
