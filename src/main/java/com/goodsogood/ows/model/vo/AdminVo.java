package com.goodsogood.ows.model.vo;

import com.goodsogood.ows.model.db.MenusEntity;
import com.goodsogood.ows.model.db.MenusResult;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel
@Data
public class AdminVo {
    public String token;
    public Long userId;
    public Date time;
    public List<MenusResult> entity;
}
