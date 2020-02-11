package com.centerm.fud_demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String createTime;
    private Integer state;
    private Integer isOnline;
    private Long roleId;
    //salt 为用户名

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long id, String username, String password, String createTime, Integer state) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createTime = createTime;
        this.state = state;
    }

    public User(Long id, String username, String password, String createTime, Integer state, Integer isOnline) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createTime = createTime;
        this.state = state;
        this.isOnline = isOnline;
    }

}