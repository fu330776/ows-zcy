package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.UsersEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UsersMapper extends MyMapper<UsersEntity> {
    @Insert({
            "<script>",
            "INSERT INTO zcy_accounts(" ,
            "<if test='userId !=null' >user_id,</if> ",
            "user_name,user_hospital,user_department,",
            "user_position,user_email,user_bank_card_number,user_cardholder_name,",
            "user_cardholder_phone,user_cardholder_idcard,review,enable,code,referrer,",
            "is_referrer,addtime,updatetime",
            ") VALUES(",
            "<if test='userId !=null' >#{userId,jdbcType=BIGINT},</if> ",
            "#{userName,jdbcType=VARCHAR},#{userHospital,jdbcType=VARCHAR},",
            "#{userDepartment,jdbcType=VARCHAR},#{userPosition,jdbcType=VARCHAR},",
            "#{userEmail,jdbcType=VARCHAR},{userBankCardNumber,jdbcType=VARCHAR},",
            "#{userCardholderName,jdbcType=VARCHAR},#{userCardholderPhone,jdbcType=VARCHAR},",
            "#{userCardholderIdcard,jdbcType=VARCHAR},#{review,jdbcType=BIT},",
            "#{enable,jdbcType=BIT},#{code,jdbcType=VARCHAR},#{referrer,jdbcType=BIGINT},",
            "#{isReferrer,jdbcType=BIT},#{addtime,jdbcType=TIMESTAMP},#{updatetime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    Long PersonalInsert(UsersEntity usersEntity);

    @Insert({
            "<script>",
            "INSERT INTO zcy_accounts(" ,
            "<if test='userId !=null' >user_id,</if> ",
            "company_name,company_code,review,enable,code,referrer,",
            "is_referrer,addtime,updatetime",
            ") VALUES(",
            "<if test='userId !=null' >#{userId,jdbcType=BIGINT},</if> ",
            "#{companyName,jdbcType=VARCHAR},#{companyCode,jdbcType=VARCHAR},#{review,jdbcType=BIT},",
            "#{enable,jdbcType=BIT},#{code,jdbcType=VARCHAR},#{referrer,jdbcType=BIGINT},",
            "#{isReferrer,jdbcType=BIT},#{addtime,jdbcType=TIMESTAMP},#{updatetime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    Long CompanyInsert(UsersEntity usersEntity);

    @Insert({
            "<script>",
            "INSERT INTO zcy_accounts(" ,
            "<if test='userId !=null' >user_id,</if> ",
            "organization_name,organization_code,review,enable,code,referrer,is_referrer,addtime,updatetime",
            ") VALUES(",
            "<if test='userId !=null' >#{userId,jdbcType=BIGINT},</if> ",
            "#{organizationName,jdbcType=VARCHAR},#{organizationCode,jdbcType=VARCHAR},#{review,jdbcType=BIT},",
            "#{enable,jdbcType=BIT},#{code,jdbcType=VARCHAR},#{referrer,jdbcType=BIGINT},",
            "#{isReferrer,jdbcType=BIT},#{addtime,jdbcType=TIMESTAMP},#{updatetime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    Long MechanismInsert(UsersEntity usersEntity);

    @Insert({
            "<script>",
            "INSERT INTO zcy_accounts(" ,
            "<if test='userId !=null' >user_id,</if> ",
            "user_name,user_hospital,user_department,",
            "user_position,user_email,user_bank_card_number,user_cardholder_name,",
            "user_cardholder_phone,user_cardholder_idcard,company_name,company_code,organization_name,organization_code,review,enable,code,referrer,",
            "is_referrer,addtime,updatetime",
            ") VALUES(",
            "<if test='userId !=null' >#{userId,jdbcType=BIGINT},</if> ",
            "#{userName,jdbcType=VARCHAR},#{userHospital,jdbcType=VARCHAR},",
            "#{userDepartment,jdbcType=VARCHAR},#{userPosition,jdbcType=VARCHAR},",
            "#{userEmail,jdbcType=VARCHAR},{userBankCardNumber,jdbcType=VARCHAR},",
            "#{userCardholderName,jdbcType=VARCHAR},#{userCardholderPhone,jdbcType=VARCHAR},",
            "#{userCardholderIdcard,jdbcType=VARCHAR},#{companyName,jdbcType=VARCHAR},#{companyCode,jdbcType=VARCHAR},#{organizationName,jdbcType=VARCHAR},#{organizationCode,jdbcType=VARCHAR},#{review,jdbcType=BIT},",
            "#{enable,jdbcType=BIT},#{code,jdbcType=VARCHAR},#{referrer,jdbcType=BIGINT},",
            "#{isReferrer,jdbcType=BIT},#{addtime,jdbcType=TIMESTAMP},#{updatetime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    Long Insert(UsersEntity usersEntity);





}
