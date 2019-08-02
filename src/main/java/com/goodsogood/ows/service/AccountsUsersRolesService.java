package com.goodsogood.ows.service;

import com.goodsogood.ows.mapper.AccountsMapper;
import com.goodsogood.ows.mapper.AccountsUsersRolesMapper;
import com.goodsogood.ows.model.db.AccountsEntity;
import com.goodsogood.ows.model.db.AccountsUsersRolesEntity;
import com.goodsogood.ows.model.vo.AccountUsersVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class AccountsUsersRolesService {
    private AccountsUsersRolesMapper mapper;
    private AccountsMapper amapper;


    @Autowired
    public AccountsUsersRolesService(AccountsUsersRolesMapper accountsUsersRolesMapper, AccountsMapper accountsMapper) {
        this.amapper = accountsMapper;
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
    public AccountsUsersRolesEntity Get(String phone, Long roleid) {
        AccountsEntity entity = this.amapper.GetByPhone(phone);
        if (entity == null) {
            return null;
        }
        return this.mapper.Get(entity.getAccountId(), roleid);
    }

    /**
     * 管理员 验证
     *
     * @param phone 手机号
     * @param pwd   md5密码
     * @return
     */
    public Long GetByFind(String phone, String pwd) {
        return this.mapper.GetAdminFind(phone, pwd);
    }


    /**
     * 查询需统计数据
     *
     * @return
     */
    public List<AccountUsersVo> GetCount() {
        return this.mapper.GetCount();

    }
}
