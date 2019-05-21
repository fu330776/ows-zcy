package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class LoginsForm {
    public  String userId;
    public  String roleId;
}
