package com.petlife.social.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.social.mapper.CommunityPostMapper;
import com.petlife.social.service.CommunityPostService;
import com.petlife.social.service.PostLikeService;
import com.petlife.common.entity.CommunityPost;
import com.petlife.common.entity.PostLike;
import org.springframework.stereotype.Service;

@Service
public class CommunityPostServiceImpl extends ServiceImpl<CommunityPostMapper, CommunityPost> implements CommunityPostService {

    private final PostLikeService postLikeService;

    public CommunityPostServiceImpl(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @Override
    public CommunityPost likePost(Long postId, Long userId) {
        CommunityPost post = baseMapper.selectById(postId);
        
        LambdaQueryWrapper<PostLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PostLike::getPostId, postId);
        wrapper.eq(PostLike::getUserId, userId);
        
        PostLike existingLike = postLikeService.getOne(wrapper);
        
        if (existingLike != null) {
            if (existingLike.getStatus() == 1) {
                existingLike.setStatus(0);
                postLikeService.updateById(existingLike);
                post.setLikes(post.getLikes() - 1);
            } else {
                existingLike.setStatus(1);
                postLikeService.updateById(existingLike);
                post.setLikes(post.getLikes() + 1);
            }
        } else {
            PostLike newLike = new PostLike();
            newLike.setPostId(postId);
            newLike.setUserId(userId);
            newLike.setStatus(1);
            postLikeService.save(newLike);
            post.setLikes(post.getLikes() + 1);
        }
        
        baseMapper.updateById(post);
        return post;
    }

    @Override
    public CommunityPost favoritePost(Long postId) {
        CommunityPost post = baseMapper.selectById(postId);
        post.setFavorites(post.getFavorites() + 1);
        baseMapper.updateById(post);
        return post;
    }

    @Override
    public boolean isLiked(Long postId, Long userId) {
        LambdaQueryWrapper<PostLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PostLike::getPostId, postId);
        wrapper.eq(PostLike::getUserId, userId);
        wrapper.eq(PostLike::getStatus, 1);
        return postLikeService.count(wrapper) > 0;
    }
}