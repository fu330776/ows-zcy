package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.CodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CodeMapper extends  MyMapper<CodeEntity> {

    @Select(
            { "SELECT code_id,code_code FROM zcy_code where code_code=#{code_code,jdbcType=VARCHAR}" }
    )
    CodeEntity Get(@Param(value = "code_code") String code_code);
}
