package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel
@Data
public class AccountUsersVo {
    public int count;
    public int oneCount;
    public int twoCount;
    public int threeCount;
}
