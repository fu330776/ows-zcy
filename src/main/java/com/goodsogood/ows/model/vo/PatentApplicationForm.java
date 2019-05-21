package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 专利申请
 */
@Data
@ApiModel
public class PatentApplicationForm {
    public Long userid;
    public String title;
    public String content;
}
