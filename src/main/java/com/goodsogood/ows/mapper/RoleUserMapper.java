package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.RoleUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RoleUserMapper extends MyMapper<RoleUserEntity> {

    @Select({
            "<script>",
            "SELECT",
            "zr.user_id as userId,zr.role_id as roleId,zu.role_name as roleName, (select z.`enable` FROM zcy_users z where z.user_id=zr.user_id) isDel",
            "FROM zcy_accounts_users_roles zr ",
            "left JOIN zcy_roles zu on zr.role_id=zu.role_id where  ",
            " zr.account_id=#{id,jdbcType=BIGINT} ",
            "</script>",

    })
    List<RoleUserEntity> GetList(@Param(value = "id") Long id);

    @Select({
            "<script>",
            "SELECT * FROM zcy_accounts_users_roles zr ",
            "left JOIN zcy_users zu on zr.user_id=zu.user_id where zu.review=2 and `enable`=1 ",
            "and  zr.account_id=#{id,jdbcType=BIGINT} ",
            "</script>",
    })
    List<RoleUserEntity> GetLists(@Param(value = "id") Long id);
}
