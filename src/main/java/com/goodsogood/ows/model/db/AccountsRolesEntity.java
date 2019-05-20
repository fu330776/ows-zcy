package com.goodsogood.ows.model.db;

import javax.persistence.Column;

public class AccountsRolesEntity {

    @Column(name = "user_id")
    public  Long  userid;

    @Column(name = "role_id")
    public  Long  roleid;

    @Column(name = "role_name")
    public  String roleName;

}
