package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UsersForm {

    @ApiModelProperty(value = "电话")
    public String phone;
    @ApiModelProperty(value = "密码")
    public String password;
    @ApiModelProperty(value = "个人姓名")
    public String userName;
    @ApiModelProperty(value = "个人所属医院")
    public String userHospital;
    @ApiModelProperty(value = "个人所属科室")
    public String userDepartment;
    @ApiModelProperty(value = "个人职位：1、医生 2、护士")
    public String userPosition;
    @ApiModelProperty(value = "个人邮箱")
    public String userEmail;

    @ApiModelProperty(value = "个人银行卡号")
    public String userBankCardNumber;

    @ApiModelProperty(value = "个人持卡人姓名")
    public String userCardholderName;

    @ApiModelProperty(value = "个人持卡人电话")
    public String userCardholderPhone;

    @ApiModelProperty(value = "个人持卡人身份证")
    public String userCardholderIdCard;

    @ApiModelProperty(value = "企业名称")
    public String companyName;

    @ApiModelProperty(value = "企业代码")
    public String companyCode;

    @ApiModelProperty(value = "机构名称")
    public String organizationName;

    @ApiModelProperty(value = "机构代码")
    public String organizationCode;
    @ApiModelProperty(value = "是否审核 1、未审核 2、已审核")
    public Integer review;
    @ApiModelProperty(value = "是否起禁用 1、启用  2、禁用")
    public Integer enable;
    @ApiModelProperty(value = "推荐码（自动生成4~6位唯一，字母+数字混合）")
    public String code;
    @ApiModelProperty(value = "推荐人（对应其他用户的推荐码）")
    public String referrer;
    @ApiModelProperty(value = "是否被管理员指定为推荐人 1、不是 2、是")
    public Integer isReferrer;
    public Long roleId;

    @ApiModelProperty(value = "角色名")
    public String roleName;

    @ApiModelProperty(value = "验证码")
    public String phoneCode;
    @ApiModelProperty(value = "是否子管理 是:1 否:2")
    public Integer Issub;
    @ApiModelProperty(value = "省")
    public  String provinces;
    @ApiModelProperty(value = "市")
    public  String municipalities;
    @ApiModelProperty(value = "区")
    public  String districts;
    @ApiModelProperty(value = "等级")
    public  String grade;
    @ApiModelProperty(value = "性质")
    public  String nature;
    @ApiModelProperty(value = "职称")
    public String title;

    @ApiModelProperty(value = "联系人")
    public  String contacts;
    @ApiModelProperty(value = "联系电话")
    public String contact_phone;
    @ApiModelProperty(value = "医疗机构详细地址")
    public  String detailed_address;
    @ApiModelProperty(value = "企业会员营业执照")
    public  String business_license;

}
