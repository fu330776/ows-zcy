package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserRoleForm {
    public  String name;
    @ApiModelProperty(value = "省")
    public  String provinces;
@ApiModelProperty(value = "市")
        public  String municipalities;
@ApiModelProperty(value = "区")
        public  String districts;
    @ApiModelProperty(value = "等级")
            public  String grade;
    @ApiModelProperty(value = "性质")
    public  String nature;
    @ApiModelProperty(value = "关键字搜索")
    public  String keyword;


}
