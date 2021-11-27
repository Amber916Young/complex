package com.complex.entity;

import lombok.Data;

@Data
public class UserInfo {
    private int id;
    private String avatar;
//    private String email;
//    private String phone;
    private String username;
    private String gender;
    private String loginTime;
    private String loginPosition;
    private String loginMark;
    private String loginIp;
    private String password;
    private String registerTime;
}
