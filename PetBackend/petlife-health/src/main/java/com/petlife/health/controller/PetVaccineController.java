package com.petlife.health.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.health.service.PetVaccineService;
import com.petlife.common.entity.PetVaccine;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vaccine")
public class PetVaccineController {

    private final PetVaccineService petVaccineService;

    public PetVaccineController(PetVaccineService petVaccineService) {
        this.petVaccineService = petVaccineService;
    }

    @GetMapping("/{id}")
    public Result<PetVaccine> getById(@PathVariable("id") Long id) {
        PetVaccine vaccine = petVaccineService.getById(id);
        return Result.success(vaccine);
    }

    @PostMapping
    public Result<PetVaccine> create(@RequestBody PetVaccine vaccine) {
        petVaccineService.save(vaccine);
        return Result.success("创建成功", vaccine);
    }

    @PutMapping("/{id}")
    public Result<PetVaccine> update(@PathVariable("id") Long id, @RequestBody PetVaccine vaccine) {
        vaccine.setId(id);
        petVaccineService.updateById(vaccine);
        return Result.success("更新成功", vaccine);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        petVaccineService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<PetVaccine>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam("petId") Long petId) {

        LambdaQueryWrapper<PetVaccine> wrapper = new LambdaQueryWrapper<>();
        if (petId != null) {
            wrapper.eq(PetVaccine::getPetId, petId);
        }

        Page<PetVaccine> pageResult = petVaccineService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @GetMapping("/pet/{petId}")
    public Result<List<PetVaccine>> getByPetId(@PathVariable("petId") Long petId) {
        List<PetVaccine> vaccines = petVaccineService.list(new LambdaQueryWrapper<PetVaccine>()
                .eq(PetVaccine::getPetId, petId));
        return Result.success(vaccines);
    }

    @GetMapping("/listAll")
    public Result<PageResult<PetVaccine>> listAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<PetVaccine> pageResult = petVaccineService.page(new Page<>(page, size));
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }
}