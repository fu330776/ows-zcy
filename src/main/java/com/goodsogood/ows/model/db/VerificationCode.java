package com.goodsogood.ows.model.db;

import lombok.Data;

@Data
public class VerificationCode {
    public Integer code;
    public Long timestamp;
    public String message;
    public Object data;
    public Integer status;
    public String time_string;
}
