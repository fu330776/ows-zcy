package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.InstructionsMapper;
import com.goodsogood.ows.model.db.InstructionsEntity;
import com.goodsogood.ows.model.vo.LoginResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class InstructionsService {
    private InstructionsMapper mapper;

    @Autowired
    public  InstructionsService(InstructionsMapper mapper){
        this.mapper=mapper;
    }

    /***
     *  添加说明
     * @param entity
     * @return
     */
    public LoginResult Insert(InstructionsEntity entity)
    {
        LoginResult result =new LoginResult();
        result.setIsb(false);
        result.setMsg("添加失败");
        int num= this.mapper.Insert(entity);
        if(num > 0 )
        {
            result.setMsg("添加成功");
            result.setIsb(true);
        }
        return  result;
    }

    /**
     *
     * @param entity
     * @return
     */
    public  LoginResult Update(InstructionsEntity entity)
    {
        LoginResult result =new LoginResult();
        result.setMsg("修改失败");
        result.setIsb(false);
        int num =this.mapper.Update(entity);
        if(num > 0)
        {
            result.setIsb(true);
            result.setMsg("修改成功");
        }
        return  result;
    }

    public  LoginResult Get()
    {
        LoginResult result = new LoginResult();
        result.setMsg("说明不存在，请联系管理员");
        result.setIsb(false);
        InstructionsEntity entity =this.mapper.Get();
        if(entity != null)
        {
            result.setIsb(true);
            result.setMsg(entity.getInstructions());
        }
        return  result;
    }


}
