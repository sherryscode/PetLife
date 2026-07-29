package com.petlife.health.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.health.mapper.PetCaseMapper;
import com.petlife.health.service.PetCaseService;
import com.petlife.common.entity.PetCase;
import org.springframework.stereotype.Service;

@Service
public class PetCaseServiceImpl extends ServiceImpl<PetCaseMapper, PetCase> implements PetCaseService {
}