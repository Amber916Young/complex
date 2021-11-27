package com.complex.mapper;

import com.complex.entity.UserInfo;
import com.complex.utils.Page;

import java.util.HashMap;
import java.util.List;

public interface PermissionMapper {
    void updateUserInfo(UserInfo user);

    UserInfo authenticate(HashMap param);

    List<HashMap> pageQueryUserData(Page page);

    int pageQueryUserCount(Page page);

    void deleteUsersByid(int id);
}
