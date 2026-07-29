package com.petlife.health.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.health.service.PetCheckupService;
import com.petlife.common.entity.PetCheckup;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkup")
public class PetCheckupController {

    private final PetCheckupService petCheckupService;

    public PetCheckupController(PetCheckupService petCheckupService) {
        this.petCheckupService = petCheckupService;
    }

    @GetMapping("/{id}")
    public Result<PetCheckup> getById(@PathVariable("id") Long id) {
        PetCheckup checkup = petCheckupService.getById(id);
        return Result.success(checkup);
    }

    @PostMapping
    public Result<PetCheckup> create(@RequestBody PetCheckup checkup) {
        petCheckupService.save(checkup);
        return Result.success("创建成功", checkup);
    }

    @PutMapping("/{id}")
    public Result<PetCheckup> update(@PathVariable("id") Long id, @RequestBody PetCheckup checkup) {
        checkup.setId(id);
        petCheckupService.updateById(checkup);
        return Result.success("更新成功", checkup);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        petCheckupService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<PetCheckup>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam("petId") Long petId) {

        LambdaQueryWrapper<PetCheckup> wrapper = new LambdaQueryWrapper<>();
        if (petId != null) {
            wrapper.eq(PetCheckup::getPetId, petId);
        }

        Page<PetCheckup> pageResult = petCheckupService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @GetMapping("/pet/{petId}")
    public Result<List<PetCheckup>> getByPetId(@PathVariable("petId") Long petId) {
        List<PetCheckup> checkups = petCheckupService.list(new LambdaQueryWrapper<PetCheckup>()
                .eq(PetCheckup::getPetId, petId));
        return Result.success(checkups);
    }
}