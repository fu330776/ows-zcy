package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.RolesEntity;
import org.apache.ibatis.annotations.Mapper;

import org.springframework.stereotype.Repository;



@Repository
@Mapper
public interface RolesMapper extends MyMapper<RolesEntity> {

}
