package com.goodsogood.ows.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.mapper.ArticlesMapper;
import com.goodsogood.ows.model.db.ArticlesEntity;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.LoginResult;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
@Log4j2
/**
 * 文章
 * */
public class ArticlesService {
    private ArticlesMapper mapper;
    @Autowired
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
    public PageInfo<ArticlesEntity> GetByType(Integer type, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.Get(type)) ;

    }


    /**
     *  管理员 删除
     * @param articleId
     * @return
     */
    public LoginResult Del(Long articleId)
    {
        LoginResult result =new LoginResult();
        result.setMsg("删除失败");
        result.setIsb(false);
        int num=this.mapper.CustomDelete(articleId);
        boolean isb=num > 0 ? true:false;
        if(isb){
            result.setIsb(isb);
            result.setMsg("删除成功");
        }
        return  result;

    }
}
