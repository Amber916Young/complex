package com.complex.service;

import com.complex.entity.UserInfo;
import com.complex.mapper.PermissionMapper;
import com.complex.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PermissionService {
    @Autowired
    PermissionMapper permissionMapper;

    public void updateUserInfo(UserInfo user) {
        permissionMapper.updateUserInfo(user);
    }

    public UserInfo authenticate(HashMap param) {
        return permissionMapper.authenticate(param);

    }

    public List<HashMap> pageQueryUserData(Page page) {
        return permissionMapper.pageQueryUserData(page);
    }

    public int pageQueryUserCount(Page page) {
        return permissionMapper.pageQueryUserCount(page);
    }

    public void deleteUsersByid(int id) {
        permissionMapper.deleteUsersByid(id);
    }
}
