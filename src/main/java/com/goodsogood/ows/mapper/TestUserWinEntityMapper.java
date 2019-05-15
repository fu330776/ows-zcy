package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.TestUserWinEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestUserWinEntityMapper extends MyMapper<TestUserWinEntity> {
//    @Delete({
//            "delete from test_user_win",
//            "where test_user_new_id = #{testUserNewId,jdbcType=BIGINT}"
//    })
//    int deleteByPrimaryKey(Long testUserNewId);
//
//    @Insert({
//            "insert into test_user_win (test_user_new_id, winner, ",
//            "win_user)",
//            "values (#{testUserNewId,jdbcType=BIGINT}, #{winner,jdbcType=BIGINT}, ",
//            "#{winUser,jdbcType=VARCHAR})"
//    })
//    int insert(TestUserWinEntity record);
//
//    @InsertProvider(type = TestUserWinEntitySqlProvider.class, method = "insertSelective")
//    int insertSelective(TestUserWinEntity record);
//
//    @Select({
//            "select",
//            "test_user_new_id, winner, win_user",
//            "from test_user_win",
//            "where test_user_new_id = #{testUserNewId,jdbcType=BIGINT}"
//    })
//    @ConstructorArgs({
//            @Arg(column = "test_user_new_id", javaType = Long.class, jdbcType = JdbcType.BIGINT, id = true),
//            @Arg(column = "winner", javaType = Long.class, jdbcType = JdbcType.BIGINT),
//            @Arg(column = "win_user", javaType = String.class, jdbcType = JdbcType.VARCHAR)
//    })
//    TestUserWinEntity selectByPrimaryKey(Long testUserNewId);
//
//    @UpdateProvider(type = TestUserWinEntitySqlProvider.class, method = "updateByPrimaryKeySelective")
//    int updateByPrimaryKeySelective(TestUserWinEntity record);
//
//    @Update({
//            "update test_user_win",
//            "set winner = #{winner,jdbcType=BIGINT},",
//            "win_user = #{winUser,jdbcType=VARCHAR}",
//            "where test_user_new_id = #{testUserNewId,jdbcType=BIGINT}"
//    })
//    int updateByPrimaryKey(TestUserWinEntity record);
}