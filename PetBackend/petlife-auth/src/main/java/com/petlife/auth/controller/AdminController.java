package com.petlife.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.auth.service.AdminService;
import com.petlife.auth.service.SysLogService;
import com.petlife.common.annotation.OperationLog;
import com.petlife.common.entity.Admin;
import com.petlife.common.entity.SysLog;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import com.petlife.common.utils.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final SysLogService sysLogService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdminController(AdminService adminService, SysLogService sysLogService) {
        this.adminService = adminService;
        this.sysLogService = sysLogService;
    }

    @PostMapping("/login")
    @OperationLog(module = "系统管理", operation = "登录", description = "管理员登录")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        Admin admin = adminService.login(username, password);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", admin.getId());
        claims.put("role", "admin");

        String token = JwtUtil.generateToken(claims);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("admin", admin);

        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<Admin> getById(@PathVariable("id") Long id) {
        Admin admin = adminService.getById(id);
        return Result.success(admin);
    }

    @PostMapping
    public Result<Admin> create(@RequestBody Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setStatus(1);
        adminService.save(admin);
        return Result.success("创建成功", admin);
    }

    @PutMapping("/{id}")
    public Result<Admin> update(@PathVariable("id") Long id, @RequestBody Admin admin) {
        admin.setId(id);
        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        adminService.updateById(admin);
        return Result.success("更新成功", admin);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        adminService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<Admin>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username) {

        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        if (username != null) {
            wrapper.like(Admin::getUsername, username);
        }

        Page<Admin> pageResult = adminService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @PutMapping("/{id}/resetPassword")
    public Result<Void> resetPassword(@PathVariable("id") Long id) {
        Admin admin = adminService.getById(id);
        admin.setPassword(passwordEncoder.encode("123456"));
        adminService.updateById(admin);
        return Result.success("密码已重置为123456");
    }
}

@RestController
@RequestMapping("/log")
class LogController {

    private final SysLogService sysLogService;

    public LogController(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }

    @GetMapping("/list")
    public Result<PageResult<SysLog>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operation) {

        LambdaQueryWrapper<SysLog> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(SysLog::getDescription, keyword).or().like(SysLog::getUrl, keyword));
        }
        if (module != null && !module.isEmpty()) {
            wrapper.like(SysLog::getModule, module);
        }
        if (operation != null && !operation.isEmpty()) {
            wrapper.like(SysLog::getOperation, operation);
        }
        wrapper.orderByDesc(SysLog::getCreatedAt);

        Page<SysLog> pageResult = sysLogService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @GetMapping("/{id}")
    public Result<SysLog> getById(@PathVariable("id") Long id) {
        SysLog sysLog = sysLogService.getById(id);
        return Result.success(sysLog);
    }
}