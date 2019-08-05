package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel
@Data
public class ApplicationForCashForm {
    public Long userId;
    public double money;
}

