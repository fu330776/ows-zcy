package com.goodsogood.ows.model.db;

import com.goodsogood.ows.model.BaseEntity;
import io.swagger.annotations.Api;
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
@Table(name = "zcy_menus")
public class MenusEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_menus.menu_id}")
    @NotNull(message = "{NotBlank.zcy_menus.menu_id}")
    @Column(name = "menu_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long menuId;

    @ApiModelProperty(value = "菜单名称")
    @Column(name = "menu_name")
    public String menuName;

    @ApiModelProperty(value = "菜单跳转地址")
    @Column(name = "menu_url")
    public String menuUrl;

    @ApiModelProperty(value = "菜单图标")
    @Column(name = "menu_icon")
    public String menuIcon;

    @ApiModelProperty(value = "菜单大小")
    @Column(name = "menu_size")
    public Integer menuSize;

    @ApiModelProperty(value = "父级菜单ID")
    @Column(name = "parent_id")
    public Long parentId;
}
