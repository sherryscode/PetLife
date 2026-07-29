package com.petlife.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.auth.service.MenuService;
import com.petlife.common.entity.Menu;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{id}")
    public Result<Menu> getById(@PathVariable("id") Long id) {
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    @PostMapping
    public Result<Menu> create(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.success("创建成功", menu);
    }

    @PutMapping("/{id}")
    public Result<Menu> update(@PathVariable("id") Long id, @RequestBody Menu menu) {
        menu.setId(id);
        menuService.updateById(menu);
        return Result.success("更新成功", menu);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        menuService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<Menu>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String menuName) {

        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        if (menuName != null) {
            wrapper.like(Menu::getMenuName, menuName);
        }
        wrapper.orderByAsc(Menu::getParentId).orderByAsc(Menu::getSortOrder);

        Page<Menu> pageResult = menuService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @GetMapping("/tree")
    public Result<List<Menu>> tree() {
        List<Menu> menus = menuService.list(new LambdaQueryWrapper<Menu>()
                .orderByAsc(Menu::getParentId).orderByAsc(Menu::getSortOrder));
        return Result.success(menus);
    }
}