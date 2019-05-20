package com.goodsogood.ows.model.vo;

import javax.validation.constraints.NotNull;

public class DemandsForm {
    @NotNull(message = "需求唯一标识 不可为空")
    public Long demandId;
    public Integer isContact;
    public String demandName;
    public String demandContent;

}
