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
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "zcy_data")
public class DataEntity extends BaseEntity {

    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_data.id}")
    @NotNull(message = "{NotBlank.zcy_data.id}")
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public  Long id;

    @ApiModelProperty(value = "统计名称")
    @Column(name = "type_name")
    public String typeName;

    @ApiModelProperty(value = "类型分类")
    @Column(name = "type_type")
    public Integer typeType;

    @ApiModelProperty(value = "总合计")
    @Column(name = "total")
    public  Long total;

    @ApiModelProperty(value = "当天合计")
    @Column(name = "day_count")
    public Integer dayCount;

    @ApiModelProperty(value = "当前时间")
    @Column(name = "current_time")
    public Date currentTime;

    @ApiModelProperty(value = "开始时间")
    @Column(name = "add_time")
    public  Date addTime;
}
