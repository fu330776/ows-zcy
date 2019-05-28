package com.goodsogood.ows.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.mapper.WithdrawsMapper;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.WithdrawSumVo;
import com.goodsogood.ows.model.vo.WithdrawsVo;
import com.google.common.base.Preconditions;
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
     * 查询 个人金额  已提现金额
     *
     * @param userId
     * @return
     */
    public BigDecimal getSumToo(Long userId) {
        return this.mapper.GetTooSum(userId);
    }


    /**
     * 查询流水
     *
     * @param userId
     * @param is
     * @param pageNumber
     * @return
     */
    public PageInfo<WithdrawsVo> Get(Long userId, Integer is, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.Get(userId, is));
    }

    /***
     * 查询流水
     * @param is
     * @param pageNumber
     * @return
     */
    public PageInfo<WithdrawsVo> GetAdmin(Integer is, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetAdmin(is));
    }

    public List<WithdrawsVo> GetOut(Integer is) {
        return this.mapper.GetAdmin(is);
    }


    /**
     * 分组查询 每人需要提现多少钱
     *
     * @param pageNumber
     * @return
     */
    public PageInfo<WithdrawSumVo> GetAdminSum(PageNumber pageNumber) {

        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetSumAdmin());
    }

    /**
     * 根据用户唯一标识 批量体现
     *
     * @param userId
     * @return
     */
    public Boolean SetCash(Long userId) {
        Integer num = this.mapper.UpdateList(userId);
        if (num == null) {
            return false;
        } else if (num == 0) {
            return false;
        } else {
            return true;
        }
    }


}
