package com.goodsogood.ows.model.db;

import com.goodsogood.ows.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "zcy_role_user")
public class RoleUserEntity extends  BaseEntity  {
    public Long userId;
    public Long roleId;
    public Long roleName;

}
