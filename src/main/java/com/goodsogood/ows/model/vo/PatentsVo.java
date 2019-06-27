package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
@ApiModel
public class PatentsVo {
    public Long patentId;

    @ApiModelProperty(value = "标题")
    @Column(name = "patent_title")
    public String patentTitle;

    @ApiModelProperty(value = "内容")
    @Column(name = "patent_content")
    public String patentContent;

    @ApiModelProperty(value = "用户")
    @Column(name = "user_id")
    public Long userId;

    @ApiModelProperty(value = "类型：1、申请专利 2、IDEA确权 3、我的专利")
    @Column(name = "patent_type")
    public Integer patentType;

    @ApiModelProperty(value = "IDEA确权收款金额（其他类型不收款）")
    @Column(name = "patent_money")
    public float patentMoney;

    @ApiModelProperty(value = "IDEA确权时间戳（后台生成）")
    @Column(name = "patent_stamp")
    public String patentStamp;

    @ApiModelProperty(value = "是否需要支付：1、需要（IDEA确权） 2、不需要")
    @Column(name = "is_need_pay")
    public Integer isNeedPay;

    @ApiModelProperty(value = "是否支付：1、未支付 2、已支付")
    @Column(name = "is_pay")
    public Integer isPay;

    @ApiModelProperty(value = "添加时间")
    public Date addtime;

    @ApiModelProperty(value = "支付时间")
    public Date pay_time;

    public String phone;

    public String userName;

    public  String picture;
    public  String state;


}
