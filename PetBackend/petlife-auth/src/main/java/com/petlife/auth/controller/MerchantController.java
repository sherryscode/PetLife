package com.petlife.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.auth.service.MerchantService;
import com.petlife.common.entity.Merchant;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import com.petlife.common.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/merchant")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");

        Merchant merchant = merchantService.login(phone, password);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", merchant.getId());
        claims.put("role", "merchant");

        String token = JwtUtil.generateToken(claims);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("merchant", merchant);

        return Result.success(result);
    }

    @PostMapping("/register")
    public Result<Merchant> register(@RequestBody Merchant merchant) {
        Merchant created = merchantService.register(merchant);
        return Result.success("注册成功，等待审核", created);
    }

    @GetMapping("/{id}")
    public Result<Merchant> getById(@PathVariable("id") Long id) {
        Merchant merchant = merchantService.getById(id);
        return Result.success(merchant);
    }

    @PutMapping("/{id}")
    public Result<Merchant> update(@PathVariable("id") Long id, @RequestBody Merchant merchant) {
        merchant.setId(id);
        merchantService.updateById(merchant);
        return Result.success("更新成功", merchant);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        merchantService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<Merchant>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String merchantName,
            @RequestParam(required = false) Integer auditStatus) {

        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        if (merchantName != null) {
            wrapper.like(Merchant::getMerchantName, merchantName);
        }
        if (auditStatus != null) {
            wrapper.eq(Merchant::getAuditStatus, auditStatus);
        }

        Page<Merchant> pageResult = merchantService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @PutMapping("/{id}/audit")
    public Result<Merchant> audit(@PathVariable("id") Long id, @RequestBody Map<String, Integer> params) {
        Merchant merchant = merchantService.getById(id);
        merchant.setAuditStatus(params.get("auditStatus"));
        merchantService.updateById(merchant);
        return Result.success("审核完成", merchant);
    }
}