package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.RoleUserMapper;
import com.goodsogood.ows.mapper.SmssMapper;
import com.goodsogood.ows.model.db.RoleUserEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2

public class LoginService {

    private final RoleUserMapper mapper;
    private  final SmssMapper smssMapper;
    @Autowired
    public LoginService(RoleUserMapper roleUserMapper,SmssMapper sMapper) {
        this.mapper=roleUserMapper;
        this.smssMapper=sMapper;
    }

    public List<RoleUserEntity> getList(String phone,String password)
    {
        return this.mapper.GetList(phone,password);
    }
    public  List<RoleUserEntity> getListPhone(String phone,String smscode)
    {
       String sms_code=  this.smssMapper.GetSmsCode(phone,new Date());
       if(sms_code==smscode && !sms_code.isEmpty())
       {
           return  this.mapper.GetLists(phone);
       }
        return  null;

    }


}
