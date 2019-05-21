package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
@Data
@ApiModel
public class UserMoneyVo {
    public BigDecimal noMoney;
    public BigDecimal tooMoney;
}
