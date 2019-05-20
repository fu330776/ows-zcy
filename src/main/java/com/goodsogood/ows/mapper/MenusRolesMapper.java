package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.RolesMenusEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MenusRolesMapper extends MyMapper<RolesMenusEntity> {


}
