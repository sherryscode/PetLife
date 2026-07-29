package com.petlife.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.mall.service.GoodsService;
import com.petlife.common.annotation.OperationLog;
import com.petlife.common.entity.Goods;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("/{id}")
    public Result<Goods> getById(@PathVariable("id") Long id) {
        Goods goods = goodsService.getById(id);
        return Result.success(goods);
    }

    @PostMapping
    public Result<Goods> create(@RequestBody Goods goods) {
        goods.setAuditStatus(0);
        goods.setSales(0);
        goodsService.save(goods);
        return Result.success("创建成功，等待审核", goods);
    }

    @PutMapping("/{id}")
    public Result<Goods> update(@PathVariable("id") Long id, @RequestBody Goods goods) {
        goods.setId(id);
        goodsService.updateById(goods);
        return Result.success("更新成功", goods);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        goodsService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<Goods>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long merchantId,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String goodsName,
            @RequestParam(required = false) Integer auditStatus) {

        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        if (merchantId != null) {
            wrapper.eq(Goods::getMerchantId, merchantId);
        }
        if (category != null) {
            wrapper.eq(Goods::getCategory, category);
        }
        if (goodsName != null) {
            wrapper.like(Goods::getGoodsName, goodsName);
        }
        if (auditStatus != null) {
            wrapper.eq(Goods::getAuditStatus, auditStatus);
        }
        wrapper.orderByDesc(Goods::getIsHot).orderByDesc(Goods::getSales);

        Page<Goods> pageResult = goodsService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @GetMapping("/hot")
    public Result<List<Goods>> getHotGoods() {
        List<Goods> goods = goodsService.list(new LambdaQueryWrapper<Goods>()
                .eq(Goods::getIsHot, 1)
                .eq(Goods::getStatus, 1)
                .eq(Goods::getAuditStatus, 1)
                .orderByDesc(Goods::getSales)
                .last("LIMIT 10"));
        return Result.success(goods);
    }

    @PutMapping("/{id}/audit")
    @OperationLog(module = "内容审核", operation = "审核商品", description = "审核商城商品")
    public Result<Goods> audit(@PathVariable("id") Long id, @RequestBody Map<String, Integer> params) {
        Goods goods = goodsService.getById(id);
        goods.setAuditStatus(params.get("auditStatus"));
        goodsService.updateById(goods);
        return Result.success("审核完成", goods);
    }

    @PutMapping("/{id}/status")
    public Result<Goods> updateStatus(@PathVariable("id") Long id, @RequestBody Integer status) {
        Goods goods = goodsService.getById(id);
        goods.setStatus(status);
        goodsService.updateById(goods);
        return Result.success(status == 1 ? "已上架" : "已下架", goods);
    }
}