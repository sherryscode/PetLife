package com.petlife.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.auth.service.UserService;
import com.petlife.common.entity.User;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import com.petlife.common.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");

        User user = userService.login(phone, password);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", "user");

        String token = JwtUtil.generateToken(claims);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);

        return Result.success(result);
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String password = params.get("password");

        User user = userService.register(phone, password);
        return Result.success("注册成功", user);
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @PutMapping("/{id}")
    public Result<User> update(@PathVariable("id") Long id, @RequestBody User user) {
        user.setId(id);
        userService.updateById(user);
        return Result.success("更新成功", user);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        userService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<User>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String nickname) {

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (phone != null) {
            wrapper.like(User::getPhone, phone);
        }
        if (nickname != null) {
            wrapper.like(User::getNickname, nickname);
        }

        Page<User> pageResult = userService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }
}