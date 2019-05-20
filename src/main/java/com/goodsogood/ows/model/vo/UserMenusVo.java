package com.goodsogood.ows.model.vo;

import com.goodsogood.ows.model.db.MenusEntity;

import java.util.Date;
import java.util.List;

public class UserMenusVo {
    public List<MenusEntity> entity;
    public String token;
    public Date time;
    public Long  userId;
}
