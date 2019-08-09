package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.AccountsUsersRolesEntity;
import com.goodsogood.ows.model.vo.AccountUsersVo;
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
//            "SELECT a.account_id,a.phone,a.pass_word,a.`enable`,a.` addtime`",
//            "FROM zcy_accounts a LEFT JOIN zcy_accounts_users_roles zr ON",
//            "a.account_id=zr.account_id WHERE a.phone=#{phone,jdbcType=VARCHAR} and zr.role_id=#{roleid,jdbcType=VARCHAR}",

            "select ",
            " aur_id as AurId, account_id as AccountId ,user_id as UserId,role_id as RoleId",
            "from zcy_accounts_users_roles where account_id=#{id,jdbcType=BIGINT} and role_id=#{rid,jdbcType=VARCHAR}",
            "</script>"
    })
    AccountsUsersRolesEntity Get(@Param(value = "id") Long id, @Param(value = "rid") Long rid);

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

    @Select({
            "<script>",
            "SELECT COUNT(*) as count,",
             "(SELECT  COUNT(*) as oneCount FROM (SELECT zaur.role_id from zcy_accounts_users_roles zaur left join zcy_users zu on zaur.user_id=zu.user_id WHERE zu.`enable`=1 and zu.review=2 and zaur.role_id=2) as c )oneCount,",
            "(SELECT  COUNT(*) as oneCount FROM (SELECT zaur.role_id from zcy_accounts_users_roles zaur left join zcy_users zu on zaur.user_id=zu.user_id WHERE zu.`enable`=1 and zu.review=2 and zaur.role_id=3) as c )twoCount,",
            "(SELECT  COUNT(*) as oneCount FROM (SELECT zaur.role_id from zcy_accounts_users_roles zaur left join zcy_users zu on zaur.user_id=zu.user_id WHERE zu.`enable`=1 and zu.review=2 and zaur.role_id=4) as c )threeCount",
            "from zcy_accounts_users_roles zaur left join zcy_users zu on zaur.user_id=zu.user_id",
            "WHERE zu.`enable`=1 and zu.review=2 and zaur.role_id>1",
            "</script>"
    })
    List<AccountUsersVo> GetCount();

    @Select(
            {
                    "<script>",
                    "SELECT COUNT(0) FROM zcy_accounts_users_roles where role_id=#{roleId,jdbcType=BIGINT}  and account_id=#{AccountId,jdbcType=BIGINT} ",
                    "</script>"
            }
    )
    int GetIsNum(@Param(value = "AccountId") Long AccountId,@Param(value = "roleId") Long roleId);

    /**
     *  查询账号是否存在多个账号
     * @param userId
     * @return
     */
    @Select({
            "<script>",
            "SELECT aur_id as AurId, account_id as AccountId ,user_id as UserId,role_id as RoleId ",
            "from zcy_accounts_users_roles where account_id = (SELECT account_id from zcy_accounts_users_roles where user_id=#{userId,jdbcType=BIGINT} )",
            "</script>"
    })
    List<AccountsUsersRolesEntity> GET(@Param(value = "userId") Long userId );



}
