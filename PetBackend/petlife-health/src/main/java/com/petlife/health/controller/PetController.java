package com.petlife.health.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.health.service.PetService;
import com.petlife.common.entity.Pet;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/{id}")
    public Result<Pet> getById(@PathVariable("id") Long id) {
        Pet pet = petService.getById(id);
        return Result.success(pet);
    }

    @PostMapping
    public Result<Pet> create(@RequestBody Pet pet) {
        petService.save(pet);
        return Result.success("创建成功", pet);
    }

    @PutMapping("/{id}")
    public Result<Pet> update(@PathVariable("id") Long id, @RequestBody Pet pet) {
        pet.setId(id);
        petService.updateById(pet);
        return Result.success("更新成功", pet);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        petService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<Pet>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam("userId") Long userId,
            @RequestParam(required = false) String petName) {

        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Pet::getUserId, userId);
        }
        if (petName != null) {
            wrapper.like(Pet::getPetName, petName);
        }

        Page<Pet> pageResult = petService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @GetMapping("/user/{userId}")
    public Result<List<Pet>> getByUserId(@PathVariable("userId") Long userId) {
        List<Pet> pets = petService.list(new LambdaQueryWrapper<Pet>()
                .eq(Pet::getUserId, userId));
        return Result.success(pets);
    }
}