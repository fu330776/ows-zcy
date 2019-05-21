package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class ProgressBarForm {

    public String TaskId;
    public float schedule;
}
