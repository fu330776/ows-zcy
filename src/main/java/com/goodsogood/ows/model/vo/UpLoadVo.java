package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class UpLoadVo {
    public Integer code;
    public Boolean success;
    public String msg;
    public String data;
}
