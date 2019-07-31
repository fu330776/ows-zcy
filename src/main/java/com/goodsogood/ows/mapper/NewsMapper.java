package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.NewsEntity;
import com.goodsogood.ows.model.vo.NewsVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NewsMapper extends MyMapper<NewsEntity> {

    @Insert({
        "<script>",
            "INSERT INTO zcy_news(news_title,news_content,news_describe,news_time)",
            "VALUES(",
            "#{title,jdbcType=VARCHAR},#{content,jdbcType=VARCHAR},#{describes,jdbcType=VARCHAR},#{time,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "newsId", keyColumn = "news_id")
    int Insert(NewsEntity newsEntity);

    @Update({
            "<script>",
            "UPDATE zcy_news SET news_title=#{title,jdbcType=VARCHAR},news_content=#{content,jdbcType=VARCHAR},news_describe=#{describe,jdbcType=VARCHAR}",
            "WHERE news_id=#{id,jdbcType=BIGINT}",
            "</script>"
    })
    int Update(@Param(value = "id") Long id
            ,@Param(value = "title") String title
            ,@Param(value = "content") String content
            ,@Param(value = "describe") String describe );

    @Select({
            "<script>",
            "SELECT news_id as newsId,news_content as content,news_describe as describes,news_title as title,news_time as time",
            "FROM zcy_news <if test='title !=null'>",
            "where news_title=like concat(concat('%',#{title,jdbcType=VARCHAR}),'%')",
            "</if> ORDER BY news_time DESC",
            "</script>"
    })
    List<NewsVo> Get(@Param(value = "title") String title);

    @Select({
            "<script>",
            "SELECT news_id as newsId,news_content as content,news_describe as describes,news_title as title,news_time as time",
            "FROM zcy_news",
            "where news_id=#{id,jdbcType=BIGINT}",
            "</script>"
    })
    NewsEntity GetFind(@Param(value = "id")Long id);
}
