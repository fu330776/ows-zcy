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
@Table(name = "zcy_roles_menus")
public class RolesMenusEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_roles_menus.rm_id}")
    @NotNull(message = "{NotBlank.zcy_roles_menus.rm_id}")
    @Column(name = "rm_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public  Long rmId;

    @ApiModelProperty(value = "角色ID")
    @Column(name = "role_id")
    public  Long roleId;

    @ApiModelProperty(value = "菜单ID")
    @Column(name = "menu_id")
    public  Long menuId;

}
