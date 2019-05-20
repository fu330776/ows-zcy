package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.SmssEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
            "and sms_type=1",
            "</script>"
    })
    String GetSmsCode(@Param(value = "sms_phone") String sms_phone, @Param(value = "sms_expire_date") Date sms_expire_date);

}
