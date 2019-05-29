package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ExamineForm {

    public Long userId;
    @ApiModelProperty(value = "通过为2 失败为 -1")
    public Integer isDel;
}
