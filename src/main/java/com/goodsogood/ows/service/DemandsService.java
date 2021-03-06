package com.goodsogood.ows.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.mapper.DataMapper;
import com.goodsogood.ows.mapper.DemandsMapper;
import com.goodsogood.ows.model.db.DemandsEntity;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.DemandsVo;
import com.goodsogood.ows.model.vo.LoginResult;
import com.google.common.base.Preconditions;
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
    private DataMapper dataMapper;
    @Autowired
    public DemandsService(DemandsMapper demandsMapper,DataMapper dataMappers) {
        this.mapper = demandsMapper;
        this.dataMapper=dataMappers;
    }

    /**
     * 根据用户，类型 ，是否联系，查询
     */
    public PageInfo<DemandsVo> Get(Long userId, Integer type, Integer isContact,String name,String twoType,String ThreeType,String rank,PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.Get(userId, type, isContact,name,twoType,ThreeType,rank));
    }

    /**
     * 后台管理员查看 根据类型查看
     */
    public PageInfo<DemandsVo> GetTypeAll(Integer type, Integer isCount, String name,String state,String rank,String twoType,String threeType ,PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetTypeAll(type, isCount,name,state,rank,twoType,threeType));
    }


    /**
     * 查询单个数据
     */
    public DemandsEntity GetByOne(Long demandId) {
        return this.mapper.selectByPrimaryKey(demandId);
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
    public Boolean UpdateBasic(Long aid, String name, String content,String rank) {
        int num = this.mapper.UpdateContent(aid, name, content,rank);
        return IsBool(num);
    }

    /**
     *  修改
     * @param aid 唯一标识
     * @param name  标题
     * @param content 内容
     * @param isContact 是否联系
     * @return
     */
    public  Boolean Update(Long aid, String name, String content,Integer isContact,String picture,String rank)
    {
        int num=this.mapper.Update(aid,name,content,isContact,picture,rank);
        return  IsBool(num);
    }


    /**
     *  递交状态修改
     * @param id
     * @param state
     * @return
     */
    public  Boolean UpdateState(long id,String state)
    {
        int num=this.mapper.UpdateState(id,state);
        return  IsBool(num);
    }

    /**
     *  撤销创新需求
     * @param id
     * @return
     */
    public LoginResult UpdateRevoke(Long id)
    {
        LoginResult result =new LoginResult();
        int num =this.mapper.UpdateRevoke(id);
        if(num >0)
        {
            result.setIsb(true);
            result.setMsg("撤销成功");
          DemandsEntity entity = this.mapper.selectByPrimaryKey(id);
          this.delCount(entity.getDemandType());
            return  result;
        }
        result.setMsg("撤销失败");
        result.setIsb(false);
        return  result;
    }

    /**
     *  减少数量
     * @param DemandType
     */
    private  void delCount(Integer DemandType) {
        Integer type;
        switch (DemandType) {
            case 1: //创新量
                type = 1;
                break;
            case 2:
            case 3:
                type = 2;
                break;
            default:
                type = null;
                break;
        }
        if (type != null) {
            this.dataMapper.delCount(type);
        }
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
