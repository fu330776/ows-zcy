package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.CodeEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CodeMapper extends MyMapper<CodeEntity> {

    @Select(
            {"<script>", "SELECT code_id,code_code FROM zcy_code where code_code=#{code_code,jdbcType=VARCHAR}", "</script>"}
    )
    CodeEntity Get(@Param(value = "code_code") String code_code);


    @Insert({
            "<script>",
            "Insert into zcy_code(",
            "<if test='codeId !=null'>code_id, </if> code_code)",
            "VALUES(",
            "<if test='codeId !=null'>#{codeId,jdbcType=BIGINT}, </if> #{code_code,jdbcType=VARCHAR}) ",
            "</script>"

    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "code_id", keyProperty = "codeId", before = false, resultType = Long.class)
    @Options(useGeneratedKeys = true, keyProperty = "codeId", keyColumn = "code_id")
    Long Insert(CodeEntity codeEntity);
}
