package com.goodsogood.ows.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.mapper.PatentsMapper;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.db.PatentsEntity;
import com.goodsogood.ows.model.vo.LoginResult;
import com.goodsogood.ows.model.vo.PatentsVo;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PatentsService {
    private PatentsMapper mapper;

    public PatentsService(PatentsMapper patentsMapper) {
        this.mapper = patentsMapper;
    }

    /**
     * 申请专利
     */
    public Boolean Insert(PatentsEntity patentsEntity) {
        Integer num = this.mapper.Insert(patentsEntity);
        if (num == null || num == 0) {
            return false;
        }
        return true;
    }
    public PatentsEntity InsertIdea(PatentsEntity patentsEntity) {
        Integer num = this.mapper.Insert(patentsEntity);
        if (num == null || num == 0) {
            return null;
        }
        return patentsEntity;
    }
    /**
     *  修改状态
     * @param id
     * @param state
     * @return
     */
    public  Boolean UpdateState(Long id,String state)
    {
        int num=this.mapper.UpdateState(id,state);
        if(num<=0)
        {
            return  false;
        }
        return  true;
    }

    /**
     * 分页查询用户（根据类型查询）
     *
     * @param userId
     * @param type
     * @param pageNumber
     * @return
     */
    public PageInfo<PatentsVo> Get(Long userId, Integer type, String title, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.Get(userId, type, title));
    }

    /**
     * 分页查询管理员（根据类型查询）
     *
     * @param type
     * @param pageNumber
     * @return
     */
    public PageInfo<PatentsVo> GetAll(Integer type, String title, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetByType(type, title));
    }


    /**
     *  分页查询idea 委托
     * @param type
     * @param isPay
     * @param title
     * @param pageNumber
     * @return
     */
    public PageInfo<PatentsVo> GetIdeaALL(Integer type, Integer isPay, String title, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetIdea(type, isPay, title));
    }


    /**
     * 根据唯一标识查询
     *
     * @param pid
     * @return
     */
    public PatentsVo GetFind(Long pid) {
        return this.mapper.GetById(pid);
    }
    /**
     *  管理员 删除
     * @param patentId
     * @return
     */
    public LoginResult Del(Long patentId)
    {
        LoginResult result =new LoginResult();
        result.setMsg("删除失败");
        result.setIsb(false);
        int num=this.mapper.CustomDelete(patentId);
        boolean isb=num > 0 ? true:false;
        if(isb){
            result.setIsb(isb);
            result.setMsg("删除成功");
        }
        return  result;

    }

}
