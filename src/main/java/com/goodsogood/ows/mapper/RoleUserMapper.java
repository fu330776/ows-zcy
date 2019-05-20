package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.RoleUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RoleUserMapper extends  MyMapper<RoleUserEntity> {

    @Select({
            "<script>",
            "SELECT * FROM zcy_accounts_users_roles ",
            "where account_id=(SELECT * FROM zcy_accounts ",
            "WHERE phone=#{phone,jdbcType=VARCHAR} and pass_word=#{password,jdbcType=VARCHAR} and enable=1)",
            "</script>",

    })
    List<RoleUserEntity> GetList(@Param(value = "phone") String phone,@Param(value = "password") String password);

    @Select({
            "<script>",
            "SELECT * FROM zcy_accounts_users_roles ",
            "where account_id=(SELECT * FROM zcy_accounts ",
            "WHERE phone=#{phone,jdbcType=VARCHAR} and enable=1)",
            "</script>",
    })
    List<RoleUserEntity> GetLists(@Param(value = "phone") String phone);
}
