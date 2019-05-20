package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.*;
import com.goodsogood.ows.model.db.*;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Param;
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

    public TasksService(TasksMapper tasksMapper, TasksSchedulesMapper tasksSchedulesMapper
            , TasksOrdersMapper tasksOrdersMapper, TaskOrderMapper taskOrderMapper,WithdrawsMapper withdrawsMapper) {
        this.mapper = tasksMapper;
        this.TsMapper = tasksSchedulesMapper;
        this.ToMapper = tasksOrdersMapper;
        this.taskOrderMapper = taskOrderMapper;
        this.withdrawsMapper=withdrawsMapper;
    }

    /**
     * 添加任务，并制定周期与人
     */
    @Transactional
    public Boolean Insert(TasksEntity entity, List<TasksSchedulesEntity> entitys) {
        int num = this.mapper.Insert(entity);
        if (num <= 0) {
            return false;
        }
        for (int i = 0; i < entitys.size(); i++) {
            num = this.TsMapper.Insert(entitys.get(i));
            if (num <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查询任务书（管理员查询）
     */
    public List<TasksEntity> GetByAll(Integer ispay) {
        return this.mapper.GetByAll(ispay);
    }

    /**
     * 查询任务书（用户查询）
     */
    public List<TasksEntity> GetByUserID(Integer ispay, Long userId) {
        return this.mapper.GetByUserIdAll(ispay, userId);
    }

    /**
     * 查询指定任务周期
     */
    public List<TasksSchedulesEntity> GetSchedule(Long taskId) {
        return this.TsMapper.Get(taskId);
    }

    /**
     * 修改进度
     */
    public Boolean UpdateTask(String TaskId, float schedule) {
        TasksEntity tasksEntity = this.mapper.selectByPrimaryKey(TaskId);
        tasksEntity.setSchedule(schedule);
        int num = this.mapper.updateByPrimaryKey(tasksEntity);
        if (num <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 任务完成，添加记录
     */
    @Transactional
    public Integer MissionOk(Long TaskId) {
        Integer result;
        TasksEntity entity = this.mapper.selectByPrimaryKey(TaskId);
        if (entity.getSchedule() < 100) {
            return 0;
        }
        entity.setIs_fulfill(2);
        entity.setIs_pay(1);
        result = this.mapper.updateByPrimaryKey(entity);
        TasksOrdersEntity ordersEntity = new TasksOrdersEntity();
        ordersEntity.setAddtime(new Date());
        ordersEntity.setTaskId(entity.getTaskId());
        ordersEntity.setTaskMoney(entity.getTaskMoney());
        ordersEntity.setUserId(entity.getUserId());
        result = this.ToMapper.Insert(ordersEntity);
        return result;
    }

    /**
     * 修改任务
     */
    @Transactional
    public Integer TastAlter(TasksEntity entity, List<TasksSchedulesEntity> entitys) {
        Integer result;
        result = this.mapper.updateByPrimaryKey(entity);
        result = this.TsMapper.Delete(entity.getTaskId());
        result = this.TsMapper.insertList(entitys);
        return result;
    }

    /**
     * 根据任务书获得科研经费
     */
    public List<TaskOrderEntity> GetFunds(Long userId) {
        return this.taskOrderMapper.Get(userId);
    }

    /**
     * 收款 修改任务书状态 并 写入 资金流水
     * */
    @Transactional
    public Boolean TaskMoneyReceive(Long TaskId) {
        int result;
        //修改任务 是否付款状态
        TasksEntity entity = this.mapper.selectByPrimaryKey(TaskId);
        entity.setIs_pay(2);
        result = this.mapper.updateByPrimaryKey(entity);

        //添加资金流水
        WithdrawsEntity withdrawsEntity=new WithdrawsEntity();
        withdrawsEntity.setAddtime(new Date());
        withdrawsEntity.setIsWithdraw(1);
        withdrawsEntity.setAddtime(new Date());
        withdrawsEntity.setUserId(entity.getUserId());
        withdrawsEntity.setWithdrawMoney(entity.getTaskMoney());
        withdrawsEntity.setWithdrawNumber(new Date().toString()); //流水号

        result=this.withdrawsMapper.Insert(withdrawsEntity);

        if(result >0)
        {
            return  true;
        }
        return false;
    }
}
