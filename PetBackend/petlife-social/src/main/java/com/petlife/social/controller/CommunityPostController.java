package com.petlife.social.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.social.service.CommunityPostService;
import com.petlife.common.annotation.OperationLog;
import com.petlife.common.entity.CommunityPost;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class CommunityPostController {

    private final CommunityPostService communityPostService;

    public CommunityPostController(CommunityPostService communityPostService) {
        this.communityPostService = communityPostService;
    }

    @GetMapping("/{id}")
    public Result<CommunityPost> getById(@PathVariable("id") Long id) {
        CommunityPost post = communityPostService.getById(id);
        return Result.success(post);
    }

    @PostMapping
    public Result<CommunityPost> create(@RequestBody CommunityPost post) {
        post.setAuditStatus(0);
        post.setLikes(0);
        post.setComments(0);
        post.setFavorites(0);
        communityPostService.save(post);
        return Result.success("发布成功，等待审核", post);
    }

    @PutMapping("/{id}")
    public Result<CommunityPost> update(@PathVariable("id") Long id, @RequestBody CommunityPost post) {
        post.setId(id);
        communityPostService.updateById(post);
        return Result.success("更新成功", post);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        communityPostService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<CommunityPost>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer auditStatus) {

        LambdaQueryWrapper<CommunityPost> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(CommunityPost::getUserId, userId);
        }
        if (auditStatus != null) {
            wrapper.eq(CommunityPost::getAuditStatus, auditStatus);
        }
        wrapper.orderByDesc(CommunityPost::getLikes).orderByDesc(CommunityPost::getCreatedAt);

        Page<CommunityPost> pageResult = communityPostService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @PutMapping("/{id}/like")
    public Result<CommunityPost> like(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        CommunityPost post = communityPostService.likePost(id, userId);
        return Result.success(post);
    }

    @GetMapping("/{id}/isLiked")
    public Result<Map<String, Boolean>> isLiked(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        boolean liked = communityPostService.isLiked(id, userId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("liked", liked);
        return Result.success(result);
    }

    @PutMapping("/{id}/favorite")
    public Result<CommunityPost> favorite(@PathVariable("id") Long id) {
        CommunityPost post = communityPostService.favoritePost(id);
        return Result.success(post);
    }

    @PutMapping("/{id}/audit")
    @OperationLog(module = "内容审核", operation = "审核帖子", description = "审核社区帖子")
    public Result<CommunityPost> audit(@PathVariable("id") Long id, @RequestBody Map<String, Integer> params) {
        CommunityPost post = communityPostService.getById(id);
        post.setAuditStatus(params.get("auditStatus"));
        communityPostService.updateById(post);
        return Result.success("审核完成", post);
    }
}