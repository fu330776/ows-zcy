package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.DemandsMapper;
import com.goodsogood.ows.model.db.DemandsEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
/**
 * 需求表Service
 * */
public class DemandsService {

    private DemandsMapper mapper;

    @Autowired
    public DemandsService(DemandsMapper demandsMapper) {
        this.mapper = demandsMapper;
    }

    /**
     * 根据用户，类型 ，是否联系，查询
     */
    public List<DemandsEntity> Get(Long userid, Integer type, Integer isContact) {
        return this.mapper.Get(userid, type, isContact);
    }

    /**
     * 后台管理员查看 根据类型查看
     */
    public List<DemandsEntity> GetTypeAll(Integer type) {
        return this.mapper.GetTypeAll(type);
    }

    /**
     *  查询单个数据
     * */
    public  DemandsEntity GetByOne(Long demandId){
        return  this.mapper.selectByPrimaryKey(demandId);
    }

    /**
     * 根据类型不同添加需求表数据
     */
    public Boolean Insert(DemandsEntity demandsEntity) {
        int num = this.mapper.Insert(demandsEntity);
        return IsBool(num);
    }

    /**
     * 根据需求ID  修改是否联系
     */
    public Boolean UpdateContact(Long aid, Integer isContact) {
        int num = this.mapper.UpdateContact(aid, isContact);
        return IsBool(num);
    }

    /**
     * 修改基本信息 ，如：名称，内容
     */
    public Boolean UpdateBasic(Long aid, String name, String content) {
        int num = this.mapper.UpdateContent(aid, name, content);
        return IsBool(num);
    }

    /**
     * 判断 是否执行成功 成功返回true
     */
    private Boolean IsBool(Integer num) {
        if (num <= 0) {
            return false;
        }
        return true;
    }

}
