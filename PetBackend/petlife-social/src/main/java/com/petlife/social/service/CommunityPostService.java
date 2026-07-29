package com.petlife.social.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.petlife.common.entity.CommunityPost;

public interface CommunityPostService extends IService<CommunityPost> {

    CommunityPost likePost(Long postId, Long userId);

    CommunityPost favoritePost(Long postId);

    boolean isLiked(Long postId, Long userId);
}