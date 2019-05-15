package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * @author xuliduo
 * @date 06/03/2018
 * @description class LoginVo
 */
@Data
@ApiModel
public class LoginVo {
    /**
     * 用户名
     */
    @ApiModelProperty(required = true)
    @NotEmpty
    public String userName;
    /**
     * md5后的密码
     */
    @NotEmpty
    @ApiModelProperty(value = "md5后的密码", required = true)
    public String password;
}
