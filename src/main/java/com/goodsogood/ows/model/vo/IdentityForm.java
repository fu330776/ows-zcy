package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class IdentityForm {
    public  String phone;
    public  String roleId;
}
