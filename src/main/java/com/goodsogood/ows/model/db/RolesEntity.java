package com.goodsogood.ows.model.db;

import com.goodsogood.ows.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sun.rmi.runtime.Log;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "zcy_roles")
public class RolesEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_roles.role_id}")
    @NotNull(message = "{NotBlank.zcy_roles.role_id}")
    @Column(name = "role_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long roleId;

    @ApiModelProperty(value = "角色名")
    @Column(name = "role_name")
    public  String roleName;
}
