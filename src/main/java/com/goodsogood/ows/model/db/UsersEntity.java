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
@Table(name = "zcy_users")
public class UsersEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_users.user_id}")
    @NotNull(message = "{NotBlank.zcy_users.user_id}")
    @Column(name = "user_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long userId;

    @ApiModelProperty(value = "个人姓名")
    @Column(name = "user_name")
    public String userName;

    @ApiModelProperty(value = "个人所属医院")
    @Column(name = "user_hospital")
    public String userHospital;

    @ApiModelProperty(value = "个人所属科室")
    @Column(name = "user_department")
    public String userDepartment;

    @ApiModelProperty(value = "个人职位")
    @Column(name = "user_position")
    public String userPosition;

    @ApiModelProperty(value = "个人邮箱")
    @Column(name = "user_email")
    public String userEmail;

    @ApiModelProperty(value = "个人银行卡号")
    @Column(name = "user_bank_card_number")
    public String userBankCardNumber;

    @ApiModelProperty(value = "个人持卡人姓名")
    @Column(name = "user_cardholder_name")
    public String userCardholderName;

    @ApiModelProperty(value = "个人持卡人电话")
    @Column(name = "user_cardholder_phone")
    public String userCardholderPhone;

    @ApiModelProperty(value = "个人持卡人身份证")
    @Column(name = "user_cardholder_idcard")
    public String userCardholderIdcard;

    @ApiModelProperty(value = "企业名称")
    @Column(name = "company_name")
    public String companyName;

    @ApiModelProperty(value = "企业代码")
    @Column(name = "company_code")
    public String companyCode;

    @ApiModelProperty(value = "机构名称")
    @Column(name = "organization_name")
    public String organizationName;

    @ApiModelProperty(value = "机构代码")
    @Column(name = "organization_code")
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
    @Column(name = "is_referrer")
    public Integer isReferrer;

    public Date addtime;

    public Date updatetime;

    public String phone;

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

    public String openid;
    @ApiModelProperty(value = "1：不是医护审核员审核员，2：医护审核员")
    @Column(name = "IsAuditor")
    public Integer isAuditor;
    @ApiModelProperty(value = "0:默认开启 1：关闭")
    @Column(name = "IsTips")
    public  Integer isTips;
}
