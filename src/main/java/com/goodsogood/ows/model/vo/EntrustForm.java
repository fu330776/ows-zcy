package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class EntrustForm {

    public String taskName;
    public  String taskContent;
    public  Integer taskType;
    public Long userId;
}
