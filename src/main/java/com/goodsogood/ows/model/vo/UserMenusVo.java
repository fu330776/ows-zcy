package com.goodsogood.ows.model.vo;

import com.goodsogood.ows.model.db.MenusEntity;
import com.goodsogood.ows.model.db.MenusResult;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class UserMenusVo {
    public List<MenusResult> entity;
    public String token;
    public Date time;
    public Long userId;
}
