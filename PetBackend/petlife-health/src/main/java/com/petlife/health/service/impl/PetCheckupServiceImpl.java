package com.petlife.health.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.health.mapper.PetCheckupMapper;
import com.petlife.health.service.PetCheckupService;
import com.petlife.common.entity.PetCheckup;
import org.springframework.stereotype.Service;

@Service
public class PetCheckupServiceImpl extends ServiceImpl<PetCheckupMapper, PetCheckup> implements PetCheckupService {
}