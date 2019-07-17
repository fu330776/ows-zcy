package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.PaymentEntity;
import com.goodsogood.ows.model.vo.PaynentPayForm;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PaymentMapper extends MyMapper<PaymentEntity> {


    @Insert({
            "<script>",
            "INSERT INTO zcy_payment(",
            "<if test='paymentId !=null'>payment_id, </if>",
            "order_no,payment_total_money,payment_real_money,payment_type,",
            "good_id,good_title,user_id,user_name,pay_status,pay_way,payment_number,",
            "wx_order_no,payment_time,payment_addtime",
            ")VALUES(",
            "<if test='paymentId !=null'>#{paymentId,jdbcType=BIGINT}, </if>",
            "#{orderNo,jdbcType=VARCHAR},#{totalMoney,jdbcType=DECIMAL},",
            "#{realMoney,jdbcType=DECIMAL},#{type,jdbcType=BIT},#{goodId,jdbcType=BIGINT},",
            "#{goodName,jdbcType=VARCHAR},#{userId,jdbcType=BIGINT},#{userName,jdbcType=VARCHAR},",
            "#{Status,jdbcType=VARCHAR},#{payWay,jdbcType=VARCHAR},#{number,jdbcType=BIT},",
            "#{wxOrder,jdbcType=VARCHAR},#{payTime,jdbcType=TIMESTAMP},#{addTime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "paymentId", keyColumn = "payment_id")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "payment_id", keyProperty = "paymentId", before = false, resultType = Long.class)
    int Insert(PaymentEntity entity);


    @Update({
            "<script>",
            "UPDATE zcy_payment SET",
            // "payment_real_money=#{realMoney,jdbcType=DECIMAL},",
            "pay_status=#{Status,jdbcType=VARCHAR},",
            "pay_way=#{payWay,jdbcType=VARCHAR},",
            "wx_order_no=#{wxOrderNo,jdbcType=VARCHAR},",
            "payment_time=#{payTime,jdbcType=TIMESTAMP}",
            "where order_no=#{orderNo,jdbcType=VARCHAR}",
            "</script>"
    })
    int Update(PaynentPayForm form);

    @Select({
            "<script>",
            "SELECT ",
            "payment_id as paymentId,order_no as orderNo,",
            "payment_total_money as totalMoney,payment_real_money as realMoney,",
            "payment_type as type,good_id as goodId,good_title as goodName,",
            "user_id as userId,user_name as userName,pay_status as Status,",
            "pay_way as payWay,payment_number as number,wx_order_no as wxOrderNo,",
            "payment_time as payTime, payment_addtime as addTime",
            "FROM zcy_payment",
            "WHERE good_id=#{gid,jdbcType=BIGINT} and payment_type=#{type,jdbcType=BIT}",
            "</script>"
    })
    PaymentEntity Get(@Param(value = "gid") Long gid, @Param(value = "type") Integer type);

    @Select({
            "<script>",
            "SELECT ",
            "payment_id as paymentId,order_no as orderNo,",
            "payment_total_money as totalMoney,payment_real_money as realMoney,",
            "payment_type as type,good_id as goodId,good_title as goodName,",
            "user_id as userId,user_name as userName,pay_status as Status,",
            "pay_way as payWay,payment_number as number,wx_order_no as wxOrderNo,",
            "payment_time as payTime, payment_addtime as addTime",
            "FROM zcy_payment",
            "WHERE user_id=#{userId,jdbcType=BIGINT}",
            "<if test='status !=null'> and pay_status=#{status,jdbcType=VARCHAR}</if>",
            "<if test='orderNo !=null'> and order_no=#{orderNo,jdbcType=VARCHAR}</if>",
            "</script>"
    })
    List<PaymentEntity> GetByUserId(@Param(value = "userId") Long userId, @Param(value = "orderNo") String orderNo, @Param(value = "status") String status);


    @Select({
            "<script>",
            "SELECT ",
            "payment_id as paymentId,order_no as orderNo,",
            "payment_total_money as totalMoney,payment_real_money as realMoney,",
            "payment_type as type,good_id as goodId,good_title as goodName,",
            "user_id as userId,user_name as userName,pay_status as Status,",
            "pay_way as payWay,payment_number as number,wx_order_no as wxOrderNo,",
            "payment_time as payTime, payment_addtime as addTime",
            "FROM zcy_payment",
            "WHERE 1=1",
            "<if test='status !=null'> and pay_status=#{status,jdbcType=VARCHAR}</if>",
            "<if test='orderNo !=null'> and order_no=#{orderNo,jdbcType=VARCHAR}</if>",
            "<if test='name !=null'> and user_name LIKE CONCAT(CONCAT('%',#{name,jdbcType=VARCHAR},'%'))</if>",
            "</script>"
    })
    List<PaymentEntity> GetByAdmin(@Param(value = "orderNo") String orderNo, @Param(value = "status") String status,@Param(value = "name") String name);

    @Select({
            "<script>",
            "select ",
            "payment_id as paymentId,order_no as orderNo,",
            "payment_total_money as totalMoney,payment_real_money as realMoney,",
            "payment_type as type,good_id as goodId,good_title as goodName,",
            "user_id as userId,user_name as userName,pay_status as Status,",
            "pay_way as payWay,payment_number as number,wx_order_no as wxOrderNo,",
            "payment_time as payTime, payment_addtime as addTime",
            " from zcy_payment where pay_status='未支付'  and order_no=#{orderNo,jdbcType=VARCHAR}",
            "</script>"

    })
    PaymentEntity GetFind(@Param(value = "orderNo") String orderNo);

    @Delete({
            "<script>",
            "delete * from zcy_payment where payment_id=#{paymentId,jdbcType=BIGINT}",
            "</script>"
    })
    int Delete(@Param(value = "paymentId") Long paymentId);
}

