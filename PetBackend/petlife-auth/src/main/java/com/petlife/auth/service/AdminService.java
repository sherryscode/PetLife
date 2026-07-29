package com.petlife.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petlife.common.entity.Admin;

public interface AdminService extends IService<Admin> {

    Admin login(String username, String password);
}