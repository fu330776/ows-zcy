package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.AccountsEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccountsMapper extends MyMapper<AccountsEntity> {

    @Insert({
            "<script>",
            "INSERT INTO zcy_accounts(",
            "<if test='AccountId !=null' >account_id,</if> ",
            "phone,pass_word,pass_word_laws)",
            " VALUES( ",
            "<if test='AccountId !=null' >#{AccountId,jdbcType=BIGINT},</if>",
            " #{phone,jdbcType=VARCHAR},#{PassWord,jdbcType=VARCHAR},",
            "#{PassWordLaws,jdbcType=VARCHAR} )",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "AccountId", keyColumn = "account_id")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "account_id", keyProperty = "AccountId", before = false,resultType = Long.class)
    Long Insert(AccountsEntity accountsEntity);

    @Select({
            "<script>",
            "SELECT  account_id as AccountId,phone,pass_word as PassWord,pass_word_laws as PassWordLaws,`enable` as Enable FROM zcy_accounts WHERE phone=#{phone,jdbcType=VARCHAR}",
            "</script>"
    })
    AccountsEntity GetByPhone(@Param(value = "phone") String phone);

    @Select({
            "<script>",
            "SELECT " ,
            "account_id as AccountId,phone,pass_word as PassWord,",
            "pass_word_laws as PassWordLaws,`enable` as Enable",
            "FROM zcy_accounts WHERE phone=#{phone,jdbcType=VARCHAR} and pass_word=#{password,jdbcType=VARCHAR} and enable=1",
            "</script>"
    })
    AccountsEntity GetByUser(@Param(value = "phone") String phone, @Param(value = "password") String password);


    @Update({
            "<script>",
            "UPDATE zcy_accounts SET pass_word_laws=#{newPwd,jdbcType=VARCHAR},pass_word=#{pwds,jdbcType=VARCHAR} ",
            "where phone=#{Phone,jdbcType=VARCHAR}",
            "</script>"
    })
    int UpdatePwd(@Param(value = "Phone") String Phone, @Param(value = "newPwd")
            String newPwd, @Param(value = "pwds") String mad5pwd);

    @Update({
            "<script>",
            "UPDATE zcy_accounts set",
            " pass_word=#{Md5Pwd,jdbcType=VARCHAR},pass_word_laws=#{pwd,jdbcType=VARCHAR} ",
            "where account_id=(SELECT account_id FROM zcy_accounts_users_roles WHERE user_id=#{userid,jdbcType=BIGINT})",
            "</script>"
    })
    int UpdatePassword(@Param(value = "userid") Long userid, @Param(value = "pwd") String pwd, @Param(value = "Md5Pwd") String Md5Pwd);
}
