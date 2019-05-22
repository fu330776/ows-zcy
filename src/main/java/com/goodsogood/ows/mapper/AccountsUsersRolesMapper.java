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


}
