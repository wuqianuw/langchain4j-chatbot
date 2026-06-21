package com.java1234.omnichat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java1234.omnichat.common.*;
import com.java1234.omnichat.entity.Conversation;
import com.java1234.omnichat.entity.User;
import com.java1234.omnichat.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 会话管理控制器
 *
 * @author Java1234
 */
@RestController
@RequestMapping("/conversation")
@RequireLogin
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    /**
     * 获取当前用户的会话列表
     *
     * @param currentUser 当前用户
     * @return 会话列表
     */
    @GetMapping("/list")
    public Result<List<Conversation>> list(@CurrentUser User currentUser) {
        return Result.success(conversationService.listByUserId(currentUser.getId()));
    }

    /**
     * 创建新会话
     *
     * @param currentUser 当前用户
     * @param title       会话标题(可选)
     * @return 新会话
     */
    @PostMapping
    public Result<Conversation> create(@CurrentUser User currentUser,
                                       @RequestParam(required = false) String title) {
        return Result.success(conversationService.createConversation(currentUser.getId(), title));
    }

    /**
     * 重命名会话
     *
     * @param id          会话ID
     * @param title       新标题
     * @param currentUser 当前用户
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Void> rename(@PathVariable Long id, @RequestParam String title,
                               @CurrentUser User currentUser) {
        Conversation conv = conversationService.getById(id);
        if (conv == null || !conv.getUserId().equals(currentUser.getId())) {
            throw new BusinessException("会话不存在");
        }
        conv.setTitle(title);
        conversationService.updateById(conv);
        return Result.success("重命名成功", null);
    }

    /**
     * 删除会话
     *
     * @param id          会话ID
     * @param currentUser 当前用户
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, @CurrentUser User currentUser) {
        Conversation conv = conversationService.getById(id);
        if (conv == null || !conv.getUserId().equals(currentUser.getId())) {
            throw new BusinessException("会话不存在");
        }
        conversationService.removeById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 管理员分页查询所有会话
     *
     * @param page 页码
     * @param size 每页大小
     * @return 会话分页数据
     */
    @GetMapping("/admin/list")
    @RequireRole("ADMIN")
    public Result<Page<Conversation>> adminList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Conversation> result = conversationService.lambdaQuery()
                .orderByDesc(Conversation::getUpdateTime)
                .page(new Page<>(page, size));
        return Result.success(result);
    }
}
