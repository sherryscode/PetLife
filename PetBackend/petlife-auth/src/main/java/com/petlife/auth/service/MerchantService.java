package com.petlife.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petlife.common.entity.Merchant;

public interface MerchantService extends IService<Merchant> {

    Merchant login(String phone, String password);

    Merchant register(Merchant merchant);
}