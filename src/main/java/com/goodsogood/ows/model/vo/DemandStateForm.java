package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class DemandStateForm {
    @NotNull(message = "需求唯一标识 不可为空")
    public Long demandId;
    public  String state;
}
