package com.goodsogood.ows.model.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goodsogood.ows.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author xuliduo
 * @date 06/03/2018
 * @description user
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "t_user")
public class UserEntity extends BaseEntity {
    @ApiModelProperty("id")
    @Min(value = 1, message = "{Min.user.id}")
    @NotNull(message = "{NotBlank.user.id}")
    @Column(name = "user_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long userId;

    @ApiModelProperty(value = "用户名")
    @Column(name = "name")
    public String name;

    public String password;

    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "{Pattern.user.phone}")
    @ApiModelProperty(value = "用户手机号")
    public String phone;

    @ApiModelProperty(value = "年龄")
    @Range(min = 3, max = 120, message = "{Range.user.age}")
    public Integer age;

    @ApiModelProperty(value = "性别")
    @Range(max = 1, message = "{Range.user.gender}")
    public Integer gender;

    public String birthday;

    @JsonProperty("last_change_user")
    @Column(name = "last_change_user")
    public Long lastChangeUser;

    @JsonProperty("create_time")
    @Column(name = "create_time")
    public Date createTime;

    @JsonProperty("update_time")
    @Column(name = "update_time")
    public Date updateTime;
}
