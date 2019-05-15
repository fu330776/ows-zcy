package com.goodsogood.ows.model.vo;

import com.goodsogood.ows.validator.constraints.CheckUserName;
import com.goodsogood.ows.validator.constraints.NotBlank4Channel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;

/**
 * @author xuliduo
 * @date 15/03/2018
 * @description class UserForm
 */
@Data
@ApiModel
public class UserForm {
    @NotBlank4Channel(message = "{NotBlank.user.name}", exclude = {"BANK", "EWECHAT"})
    @CheckUserName(message = "{UserName.not.start}", start = "张")
    @ApiModelProperty(value = "用户名")
    private String name;

    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "{Pattern.user.phone}")
    @NotBlank4Channel(message = "{NotBlank.user.phone}", include = "BANK")
    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "性别")
    @Range(max = 1, message = "{Range.user.gender}")
    private int gender;

    @ApiModelProperty(value = "年龄")
    @Range(min = 3, max = 120, message = "{Range.user.age}")
    private int age;
}
