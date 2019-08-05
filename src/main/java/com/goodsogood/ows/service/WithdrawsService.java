package com.goodsogood.ows.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.helper.OrderGeneratorUtils;
import com.goodsogood.ows.mapper.WithdrawsMapper;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.db.WithdrawsEntity;
import com.goodsogood.ows.model.vo.*;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class WithdrawsService {
    private WithdrawsMapper mapper;

    public WithdrawsService(WithdrawsMapper withdrawsMapper) {
        this.mapper = withdrawsMapper;
    }

    /**
     *  添加 提现请求
     * @param entity
     * @return
     */
    public LoginResult Insert(WithdrawsEntity entity)
    {
        LoginResult result =new LoginResult();
        result.setIsb(false);
        result.setMsg("提交失败");

        UserMoneyVo vo= this.mapper.GetSumUser(entity.getUserId());
        if(vo == null)
        {
            result.setMsg("无资金可提现");
            return result;
        }
        if(vo.getNowMoney() - entity.getWithdrawMoney() < 0)
        {
            result.setMsg("提现金额超出");
            return  result;
        }
        entity.setStatus(1);
        entity.setIsWithdraw(2);
        entity.setAddtime(new Date());
        entity.setPaytime(new Date());
        entity.setWithdrawNumber(OrderGeneratorUtils.getOrderIdByUUId());
       int num =  this.mapper.Insert(entity);
       if(num > 0)
       {
           result.setIsb(true);
           result.setMsg("提交成功");
       }
       return  result;
    }

    /**
     *  管理员查询资金流水(提现记录)
     */
    public  PageInfo<WithdrawsSonVo> GetPageAdmin(Integer status,String keyValue,PageNumber pageNumber)
    {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return  new PageInfo<>(this.mapper.GetPageAdmin(status,keyValue));
    }

    /**
     *  用户查询个人资金流水记录
     */
    public  PageInfo<WithdrawsSonVo> GetPageUser( Long userId ,Integer status,PageNumber pageNumber){
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return  new PageInfo<>(this.mapper.GetPageUser(userId,status));
    }

    /***
     *  个人平台资金合计
     */
    public UserMoneyVo GetSumUser( Long userId){
        return  this.mapper.GetSumUser(userId);
    }

    /**
     *  修改提现状态
     */
    public  LoginResult UpdateStatus( Long userId, Long withdraw_id, int status ){
        LoginResult result =new LoginResult();
        result.setIsb(false);
        result.setMsg("修改失败");
        int num =this.mapper.UpdateStatus(userId,withdraw_id,status);
        if(num > 0)
        {
            result.setIsb(true);
            result.setMsg("修改成功");
            return  result;
        }
        return  result;

    }

    /**
     *  修改提现金额
     */
    public  LoginResult  UpdateMoney(Long userId,Long withdraw_id,BigDecimal money )
    {
        LoginResult result = new LoginResult();
        result.setIsb(false);
        result.setMsg("修改失败");
        int num =this.mapper.UpdateMoney(userId,withdraw_id,money);
        if(num > 0)
        {
            result.setIsb(true);
            result.setMsg("修改成功");
            return  result;
        }
        return  result;
    }


//    /**
//     * 个人 提现(单个提现)
//     *
//     * @param wid 流水唯一标识
//     * @return
//     */
//    @Transactional
//    public Boolean cashOut(Long wid) {
//        return this.mapper.Update(wid) > 0;
//    }
//
//    /**
//     * 批量提现
//     *
//     * @param wids 资金流水集合唯一标识
//     * @return
//     */
//    @Transactional
//    public Boolean cashOuts(List<Long> wids) {
//        for (Long db : wids) {
//            this.mapper.Update(db);
//        }
//        return true;
//    }
//
//    /**
//     * 查询 个人金额 未提现金额
//     *
//     * @param userId
//     * @return
//     */
//    public BigDecimal getSum(Long userId) {
//        return this.mapper.GetNotSum(userId);
//    }
//
//    /**
//     * 查询 个人金额  已提现金额
//     *
//     * @param userId
//     * @return
//     */
//    public BigDecimal getSumToo(Long userId) {
//        return this.mapper.GetTooSum(userId);
//    }
//
//
//    /**
//     * 查询流水
//     *
//     * @param userId
//     * @param is
//     * @param pageNumber
//     * @return
//     */
//    public PageInfo<WithdrawsVo> Get(Long userId, Integer is, PageNumber pageNumber) {
//        int p = Preconditions.checkNotNull(pageNumber.getPage());
//        int r = Preconditions.checkNotNull(pageNumber.getRows());
//        PageHelper.startPage(p, r);
//        return new PageInfo<>(this.mapper.Get(userId, is));
//    }
//
//    /***
//     * 查询流水
//     * @param is
//     * @param pageNumber
//     * @return
//     */
//    public PageInfo<WithdrawsVo> GetAdmin(Integer is, PageNumber pageNumber) {
//        int p = Preconditions.checkNotNull(pageNumber.getPage());
//        int r = Preconditions.checkNotNull(pageNumber.getRows());
//        PageHelper.startPage(p, r);
//        return new PageInfo<>(this.mapper.GetAdmin(is));
//    }
//
//    public List<WithdrawsVo> GetOut(Integer is) {
//        return this.mapper.GetAdmin(is);
//    }
//
//
//    /**
//     * 分组查询 每人需要提现多少钱
//     *
//     * @param pageNumber
//     * @return
//     */
//    public PageInfo<WithdrawSumVo> GetAdminSum(PageNumber pageNumber) {
//
//        int p = Preconditions.checkNotNull(pageNumber.getPage());
//        int r = Preconditions.checkNotNull(pageNumber.getRows());
//        PageHelper.startPage(p, r);
//        return new PageInfo<>(this.mapper.GetSumAdmin());
//    }
//
//    /**
//     * 根据用户唯一标识 批量体现
//     *
//     * @param userId
//     * @return
//     */
//    public Boolean SetCash(Long userId) {
//        Integer num = this.mapper.UpdateList(userId);
//        if (num == null) {
//            return false;
//        } else if (num == 0) {
//            return false;
//        } else {
//            return true;
//        }
//    }


}
