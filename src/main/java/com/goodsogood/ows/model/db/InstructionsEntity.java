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
@Table(name = "zcy_Instructions")
public class InstructionsEntity  extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_Instructions.id}")
    @NotNull(message = "{NotBlank.zcy_Instructions.id}")
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Integer id;
    @ApiModelProperty(value = "内容")
    @Column(name = "Instructions")
    public  String Instructions;
}
