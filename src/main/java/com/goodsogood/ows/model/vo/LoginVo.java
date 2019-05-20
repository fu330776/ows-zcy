package com.goodsogood.ows.model.vo;

import com.goodsogood.ows.validator.constraints.NotBlank4Channel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;


/**
 * @author xuliduo
 * @date 06/03/2018
 * @description class LoginVo
 */
@Data
@ApiModel
public class LoginVo {
    /**
     * 手机号
     */
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "{Pattern.admin.mobile}")
    @NotBlank4Channel(message = "{NotBlank.login.mobile}", include = "BANK")
    @ApiModelProperty(value = "手机号", required = true)
    public String mobile;
    /**
     * 短信验证码
     */
//    @NotEmpty
    @ApiModelProperty(value = "短信验证码", required = true)
    public String code;
    /**
     * 30天免登陆
     */
    @ApiModelProperty(value = "30天免登陆")
    public Boolean freeLogin;
}
