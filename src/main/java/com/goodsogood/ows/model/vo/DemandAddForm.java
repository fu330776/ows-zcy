package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
@ApiModel
public class DemandAddForm {
    @ApiModelProperty(value = "需求类型：1、个人医护创新 2、个人医护帮我找 3、个人医护帮我做 4、企业提出需求 5、企业项目评价 6、企业成果转化 7、机构交易 8、机构合作 9、机构项目评价")
    @Column(name = "demand_type")
    public  Integer demandType;

    @ApiModelProperty(value = "需求名称")
    @Column(name = "demand_name")
    public String demandName;

    @ApiModelProperty(value = "需求描述")
    @Column(name = "demand_content")
    public  String demandContent;

    @ApiModelProperty(value = "是否联系：1、已联系 2、未联系")
    @Column(name = "is_contact")
    public  Integer isContact;

    @ApiModelProperty(value = "用户ID")
    @Column(name = "user_id")
    public  Long userId;

    public  String picture;
}
