package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.TestUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestUserEntityMapper extends MyMapper<TestUserEntity> {
//    @Delete({
//            "delete from test_user",
//            "where test_user_id = #{testUserId,jdbcType=BIGINT}"
//    })
//    int deleteByPrimaryKey(Long testUserId);
//
//    @Insert({
//            "insert into test_user (test_user_id, user_id, ",
//            "name)",
//            "values (#{testUserId,jdbcType=BIGINT}, #{userId,jdbcType=BIGINT}, ",
//            "#{name,jdbcType=VARCHAR})"
//    })
//    int insert(TestUserEntity record);
//
//    @InsertProvider(type = TestUserEntitySqlProvider.class, method = "insertSelective")
//    int insertSelective(TestUserEntity record);
//
//    @Select({
//            "select",
//            "test_user_id, user_id, name",
//            "from test_user",
//            "where test_user_id = #{testUserId,jdbcType=BIGINT}"
//    })
//    @ConstructorArgs({
//            @Arg(column = "test_user_id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
//            @Arg(column = "user_id", javaType = Long.class, jdbcType = JdbcType.BIGINT),
//            @Arg(column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR)
//    })
//    TestUserEntity selectByPrimaryKey(Long testUserId);
//
//    @UpdateProvider(type = TestUserEntitySqlProvider.class, method = "updateByPrimaryKeySelective")
//    int updateByPrimaryKeySelective(TestUserEntity record);
//
//    @Update({
//            "update test_user",
//            "set user_id = #{userId,jdbcType=BIGINT},",
//            "name = #{name,jdbcType=VARCHAR}",
//            "where test_user_id = #{testUserId,jdbcType=BIGINT}"
//    })
//    int updateByPrimaryKey(TestUserEntity record);
}