package com.goodsogood.ows.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.mapper.NewsMapper;
import com.goodsogood.ows.model.db.NewsEntity;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.LoginResult;
import com.goodsogood.ows.model.vo.NewsVo;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class NewsService {
    private  final NewsMapper mapper;

    @Autowired
    public NewsService(NewsMapper newsMapper) {
        this.mapper=newsMapper;
    }

    /**
     *  添加
     * @param newsEntity
     * @return
     */
    public  Boolean Add(NewsEntity newsEntity) {
        int num=this.mapper.Insert(newsEntity);
        if(num <= 0){
            return  false;
        }
        return  true;
    }

    /**
     *  修改
     * @param newsEntity
     * @return
     */
    public  Boolean Edit(NewsEntity newsEntity) {
        int num=this.mapper.Update(newsEntity.getNewsId(),newsEntity.getTitle(),newsEntity.getContent(),newsEntity.getDescribes());
        if(num <=0) {
            return  false;
        }
        return  true;
    }

    /**
     *  查询所有
     * @param title
     * @param pageNumber
     * @return
     */
    public PageInfo<NewsVo> Get(String title, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.Get(title));
    }

    /**
     * 精确查询
     * @param id
     * @return
     */
    public  NewsEntity GetFind(Long id) {
        NewsEntity entity=this.mapper.GetFind(id);
        if(entity ==null) {
            return  new NewsEntity();
        }
        return  entity;
    }
    /**
     *  管理员 删除
     * @param newId
     * @return
     */
    public LoginResult Del(Long newId)
    {
        LoginResult result =new LoginResult();
        result.setIsb(false);
        result.setMsg("删除失败");
        int num=this.mapper.CustomDelete(newId);
        boolean isb=num > 0 ? true:false;
        if(isb){
            result.setIsb(isb);
            result.setMsg("删除成功");
        }
        return  result;

    }
}
