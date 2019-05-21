package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
@Data
@ApiModel
public class UserCloseForm {

    @Min(value = 1,message = "超出界限")
    public Long userid;
    @Min(value = 1,message = "超出界限")
    @Max(value = 2,message = "超出界限最大值")
    public Integer isdel;
}
