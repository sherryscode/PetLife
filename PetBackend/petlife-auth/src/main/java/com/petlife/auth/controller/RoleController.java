package com.petlife.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.auth.mapper.RoleMenuMapper;
import com.petlife.auth.service.RoleService;
import com.petlife.common.entity.Role;
import com.petlife.common.entity.RoleMenu;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;
    private final RoleMenuMapper roleMenuMapper;

    public RoleController(RoleService roleService, RoleMenuMapper roleMenuMapper) {
        this.roleService = roleService;
        this.roleMenuMapper = roleMenuMapper;
    }

    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable("id") Long id) {
        Role role = roleService.getById(id);
        return Result.success(role);
    }

    @PostMapping
    public Result<Role> create(@RequestBody Role role) {
        roleService.save(role);
        return Result.success("创建成功", role);
    }

    @PutMapping("/{id}")
    public Result<Role> update(@PathVariable("id") Long id, @RequestBody Role role) {
        role.setId(id);
        roleService.updateById(role);
        return Result.success("更新成功", role);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        roleService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<Role>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String roleName) {

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        if (roleName != null) {
            wrapper.like(Role::getRoleName, roleName);
        }

        Page<Role> pageResult = roleService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @PutMapping("/{id}/menus")
    public Result<Void> assignMenus(@PathVariable("id") Long id, @RequestBody List<Long> menuIds) {
        roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, id));

        for (Long menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(id);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }

        return Result.success("菜单分配成功");
    }

    @GetMapping("/{id}/menus")
    public Result<List<Long>> getMenuIds(@PathVariable("id") Long id) {
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getRoleId, id));
        List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).toList();
        return Result.success(menuIds);
    }
}