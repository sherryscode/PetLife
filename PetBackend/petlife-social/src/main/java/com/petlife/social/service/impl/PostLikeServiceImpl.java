package com.petlife.social.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.petlife.social.mapper.PostLikeMapper;
import com.petlife.social.service.PostLikeService;
import com.petlife.common.entity.PostLike;
import org.springframework.stereotype.Service;

@Service
public class PostLikeServiceImpl extends ServiceImpl<PostLikeMapper, PostLike> implements PostLikeService {
}