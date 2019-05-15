package com.goodsogood.ows.model.db;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "test_user_win")
public class TestUserWinEntity {
    @Id
    @Column(name = "test_user_new_id")
    private Long testUserNewId;

    private Long winner;

    @Column(name = "win_user")
    private String winUser;
}