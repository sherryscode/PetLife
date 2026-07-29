package com.petlife.social.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.social.mapper.PetLostMapper;
import com.petlife.social.service.PetLostService;
import com.petlife.common.entity.PetLost;
import org.springframework.stereotype.Service;

@Service
public class PetLostServiceImpl extends ServiceImpl<PetLostMapper, PetLost> implements PetLostService {
}