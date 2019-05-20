package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.FundsMapper;
import com.goodsogood.ows.model.db.FundsEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class FundsService {
    private FundsMapper mapper;

    @Autowired
    public FundsService(FundsMapper fundsMapper) {
        this.mapper = fundsMapper;
    }

    /**
     * 申请医创梦计划基金
     */
    public Integer AddFuns(FundsEntity fundsEntity) {
        Integer result = this.mapper.Insert(fundsEntity);
        return result;
    }

    /**
     * 修改 医创梦计划基金
     */
    public Integer AlterFuns(FundsEntity fundsEntity) {
        Integer result = this.mapper.Update(fundsEntity.fundId, fundsEntity.title, fundsEntity.introduction);
        return result;
    }

    /**
     * 用户查询 医创梦计划基金
     */
    public List<FundsEntity> GetByUserId(Long userId) {
        List<FundsEntity> entity = this.mapper.Get(userId);
        return entity;
    }

    /**
     * 管理员查询 医创梦计划基金
     * */
    public List<FundsEntity> GetByAdmin() {
        List<FundsEntity> entity = this.mapper.GetAll();
        return entity;
    }

}
