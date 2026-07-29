package com.petlife.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.auth.mapper.MerchantMapper;
import com.petlife.auth.service.MerchantService;
import com.petlife.common.entity.Merchant;
import com.petlife.common.exception.BusinessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Merchant login(String phone, String password) {
        Merchant merchant = baseMapper.selectOne(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getPhone, phone)
                .eq(Merchant::getStatus, 1));

        if (merchant == null || !passwordEncoder.matches(password, merchant.getPassword())) {
            throw new BusinessException("手机号或密码错误");
        }

        if (merchant.getAuditStatus() != 1) {
            throw new BusinessException("商户审核未通过，请联系管理员");
        }

        return merchant;
    }

    @Override
    public Merchant register(Merchant merchant) {
        Merchant existing = baseMapper.selectOne(new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getPhone, merchant.getPhone()));

        if (existing != null) {
            throw new BusinessException("该手机号已注册");
        }

        merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));
        merchant.setAuditStatus(0);
        merchant.setStatus(1);

        baseMapper.insert(merchant);
        return merchant;
    }
}