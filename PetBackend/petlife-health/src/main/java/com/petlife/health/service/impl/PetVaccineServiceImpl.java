package com.petlife.health.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.health.mapper.PetVaccineMapper;
import com.petlife.health.service.PetVaccineService;
import com.petlife.common.entity.PetVaccine;
import org.springframework.stereotype.Service;

@Service
public class PetVaccineServiceImpl extends ServiceImpl<PetVaccineMapper, PetVaccine> implements PetVaccineService {
}