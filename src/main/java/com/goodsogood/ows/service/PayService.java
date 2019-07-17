package com.goodsogood.ows.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.helper.OrderGeneratorUtils;
import com.goodsogood.ows.mapper.PatentsMapper;
import com.goodsogood.ows.mapper.PaymentMapper;
import com.goodsogood.ows.mapper.TasksMapper;
import com.goodsogood.ows.mapper.UsersMapper;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.db.PaymentEntity;
import com.goodsogood.ows.model.db.TasksEntity;
import com.goodsogood.ows.model.vo.PatentsVo;
import com.goodsogood.ows.model.vo.PaynentPayForm;
import com.goodsogood.ows.model.vo.UserInfoVo;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Log4j2
public class PayService {
    private PaymentMapper mapper;
    private TasksMapper tasksMapper;
    private PatentsMapper patentsMapper;
    private UsersMapper usersMapper;

    @Autowired
    public PayService(PaymentMapper paymentMapper, TasksMapper tasksMapper, PatentsMapper patentsMapper, UsersMapper usersMapper) {
        this.mapper = paymentMapper;
        this.tasksMapper = tasksMapper;
        this.patentsMapper = patentsMapper;
        this.usersMapper = usersMapper;
    }

    /**
     * idea确权
     *
     * @param pid
     * @return
     */
    @Transactional
    public PaymentEntity InsertPatents(Long pid) {
        PaymentEntity entity;

        entity = this.Get(pid, 1);
        if (entity == null) {
            PatentsVo vo = this.patentsMapper.GetById(pid);
            UserInfoVo userVo = this.usersMapper.GetUserById(vo.getUserId());
            entity.setAddTime(new Date());
            entity.setGoodId(vo.getPatentId());
            entity.setUserName(vo.getUserName());
            entity.setUserId(vo.getUserId());
            entity.setType(1);
            entity.setTotalMoney(vo.getPatentMoney());
            entity.setStatus("未支付");
            entity.setRealMoney(vo.getPatentMoney());
            entity.setPayWay("微信支付");
            entity.setPayTime(new Date());
            entity.setOrderNo(userVo.getCode() + OrderGeneratorUtils.getOrderIdByTime(userVo.getUserId()));
            entity.setNumber(1);
            entity.setGoodName(vo.getPatentTitle());
            int num = this.mapper.Insert(entity);

        }
        return entity;
    }

    /**
     * 委托书
     *
     * @param tid
     * @return
     */
    @Transactional
    public PaymentEntity InsertTasks(Long tid) {
        PaymentEntity entity;
        entity = this.Get(tid, 2);
        if (entity == null) {
            TasksEntity tasksEntity = this.tasksMapper.GetByTaskId(tid);
            UserInfoVo userVo = this.usersMapper.GetUserById(tasksEntity.getUserId());
            entity.setTotalMoney(tasksEntity.getTaskMoney());
            entity.setAddTime(new Date());
            entity.setGoodId(tasksEntity.getTaskId());
            entity.setUserName(userVo.getUserName());
            entity.setUserId(userVo.getUserId());
            entity.setType(2);
            entity.setStatus("未支付");
            entity.setRealMoney(tasksEntity.getTaskMoney());
            entity.setPayWay("微信支付");
            entity.setPayTime(new Date());
            entity.setOrderNo(userVo.getCode() + OrderGeneratorUtils.getOrderIdByTime(userVo.getUserId()));
            entity.setNumber(1);
            entity.setGoodName(tasksEntity.getTaskName());
            int num = this.mapper.Insert(entity);
        }
        return entity;
    }


    /**
     * 修改订单状态
     *
     * @param orderNo
     * @param wxOrderNo
     */
    @Transactional
    public void Update(String orderNo, String wxOrderNo) {
        PaymentEntity pays = this.mapper.GetFind(orderNo);
        if(pays !=null)
        {
            PaynentPayForm pay = new PaynentPayForm();
            pay.setOrderNo(orderNo);
            pay.setPayTime(new Date());
            pay.setPayWay("微信支付");
            pay.setStatus("已支付");
            pay.setWxOrderNo(wxOrderNo);
            this.mapper.Update(pay);
            switch (pays.getType())
            {
                case 1:
                    this.patentsMapper.UpdateIdea(pays.getGoodId());
                    break;
                case 2:
                    TasksEntity entity = this.tasksMapper.GetByTaskId(pays.getGoodId());
                    entity.setIs_pay(2);
                    this.tasksMapper.updateByPrimaryKey(entity);
                    break;
            }



        }

    }


    /**
     * 根据用户查询
     *
     * @param userId     用户唯一标识
     * @param orderNo    订单号
     * @param Status     是否支付
     * @param pageNumber
     * @return
     */
    public PageInfo<PaymentEntity> GetByUser(Long userId, String orderNo, String Status, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetByUserId(userId, orderNo, Status));
    }


    /**
     * 管理员查看所有支付记录
     *
     * @param name       支付人名
     * @param orderNo    订单号
     * @param Status     支付状态
     * @param pageNumber
     * @return
     */
    public PageInfo<PaymentEntity> GetByAdmin(String name, String orderNo, String Status, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetByAdmin(orderNo, Status, name));
    }


    /**
     * 查询
     *
     * @param id   商品ID
     * @param type 商品类型
     * @return
     */
    private PaymentEntity Get(Long id, Integer type) {
        return this.mapper.Get(id, type);
    }


    /**
     *  删除订单
     * @param pid 当前订单表的自增主键
     * @return
     */
    public  int Del(Long pid)
    {
        int num=this.mapper.Delete(pid);
        return  num;
    }


}
