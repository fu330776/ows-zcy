package com.goodsogood.ows.model.db;

import com.goodsogood.ows.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "zcy_demands")
/**
 * 需求表
 * */
public class DemandsEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_demands.demand_id}")
    @NotNull(message = "{NotBlank.zcy_demands.demand_id}")
    @Column(name = "demand_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public  Long demandId;

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

    @ApiModelProperty(value = "添加时间")
    @Column(name = "addtime")
    public Date addtime;

    @ApiModelProperty(value = "图片")
    public  String picture;
    @ApiModelProperty(value = "状态：递交失败、递交成功、已受理、未受理、受理失败")
    public  String state;
}
