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

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "zcy_user_menu")
public class UserMenuEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_user_menu.user_id}")
    @NotNull(message = "{NotBlank.zcy_user_menu.user_id}")
    @Column(name = "zcy_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long zcyId;
    @ApiModelProperty(value = "账号用户ID")
    @Column(name = "zcy_userId")
    public Long zcyUserId;
    @ApiModelProperty(value = "按钮ID")
    @Column(name = "zcy_menuId")
    public Long zcyMenuId;
}
