package com.goodsogood.ows.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.mapper.PatentsMapper;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.db.PatentsEntity;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PatentsService {
    private PatentsMapper mapper;

    public PatentsService(PatentsMapper patentsMapper) {
        this.mapper = patentsMapper;
    }

    /**
     * 申请专利
     */
    public Boolean Insert(PatentsEntity patentsEntity) {
        int num = this.mapper.Insert(patentsEntity);
        return num > 0 ? true : false;
    }


    /**
     * 分页查询用户（根据类型查询）
     *
     * @param userId
     * @param type
     * @param pageNumber
     * @return
     */
    public PageInfo<PatentsEntity> Get(Long userId, Integer type, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.Get(userId, type));
    }

    /**
     * 分页查询管理员（根据类型查询）
     *
     * @param type
     * @param pageNumber
     * @return
     */
    public PageInfo<PatentsEntity> GetAll(Integer type, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetByType(type));
    }

    /**
     * 根据唯一标识查询
     *
     * @param pid
     * @return
     */
    public PatentsEntity GetFind(Long pid) {
        return this.mapper.GetById(pid);
    }


}
