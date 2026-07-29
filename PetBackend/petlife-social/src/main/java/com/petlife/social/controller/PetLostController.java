package com.petlife.social.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.social.service.PetLostService;
import com.petlife.common.annotation.OperationLog;
import com.petlife.common.entity.PetLost;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lost")
public class PetLostController {

    private final PetLostService petLostService;

    public PetLostController(PetLostService petLostService) {
        this.petLostService = petLostService;
    }

    @GetMapping("/{id}")
    public Result<PetLost> getById(@PathVariable("id") Long id) {
        PetLost petLost = petLostService.getById(id);
        return Result.success(petLost);
    }

    @PostMapping
    public Result<PetLost> create(@RequestBody PetLost petLost) {
        petLost.setAuditStatus(0);
        petLostService.save(petLost);
        return Result.success("创建成功，等待审核", petLost);
    }

    @PutMapping("/{id}")
    public Result<PetLost> update(@PathVariable("id") Long id, @RequestBody PetLost petLost) {
        petLost.setId(id);
        petLostService.updateById(petLost);
        return Result.success("更新成功", petLost);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        petLostService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<PetLost>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer auditStatus,
            @RequestParam(required = false) Integer isTop) {

        LambdaQueryWrapper<PetLost> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(PetLost::getUserId, userId);
        }
        if (auditStatus != null) {
            wrapper.eq(PetLost::getAuditStatus, auditStatus);
        }
        if (isTop != null) {
            wrapper.eq(PetLost::getIsTop, isTop);
        }
        wrapper.orderByDesc(PetLost::getIsTop).orderByDesc(PetLost::getCreatedAt);

        Page<PetLost> pageResult = petLostService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @PutMapping("/{id}/audit")
    @OperationLog(module = "内容审核", operation = "审核寻宠启事", description = "审核寻宠启事")
    public Result<PetLost> audit(@PathVariable("id") Long id, @RequestBody Map<String, Integer> params) {
        PetLost petLost = petLostService.getById(id);
        petLost.setAuditStatus(params.get("auditStatus"));
        petLostService.updateById(petLost);
        return Result.success("审核完成", petLost);
    }

    @PutMapping("/{id}/top")
    public Result<PetLost> toggleTop(@PathVariable("id") Long id) {
        PetLost petLost = petLostService.getById(id);
        petLost.setIsTop(petLost.getIsTop() == 1 ? 0 : 1);
        petLostService.updateById(petLost);
        return Result.success(petLost.getIsTop() == 1 ? "已置顶" : "已取消置顶", petLost);
    }
}