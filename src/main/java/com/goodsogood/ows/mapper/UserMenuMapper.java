package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.MenusEntity;
import com.goodsogood.ows.model.db.UserMenuEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMenuMapper extends MyMapper<UserMenuEntity>  {

    /**
     *  插入 返回 自增主键
     * @param entity
     * @return
     */
    @Insert({
            "<script>",
            "INSERT INTO zcy_user_menu(",
            "<if test ='zcyId !=null'> zcy_id, </if>",
            "zcy_userId,zcy_menuId",
            ")VALUES(",
            "<if test ='zcyId != null'> #{zcyId,jdbcType=BIGINT},</if>",
            "#{zcyUserId,jdbcType=BIGINT},#{zcyMenuId,BIGINT}",
            ")",
            "</script>"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "zcy_id", keyProperty = "zcyId", before = false, resultType = Long.class)
    @Options(useGeneratedKeys = true, keyProperty = "zcyId", keyColumn = "zcy_id")
    int Insert(UserMenuEntity entity);

    @Select({
            "<script>",
            "SELECT",
            "menu_id as menuId,menu_icon as menuIcon,menu_name as menuName,menu_url as menuUrl,parent_id as parentId",
            "FROM zcy_menus where menu_id in (SELECT zcy_menuId FROM zcy_user_menu where zcy_id=#{userId,jdbcType=BIGINT})",
            "or parent_id in(SELECT zcy_menuId FROM zcy_user_menu where zcy_id=#{userId,jdbcType=BIGINT})",
            "</script>"
    })
    List<MenusEntity> Get(@Param(value = "userId") Long userId);


    @Delete({
            "<script>",
            " DELETE FROM zcy_user_menu where zcy_id=#{userId,jdbcType=BIGINT} and zcy_menuId=#{menuId,jdbcType=BIGINT}",
            "</script>"
    })
    int DelIsAuditor(@Param(value = "userId") Long userId,@Param(value = "menuId") Long menuId);

}
