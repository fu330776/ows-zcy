package com.goodsogood.ows.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.mapper.*;
import com.goodsogood.ows.model.db.*;
import com.goodsogood.ows.model.vo.TaskListForm;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class TasksService {
    private TasksMapper mapper;
    private TasksSchedulesMapper TsMapper;
    private TasksOrdersMapper ToMapper;
    private TaskOrderMapper taskOrderMapper;
    private WithdrawsMapper withdrawsMapper;
    private UsersMapper usersMapper;

    public TasksService(TasksMapper tasksMapper, TasksSchedulesMapper tasksSchedulesMapper
            , TasksOrdersMapper tasksOrdersMapper, TaskOrderMapper taskOrderMapper, WithdrawsMapper withdrawsMapper,
                        UsersMapper usersMapper) {
        this.mapper = tasksMapper;
        this.TsMapper = tasksSchedulesMapper;
        this.ToMapper = tasksOrdersMapper;
        this.taskOrderMapper = taskOrderMapper;
        this.withdrawsMapper = withdrawsMapper;
        this.usersMapper = usersMapper;
    }


    /***
     *  委托 或  任务书添加
     * @param entity
     * @return
     */
    public Boolean Insert(TasksEntity entity) {
        return this.mapper.Insert(entity) > 0;
    }

    /**
     * 查询 任务 或委托
     *
     * @param isPay
     * @param type
     * @return
     */
    public PageInfo<TasksEntity> getAll(Integer isPay, Integer type, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetByAll(isPay, type));
    }

    /**
     * 根据userId 查询
     *
     * @param userId
     * @param isPay
     * @return
     */
    public PageInfo<TasksEntity> getByUser(Long userId, Integer isPay, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetByUserIdAll(isPay, userId));

    }

    /**
     * 管理员 根据类型查询
     *
     * @param type 1:任务书 2：委托书
     * @return
     */
    public PageInfo<TaskListForm> getByType(Integer type,PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>( this.mapper.GetAll(type));
    }

    /**
     * 根据实体修改
     *
     * @param tasksEntity
     * @return
     */
    public Boolean putByTask(TasksEntity tasksEntity) {

        return this.mapper.updateByPrimaryKey(tasksEntity) > 0;
    }

    /**
     * 管理员 修改委托书
     *
     * @param tasksEntity
     * @return
     */
    public Boolean putByAdmin(TasksEntity tasksEntity) {
        return this.mapper.Update(tasksEntity) > 0;
    }

    /**
     * 修改任务进度
     *
     * @param taskId             任务或委托唯一标识
     * @param taskCompletionDays 完成天数
     * @param taskCompletedDays  已完成天数
     * @return
     */
    public Boolean putByTaskId(Long taskId, Integer taskCompletionDays, Integer taskCompletedDays) {
        return this.mapper.PutTaskUpdate(taskId, taskCompletionDays, taskCompletedDays) > 0;
    }

    /**
     * 任务或 委托完成
     *
     * @param taskId
     * @return
     */
    @Transactional
    public Boolean putIsFullfill(Long taskId) {
        Boolean bool = false;
        TasksEntity entity = this.mapper.GetByTaskId(taskId);
        entity.setIs_fulfill(2); //完成

        bool = this.mapper.updateByPrimaryKey(entity) > 0;
        return bool;
    }

    /**
     * 任务（委托） 管理员确认 支付（付款）
     *
     * @param taskId
     * @return
     */
    public Boolean putIsPay(Long taskId) {
        Boolean bool = false;
        TasksEntity entity = this.mapper.GetByTaskId(taskId);
        entity.setIs_pay(2);
        bool = this.mapper.updateByPrimaryKey(entity) > 0;
        return bool;
    }


    /**
     * 领取任务奖励
     *
     * @param taskId
     * @return
     */
    @Transactional
    public Boolean PutReceive(Long taskId) {
        Boolean bool = false;
        TasksEntity entity = this.mapper.GetByTaskId(taskId);
        if (entity.getTaskType() == 1) {
            TasksOrdersEntity orderEntity = new TasksOrdersEntity();
            orderEntity.setAddtime(new Date());
            orderEntity.setTaskId(entity.getTaskId());
            orderEntity.setTaskMoney(entity.getTaskMoney());
            orderEntity.setUserId(entity.getUserId());
            bool = this.ToMapper.insert(orderEntity) > 0;
            WithdrawsEntity withdrawsEntity = new WithdrawsEntity();
            withdrawsEntity.setAddtime(new Date());
            withdrawsEntity.setIsWithdraw(1);
            withdrawsEntity.setUserId(entity.getUserId());
            withdrawsEntity.setWithdrawMoney(entity.getTaskMoney());
            withdrawsEntity.setWithdrawNumber(new Date().toString());
            withdrawsEntity.setPaytime(null);
            bool = this.withdrawsMapper.Insert(withdrawsEntity) > 0;
        }
        return bool;
    }


