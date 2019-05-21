package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.ArticlesMapper;
import com.goodsogood.ows.model.db.ArticlesEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
/**
 * 文章
 * */
public class ArticlesService {
    private ArticlesMapper mapper;

    public ArticlesService(ArticlesMapper articlesMapper) {
        this.mapper = articlesMapper;
    }

    /**
     * 新增文章
     *
     * @param articlesEntity
     * @return
     */
    public Boolean AddArticle(ArticlesEntity articlesEntity) {
        return this.mapper.Insert(articlesEntity) > 0;
    }

    /**
     * 修改文章内容
     *
     * @param id      文章唯一id
     * @param title   文章标题
     * @param content 文章内容
     * @return
     */
    public Boolean AlterArticle(Long id, String title, String content) {
        return this.mapper.Update(id, title, content) > 0;
    }

    /**
     *  根据类型查询文章
     * @param type  文章类型
     * @return
     */
    public List<ArticlesEntity> GetByType(Integer type) {
        return this.mapper.Get(type);
    }

    
}
