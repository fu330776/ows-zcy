package com.goodsogood.ows.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.goodsogood.ows.mapper.UserMapper;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.db.UserEntity;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

/**
 * @author xuliduo
 * @date 16/03/2018
 * @description class 用户操作服务
 */
@Service
@Log4j2
public class UserService {

    private UserMapper mapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.mapper = userMapper;
    }

    /**
     * 获得所有的对象
     *
     * @param pageNumber 分页信息封装对象 可以为null，为null不分页
     * @return list
     */
    public Page<UserEntity> getAll(PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        // PageHelper.startPage(p, r);
        return PageHelper.startPage(p, r).doSelectPage(mapper::selectAll);
    }

    /**
     * 插入方法
     *
     * @param user 需要插入的对象
     * @return 包含id的user
     */
    public UserEntity insert(UserEntity user) {
        this.mapper.insert(user);
        return user;
    }

    /**
     * 通过id查询
     *
     * @param id userId
     * @return user or null
     */
    @Nullable
    public UserEntity getOne(long id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    /**
     * 通过主键更新数据
     *
     * @param userEntity 需要更新的对象
     * @return 发生变化的列
     */
    public int update(UserEntity userEntity) {
        // 使用 updateByPrimaryKeySelective 不会更新null
        // 如果要强制更新 null ，那么使用updateByPrimaryKey
        // tk example 对象
        // Example
        return this.mapper.updateByPrimaryKeySelective(userEntity);
    }

    @Async("myExecutor")
    public void print(int i) {
        log.debug("i-->{}", i);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
