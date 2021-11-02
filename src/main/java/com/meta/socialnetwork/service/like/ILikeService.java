package com.meta.socialnetwork.service.like;

import com.meta.socialnetwork.model.Like;
import com.meta.socialnetwork.service.IService;

public interface ILikeService extends IService<Like> {
    Iterable<Like> findAllLikeByPosts_Id(Long id);
    void deleteAll(Iterable<? extends Like> likes);
}
