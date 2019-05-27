package com.goodsogood.ows.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.mapper.MoneyMapper;
import com.goodsogood.ows.model.db.MoneyEntity;
import com.goodsogood.ows.model.db.PageNumber;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Log4j2
public class MoneyService {
    private MoneyMapper mapper;

    @Autowired
    public MoneyService(MoneyMapper moneyMapper) {
        this.mapper = moneyMapper;
    }

    /**
     * 设置
     *
     * @param moneyEntity
     * @return
     */
    public Integer Add(MoneyEntity moneyEntity) {
        return this.mapper.Insert(moneyEntity);
    }

    /**
     * 修改
     *
     * @param id
     * @param money
     * @return
     */
    public Integer Put(Long id, BigDecimal money) {
        return this.mapper.Update(id, money);
    }

    /**
     * 查询
     *
     * @param pageNumber
     * @return
     */
    public PageInfo<MoneyEntity> Get(PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.Get());
    }

    /**
     * 单个查询
     *
     * @param name
     * @return
     */
    public MoneyEntity GetFind(String name) {
        return this.mapper.GetFind(name);
    }


}