//    /**
//     * 添加任务，并制定周期与人
//     */
//    @Transactional
//    public Boolean Insert(TasksEntity entity, List<TasksSchedulesEntity> entitys) {
//        int num = this.mapper.Insert(entity);
//        if (num <= 0) {
//            return false;
//        }
//        for (int i = 0; i < entitys.size(); i++) {
//            num = this.TsMapper.Insert(entitys.get(i));
//            if (num <= 0) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 查询任务书（管理员查询）
//     */
//    public List<TasksEntity> GetByAll(Integer ispay) {
//        return this.mapper.GetByAll(ispay);
//    }
//
//    /**
//     * 查询任务书（用户查询）
//     */
//    public List<TasksEntity> GetByUserID(Integer ispay, Long userId) {
//        return this.mapper.GetByUserIdAll(ispay, userId);
//    }
//
//    /**
//     * 查询指定任务周期
//     */
//    public List<TasksSchedulesEntity> GetSchedule(Long taskId) {
//        return this.TsMapper.Get(taskId);
//    }
//
//    /**
//     * 修改进度
//     */
//    public Boolean UpdateTask(String TaskId, float schedule) {
//        TasksEntity tasksEntity = this.mapper.selectByPrimaryKey(TaskId);
//        tasksEntity.setSchedule(schedule);
//        int num = this.mapper.updateByPrimaryKey(tasksEntity);
//        if (num <= 0) {
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 任务完成，添加记录
//     */
//    @Transactional
//    public Integer MissionOk(Long TaskId) {
//        Integer result;
//        TasksEntity entity = this.mapper.selectByPrimaryKey(TaskId);
//        if (entity.getSchedule() < 100) {
//            return 0;
//        }
//        entity.setIs_fulfill(2);
//        entity.setIs_pay(1);
//        result = this.mapper.updateByPrimaryKey(entity);
//        TasksOrdersEntity ordersEntity = new TasksOrdersEntity();
//        ordersEntity.setAddtime(new Date());
//        ordersEntity.setTaskId(entity.getTaskId());
//        ordersEntity.setTaskMoney(entity.getTaskMoney());
//        ordersEntity.setUserId(entity.getUserId());
//        result = this.ToMapper.Insert(ordersEntity);
//        return result;
//    }
//
//    /**
//     * 修改任务
//     */
//    @Transactional
//    public Integer TastAlter(TasksEntity entity, List<TasksSchedulesEntity> entitys) {
//        Integer result;
//        result = this.mapper.updateByPrimaryKey(entity);
//        result = this.TsMapper.Delete(entity.getTaskId());
//        result = this.TsMapper.insertList(entitys);
//        return result;
//    }
//
//    /**
//     * 根据任务书获得科研经费
//     */
//    public List<TaskOrderEntity> GetFunds(Long userId) {
//        return this.taskOrderMapper.Get(userId);
//    }
//
//    /**
//     * 收款 修改任务书状态 并 写入 资金流水
//     */
//    @Transactional
//    public Boolean TaskMoneyReceive(Long TaskId) {
//        int result;
//        //查询任务书情况
//        TasksEntity entity = this.mapper.selectByPrimaryKey(TaskId);
//        //修改任务 是否付款状态
//        entity.setIs_pay(2);
//        result = this.mapper.updateByPrimaryKey(entity);
//
//        //添加资金流水
//        WithdrawsEntity withdrawsEntity = new WithdrawsEntity();
//        withdrawsEntity.setAddtime(new Date());
//        withdrawsEntity.setIsWithdraw(1);
//        withdrawsEntity.setAddtime(new Date());
//        withdrawsEntity.setUserId(entity.getUserId());
//        withdrawsEntity.setWithdrawMoney(entity.getTaskMoney());
//        withdrawsEntity.setWithdrawNumber(new Date().toString()); //流水号
//
//        result = this.withdrawsMapper.Insert(withdrawsEntity);
//
//        if (result > 0) {
//            return true;
//        }
//        return false;
//    }
}
