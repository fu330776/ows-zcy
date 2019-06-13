package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class CreateOrderForm {
    public  Long gid;
    public  int type;
}
