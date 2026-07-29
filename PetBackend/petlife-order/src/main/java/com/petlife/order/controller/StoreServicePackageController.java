package com.petlife.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.order.service.StoreServicePackageService;
import com.petlife.common.entity.StoreService;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
public class StoreServicePackageController {

    private final StoreServicePackageService storeServicePackageService;

    public StoreServicePackageController(StoreServicePackageService storeServicePackageService) {
        this.storeServicePackageService = storeServicePackageService;
    }

    @GetMapping("/{id}")
    public Result<StoreService> getById(@PathVariable("id") Long id) {
        StoreService service = storeServicePackageService.getById(id);
        return Result.success(service);
    }

    @PostMapping
    public Result<StoreService> create(@RequestBody StoreService service) {
        storeServicePackageService.save(service);
        return Result.success("创建成功", service);
    }

    @PutMapping("/{id}")
    public Result<StoreService> update(@PathVariable("id") Long id, @RequestBody StoreService service) {
        service.setId(id);
        storeServicePackageService.updateById(service);
        return Result.success("更新成功", service);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        storeServicePackageService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<StoreService>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam("storeId") Long storeId,
            @RequestParam(required = false) Integer serviceType) {

        LambdaQueryWrapper<StoreService> wrapper = new LambdaQueryWrapper<>();
        if (storeId != null) {
            wrapper.eq(StoreService::getStoreId, storeId);
        }
        if (serviceType != null) {
            wrapper.eq(StoreService::getServiceType, serviceType);
        }

        Page<StoreService> pageResult = storeServicePackageService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @GetMapping("/store/{storeId}")
    public Result<List<StoreService>> getByStoreId(@PathVariable("storeId") Long storeId) {
        List<StoreService> services = storeServicePackageService.list(new LambdaQueryWrapper<StoreService>()
                .eq(StoreService::getStoreId, storeId));
        return Result.success(services);
    }
}