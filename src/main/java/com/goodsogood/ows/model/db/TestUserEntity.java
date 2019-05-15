package com.goodsogood.ows.model.db;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "test_user")
public class TestUserEntity {
    @Id
    @Column(name = "test_user_id")
    private Long testUserId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String userName;

}