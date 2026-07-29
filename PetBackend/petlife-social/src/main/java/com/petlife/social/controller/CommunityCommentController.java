package com.petlife.social.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.petlife.social.service.CommunityCommentService;
import com.petlife.social.service.CommunityPostService;
import com.petlife.common.entity.CommunityComment;
import com.petlife.common.entity.CommunityPost;
import com.petlife.common.response.PageResult;
import com.petlife.common.response.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommunityCommentController {

    private final CommunityCommentService communityCommentService;
    private final CommunityPostService communityPostService;

    public CommunityCommentController(CommunityCommentService communityCommentService, CommunityPostService communityPostService) {
        this.communityCommentService = communityCommentService;
        this.communityPostService = communityPostService;
    }

    @GetMapping("/{id}")
    public Result<CommunityComment> getById(@PathVariable("id") Long id) {
        CommunityComment comment = communityCommentService.getById(id);
        return Result.success(comment);
    }

    @PostMapping
    public Result<CommunityComment> create(@RequestBody CommunityComment comment) {
        comment.setLikes(0);
        communityCommentService.save(comment);
        
        CommunityPost post = communityPostService.getById(comment.getPostId());
        if (post != null) {
            post.setComments(post.getComments() + 1);
            communityPostService.updateById(post);
        }
        
        return Result.success("评论成功", comment);
    }

    @PutMapping("/{id}")
    public Result<CommunityComment> update(@PathVariable("id") Long id, @RequestBody CommunityComment comment) {
        comment.setId(id);
        communityCommentService.updateById(comment);
        return Result.success("更新成功", comment);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        communityCommentService.removeById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/list")
    public Result<PageResult<CommunityComment>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam("postId") Long postId,
            @RequestParam("userId") Long userId) {

        LambdaQueryWrapper<CommunityComment> wrapper = new LambdaQueryWrapper<>();
        if (postId != null) {
            wrapper.eq(CommunityComment::getPostId, postId);
        }
        if (userId != null) {
            wrapper.eq(CommunityComment::getUserId, userId);
        }
        wrapper.orderByAsc(CommunityComment::getParentId).orderByDesc(CommunityComment::getCreatedAt);

        Page<CommunityComment> pageResult = communityCommentService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(pageResult.getRecords(), pageResult.getTotal(), page, size));
    }

    @GetMapping("/post/{postId}")
    public Result<List<CommunityComment>> getByPostId(@PathVariable("postId") Long postId) {
        List<CommunityComment> comments = communityCommentService.list(new LambdaQueryWrapper<CommunityComment>()
                .eq(CommunityComment::getPostId, postId)
                .orderByAsc(CommunityComment::getParentId).orderByDesc(CommunityComment::getCreatedAt));
        return Result.success(comments);
    }

    @PutMapping("/{id}/like")
    public Result<CommunityComment> like(@PathVariable("id") Long id) {
        CommunityComment comment = communityCommentService.likeComment(id);
        return Result.success(comment);
    }
}