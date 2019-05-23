package com.goodsogood.ows.model.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel
public class AdminForm {

    public String phone;
    public String pwd;
}
