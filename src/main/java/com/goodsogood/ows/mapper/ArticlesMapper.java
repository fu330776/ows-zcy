package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.ArticlesEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ArticlesMapper extends MyMapper<ArticlesEntity> {

    @Insert({
            "<script>",
            "INSERT INTO zcy_articles(",
            "<if test='articleId !=null'>article_id,</if>",
            "article_title,article_content,article_type,addtime",
            ")VALUES(",
            "<if test='articleId !=null'>#{articleId,jdbcType=BIGINT},</if>",
            "#{title,jdbcType=VARCHAR},#{content,jdbcType=VARCHAR},",
            "#{type,jdbcType=BIT},#{addtime,jdbcType=TIMESTAMP})",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "articleId", keyColumn = "article_id")
    int Insert(ArticlesEntity articlesEntity);


    @Select({
            "<script>",
            "SELECT article_id as articleId,article_title as title,article_content as content,article_type as type,addtime",
            "FROM zcy_articles where article_type=#{type,jdbcType=BIT}",
            "</script>"
    })
    List<ArticlesEntity> Get(@Param(value = "type") Integer type);


    @Update({
            "<script>",
            "UPDATE zcy_articles SET article_title=#{title,jdbcType=VARCHAR}",
            ",article_content=#{content,jdbcType=VARCHAR} ",
            "WHERE article_id=#{id,jdbcType=BIT}",
            "</script>"
    })
    int Update(@Param(value = "id") Long id, @Param(value = "title") String title, @Param(value = "content") String content);
}
