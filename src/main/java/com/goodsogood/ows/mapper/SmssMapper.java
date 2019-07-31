package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.SmssEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Mapper
public interface SmssMapper extends MyMapper<SmssEntity> {

    @Select({
            "<script>",
            "SELECT sms_code FROM zcy_smss   ",
            "where  sms_phone=#{sms_phone,jdbcType=VARCHAR}",
            " and sms_expire_date > #{sms_expire_date,jdbcType=TIMESTAMP}",
            "and sms_type=1 and sms_use=1",
            "</script>"
    })
    String GetSmsCode(@Param(value = "sms_phone") String sms_phone, @Param(value = "sms_expire_date") Date sms_expire_date);


    @Select({
            "<script>",
            "SELECT sms_id as smsId,sms_phone as smsPhone,sms_type as smsType",
            ",sms_send_type as smsSendType,sms_code as smsCode, sms_content as smsContent",
            ",sms_send_frequency as smsSendFrequency, addtime,sms_expire_date as smsExpireDate",
            "from zcy_smss ",
            "where sms_phone =#{sms_phone,jdbcType=VARCHAR} and sms_expire_date>=#{sms_expire_date,jdbcType=TIMESTAMP}",
            "and sms_use=1 and sms_type=#{smsType,jdbcType=BIT}",
            "</script>"
    })
    SmssEntity GetByPhone(@Param(value = "sms_phone") String sms_phone, @Param(value = "sms_expire_date") Date sms_expire_date, @Param(value = "smsType") Integer smsType);

    @Insert({
            "<script>",
            "INSERT INTO zcy_smss(",
            "<if test='smsId!=null'>sms_id,</if>",
            "sms_phone,sms_type,sms_send_type,sms_code,sms_content,",
            "sms_send_frequency,addtime,sms_expire_date,sms_use",
            ")VALUES(",
            "<if test='smsId!=null'>#{smsId,jdbcType=BIGINT},</if>",
            "#{smsPhone,jdbcType=VARCHAR},#{smsType,jdbcType=BIT},",
            "#{smsSendType,jdbcType=BIT},#{smsCode,jdbcType=VARCHAR},",
            "#{smsContent,jdbcType=VARCHAR},#{smsSendFrequency,jdbcType=BIT},",
            "#{addtime,jdbcType=TIMESTAMP},#{smsExpireDate,jdbcType=TIMESTAMP},#{smsUse,jdbcType=BIT}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "smsId", keyColumn = "sms_id")
    int Insert(SmssEntity smssEntity);


    @Update({
            "<script>",
            "UPDATE zcy_smss SET sms_use=2 WHERE  sms_phone=#{sms_phone,jdbcType=VARCHAR} ",
            " and sms_expire_date > #{sms_expire_date,jdbcType=TIMESTAMP}",
            " and sms_use=1",
            "</script>"
    })
    int Update(@Param(value = "sms_phone") String sms_phone, @Param(value = "sms_expire_date") Date sms_expire_date);

    @Select({
            "<script>",
            "select COUNT(*) from zcy_smss where DATEDIFF(addtime,NOW())=0 and sms_phone=#{phone,jdbcType=VARCHAR}",
            "</script>"
    })
    int NowGetCount(@Param(value = "phone") String phone);

}
