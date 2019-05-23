package com.goodsogood.ows.model.db;

import com.goodsogood.ows.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@Table(name = "zcy_money")
public class MoneyEntity extends BaseEntity {
    @ApiModelProperty(value = "id")
    @Min(value = 1, message = "{Min.zcy_money.money_id}")
    @NotNull(message = "{NotBlank.zcy_money.money_id}")
    @Column(name = "money_id")
    @GeneratedValue(generator = "JDBC")
    @Id
    public Long moneyId;

    @ApiModelProperty(value = "付费类型")
    @Column(name = "money_name")
    public String name;

    @ApiModelProperty(value = "金额")
    @Column(name = "money_money")
    public BigDecimal money;

    @ApiModelProperty(value = "是否生效")
    @Column(name = "money_del")
    public Integer del;

    @ApiModelProperty(value = "添加时间")
    @Column(name = "money_time")
    public Date time;


}
