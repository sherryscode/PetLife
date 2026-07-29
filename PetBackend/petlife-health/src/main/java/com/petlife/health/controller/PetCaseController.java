package com.petlife.health.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.health.service.PetCaseService;
import com.petlife.common.entity.PetCase;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/case")
public class PetCaseController {

    private final PetCaseService petCaseService;

    public PetCaseController(PetCaseService petCaseService) {
        this.petCaseService = petCaseService;
    }

    @GetMapping("/{id}")
    public Result<PetCase> getById(@PathVariable("id") Long id) {
        PetCase petCase = petCaseService.getById(id);
        return Result.success(petCase);
    }

    @PostMapping
    public Result<PetCase> create(@RequestBody PetCase petCase) {
        petCaseService.save(petCase);
        return Result.success("创建成功", petCase);
    }

    @PutMapping("/{id}")
    public Result<PetCase> update(@PathVariable("id") Long id, @RequestBody PetCase petCase) {
        petCase.setId(id);
        petCaseService.updateById(petCase);
        return Result.success("更新成功", petCase);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        petCaseService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<PetCase>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam("petId") Long petId) {

        LambdaQueryWrapper<PetCase> wrapper = new LambdaQueryWrapper<>();
        if (petId != null) {
            wrapper.eq(PetCase::getPetId, petId);
        }

        Page<PetCase> pageResult = petCaseService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @GetMapping("/pet/{petId}")
    public Result<List<PetCase>> getByPetId(@PathVariable("petId") Long petId) {
        List<PetCase> cases = petCaseService.list(new LambdaQueryWrapper<PetCase>()
                .eq(PetCase::getPetId, petId));
        return Result.success(cases);
    }
}