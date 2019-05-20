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
@Table(name = "zcy_accounts_users_roles")
public class AccountsUsersRolesEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_accounts_users_roles.aur_id}")
    @NotNull(message = "{NotBlank.zcy_accounts_users_roles.aur_id}")
    @Column(name = "aur_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long AurId;

    @ApiModelProperty(value = "账号ID")
    @Column(name = "account_id")
    public  Long AccountId;

    @ApiModelProperty(value = "用户ID")
    @Column(name = "user_id")
    public  Long UserId;

    @ApiModelProperty(value = "角色ID")
    @Column(name = "role_id")
    public  Long RoleId;

//    @ApiModelProperty(value = "角色名")
//    @Column(name = "role_name")
//    public  String roleName;
}
