package com.goodsogood.ows.service;

import com.goodsogood.ows.configuration.CacheConfiguration;
import com.goodsogood.ows.helper.AesEncryptUtils;
import com.goodsogood.ows.mapper.AccountsUsersRolesMapper;
import com.goodsogood.ows.mapper.MenusMapper;
import com.goodsogood.ows.model.db.AccountsUsersRolesEntity;
import com.goodsogood.ows.model.db.MenusEntity;
import com.goodsogood.ows.model.vo.AuthVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j2

public class MenusService {
    private final MenusMapper mapper;
    private final AccountsUsersRolesMapper accountsUsersRolesMapper;

    @Autowired
    public MenusService(MenusMapper menusMapper, AccountsUsersRolesMapper accountsUsersRolesMapper) {
        this.mapper = menusMapper;
        this.accountsUsersRolesMapper = accountsUsersRolesMapper;
    }

    /**
     * 角色确认后返回 按钮权限
     */
    public List<MenusEntity> GetMenus(Long userid) throws Exception {
        AccountsUsersRolesEntity entitys = this.accountsUsersRolesMapper.GetUserId(userid);
        List<MenusEntity> entity = this.mapper.GetLogin(entitys.getRoleId());
        return entity;
    }


}
