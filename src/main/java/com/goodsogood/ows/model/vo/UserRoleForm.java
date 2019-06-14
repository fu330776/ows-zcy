package com.goodsogood.ows.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

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
    public  Long roleId;
    @ApiModelProperty(value = "是否审核 1、未审核 2、已审核")
    public Integer review;
    @ApiModelProperty(value = "是否起禁用 1、启用  2、禁用")
    public  Integer enable;


}
