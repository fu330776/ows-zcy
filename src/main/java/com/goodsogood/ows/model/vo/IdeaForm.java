package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class IdeaForm {

    public String title;
    public String content;
    public Long userId;
    public float money;
    public Integer isPay;
}
