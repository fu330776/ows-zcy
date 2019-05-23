package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.AccountsUsersRolesMapper;
import com.goodsogood.ows.model.db.AccountsUsersRolesEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AccountsUsersRolesService {
    private AccountsUsersRolesMapper mapper;

    public AccountsUsersRolesService(AccountsUsersRolesMapper accountsUsersRolesMapper) {
        this.mapper = accountsUsersRolesMapper;
    }

    /**
     * 人对应相应角色
     */
    public Long Insert(AccountsUsersRolesEntity accountsUsersRolesEntity) {
        return this.mapper.RewriteInsert(accountsUsersRolesEntity);
    }

    /**
     * 根据账号和角色查询
     **/
    public AccountsUsersRolesEntity Get(String phone, String roleid) {
        return this.mapper.Get(phone, roleid);
    }

    /**
     *  管理员 验证
     * @param phone 手机号
     * @param pwd  md5密码
     * @return
     */
    public Long GetByFind(String phone, String pwd) {
        return this.mapper.GetAdminFind(phone, pwd);
    }
}
