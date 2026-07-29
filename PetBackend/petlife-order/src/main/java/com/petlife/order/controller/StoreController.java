package com.petlife.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.order.service.StoreService;
import com.petlife.common.entity.Store;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{id}")
    public Result<Store> getById(@PathVariable("id") Long id) {
        Store store = storeService.getById(id);
        return Result.success(store);
    }

    @PostMapping
    public Result<Store> create(@RequestBody Store store) {
        storeService.save(store);
        return Result.success("创建成功", store);
    }

    @PutMapping("/{id}")
    public Result<Store> update(@PathVariable("id") Long id, @RequestBody Store store) {
        store.setId(id);
        storeService.updateById(store);
        return Result.success("更新成功", store);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        storeService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<Store>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) String storeName) {

        LambdaQueryWrapper<Store> wrapper = new LambdaQueryWrapper<>();
        if (merchantId != null) {
            wrapper.eq(Store::getMerchantId, merchantId);
        }
        if (storeName != null) {
            wrapper.like(Store::getStoreName, storeName);
        }

        Page<Store> pageResult = storeService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @GetMapping("/merchant/{merchantId}")
    public Result<List<Store>> getByMerchantId(@PathVariable("merchantId") Long merchantId) {
        List<Store> stores = storeService.list(new LambdaQueryWrapper<Store>()
                .eq(Store::getMerchantId, merchantId));
        return Result.success(stores);
    }
}