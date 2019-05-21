package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.WithdrawsMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
@Service
@Log4j2
public class WithdrawsService {
    private WithdrawsMapper mapper;

    public WithdrawsService(WithdrawsMapper withdrawsMapper) {
        this.mapper = withdrawsMapper;
    }

    /**
     * 个人 提现(单个提现)
     *
     * @param wid 流水唯一标识
     * @return
     */
    @Transactional
    public Boolean cashOut(Long wid) {
        return this.mapper.Update(wid) > 0;
    }

    /**
     * 批量提现
     *
     * @param wids 资金流水集合唯一标识
     * @return
     */
    @Transactional
    public Boolean cashOuts(List<Long> wids) {
        for (Long db : wids) {
            this.mapper.Update(db);
        }
        return true;
    }

    /**
     * 查询 个人金额 未提现金额
     *
     * @param userId
     * @return
     */
    public BigDecimal getSum(Long userId) {
        return this.mapper.GetNotSum(userId);
    }

    /**
     *  查询 个人金额  已提现金额
     * @param userId
     * @return
     */
    public BigDecimal getSumToo(Long userId) {
        return this.mapper.GetTooSum(userId);
    }
}
