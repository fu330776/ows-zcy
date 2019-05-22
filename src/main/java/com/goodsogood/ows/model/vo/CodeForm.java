package com.goodsogood.ows.model.vo;

import com.goodsogood.ows.validator.constraints.NotBlank4Channel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@ApiModel
public class CodeForm {
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "{Pattern.admin.mobile}")
    @NotBlank4Channel(message = "{NotBlank.code.mobile}", include = "BANK")
    @ApiModelProperty(value = "手机号", required = true)
    public String mobile;

    @ApiModelProperty(value = "发送验证码类型 1：注册，2：修改密码")
    public Integer Type;

}
