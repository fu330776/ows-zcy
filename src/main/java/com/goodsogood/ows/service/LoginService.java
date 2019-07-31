package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.AccountsMapper;
import com.goodsogood.ows.mapper.RoleUserMapper;
import com.goodsogood.ows.mapper.SmssMapper;
import com.goodsogood.ows.model.db.AccountsEntity;
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
    private final SmssMapper smssMapper;
    private final AccountsMapper accountsMapper;

    @Autowired
    public LoginService(RoleUserMapper roleUserMapper, SmssMapper sMapper, AccountsMapper accountsMapper) {
        this.mapper = roleUserMapper;
        this.smssMapper = sMapper;
        this.accountsMapper = accountsMapper;
    }

    public List<RoleUserEntity> getList(String phone, String password) {
        AccountsEntity accountsEntity = this.accountsMapper.GetByUser(phone, password);
        return this.mapper.GetList(accountsEntity.getAccountId());
    }


    public List<RoleUserEntity> getListPhone(String phone, String smscode) {
        String sms_code = this.smssMapper.GetSmsCode(phone, new Date());
        AccountsEntity accountsEntity = this.accountsMapper.GetByPhone(phone);
        if (sms_code == smscode && !sms_code.isEmpty()) {
            return this.mapper.GetLists(accountsEntity.getAccountId());
        }
        return null;
    }


}
