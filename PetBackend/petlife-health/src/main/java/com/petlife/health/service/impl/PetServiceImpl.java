package com.petlife.health.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.health.mapper.PetMapper;
import com.petlife.health.service.PetService;
import com.petlife.common.entity.Pet;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements PetService {
}