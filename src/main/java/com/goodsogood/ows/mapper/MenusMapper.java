package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.MenusEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MenusMapper extends MyMapper<MenusEntity> {
    @Select({
            "<script>",
            "SELECT zm.menu_id as menuId,zm.menu_icon as menuIcon,zm.menu_name as menuName,zm.menu_url as menuUrl,zm.parent_id as parentId ",
            "from zcy_roles_menus zrm ",
            "LEFT JOIN zcy_menus zm on zrm.menu_id=zm.menu_id",
            "where zrm.role_id=#{roleId,jdbcType=BIGINT}",
            "</script>"
    })
    List<MenusEntity> GetLogin(@Param(value = "roleId") Long roleId);
}
