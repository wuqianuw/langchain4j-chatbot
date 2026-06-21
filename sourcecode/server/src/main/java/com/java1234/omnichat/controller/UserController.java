package com.java1234.omnichat.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java1234.omnichat.common.*;
import com.java1234.omnichat.entity.User;
import com.java1234.omnichat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器(管理员)
 *
 * @author Java1234
 */
@RestController
@RequestMapping("/user")
@RequireLogin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 分页查询用户列表
     *
     * @param page     页码
     * @param size     每页大小
     * @param username 用户名搜索(可选)
     * @return 用户分页数据
     */
    @GetMapping("/list")
    @RequireRole("ADMIN")
    public Result<Page<User>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (username != null && !username.isBlank()) {
            wrapper.like("username", username).or().like("nickname", username);
        }
        wrapper.orderByDesc("create_time");
        Page<User> result = userService.page(new Page<>(page, size), wrapper);
        return Result.success(result);
    }

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 操作结果
     */
    @PostMapping
    @RequireRole("ADMIN")
    public Result<Void> add(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        user.setPassword(Md5Util.encrypt(user.getPassword() != null ? user.getPassword() : "123456"));
        if (user.getRole() == null) user.setRole("USER");
        if (user.getStatus() == null) user.setStatus(1);
        userService.save(user);
        return Result.success("添加成功", null);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 操作结果
     */
    @PutMapping
    @RequireRole("ADMIN")
    public Result<Void> update(@RequestBody User user) {
        User existing = userService.getById(user.getId());
        if (existing == null) {
            throw new BusinessException("用户不存在");
        }
        if (user.getNickname() != null) existing.setNickname(user.getNickname());
        if (user.getRole() != null) existing.setRole(user.getRole());
        if (user.getStatus() != null) existing.setStatus(user.getStatus());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(Md5Util.encrypt(user.getPassword()));
        }
        userService.updateById(existing);
        return Result.success("更新成功", null);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @RequireRole("ADMIN")
    public Result<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 获取当前登录用户信息
     *
     * @param currentUser 当前用户
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<User> info(@CurrentUser User currentUser) {
        return Result.success(currentUser);
    }
}
