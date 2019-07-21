package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class SmsForm {
    public  String Message;
    public  String  RequestId;
    public  String BizId;
    public  String Code;
}
