package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.AccountsEntity;
import com.goodsogood.ows.model.db.AccountsUsersRolesEntity;
import com.goodsogood.ows.model.db.MenusEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AccountsUsersRolesMapper extends MyMapper<AccountsUsersRolesEntity> {

    @Insert({
            "<script>",
            "INSERT INTO zcy_accounts_users_roles(",
            "<if test='AurId !=null' >aur_id,</if> ",
            "account_id,user_id,role_id",
            ") VALUES(",
            "<if test='AurId !=null' >#{AurId,jdbcType=BIGINT},</if> ",
            "#{AccountId,jdbcType=BIGINT},#{UserId,jdbcType=BIGINT},",
            "#{RoleId,jdbcType=BIGINT}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "AurId", keyColumn = "aur_id")
    Long RewriteInsert(AccountsUsersRolesEntity accountsUsersRolesEntity);

    @Select({
            "<script>",
            "SELECT a.account_id,a.phone,a.pass_word,a.`enable`,a.` addtime`",
            "FROM zcy_accounts a LEFT JOIN zcy_accounts_users_roles zr ON",
            "a.account_id=zr.account_id WHERE a.phone=#{phone,jdbcType=VARCHAR} and zr.role_id=#{roleid,jdbcType=VARCHAR}",
            "</script>"
    })
    AccountsUsersRolesEntity Get(@Param(value = "phone") String phone, @Param(value = "roleid") String roleid);

    @Select({
            "<script>",
            "SELECT zr.user_id as userId FROM zcy_accounts za ",
            "LEFT JOIN  zcy_accounts_users_roles zr  on za.account_id=zr.account_id",
            "LEFT JOIN zcy_users zu ON zr.user_id=zu.user_id",
            "WHERE za.phone=#{phone,jdbcType=VARCHAR} and za.pass_word=#{pwd,jdbcType=VARCHAR}",
            "and zr.role_id=1 and  zu.`enable`=1 and zu.review=2 and za.`enable`=1 ",
            "</script>"
    })
    Long GetAdminFind(@Param(value = "phone") String phone, @Param(value = "pwd") String pwd);

    @Select({
            "<script>",
            "SELECT account_id as AccountId,user_id as userId,aur_id as AurId, role_id as roleId  FROM zcy_accounts_users_roles",
            "where user_id=#{userId,jdbcType=VARCHAR}",
            "</script>"
    })
    AccountsUsersRolesEntity GetUserId(@Param(value = "userId") Long userId);

}
