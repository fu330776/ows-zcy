package com.goodsogood.ows.model.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author xuliduo
 * @date 2018/9/12
 * @description class UserOtherEntity
 */
@Table(name = "t_user_other")
@Data
public class UserOtherEntity {
    @ApiModelProperty("id")
    @Min(value = 1, message = "{Min.user.id}")
    @NotNull(message = "{NotBlank.user.id}")
    @Column(name = "user_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long userId;

    private String alias;
}
