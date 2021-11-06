package com.meta.socialnetwork.service.like;

import com.meta.socialnetwork.model.Friend;
import com.meta.socialnetwork.model.Like;
import com.meta.socialnetwork.model.User;
import com.meta.socialnetwork.service.IService;

import java.util.List;

public interface ILikeService extends IService<Like> {
    Iterable<Like> findAllLikeByPosts_Id(Long id);

    void deleteAll(Iterable<? extends Like> likes);

    Like findByPostsIdAndUserId(Long idPost, Long idUser);

    Like saves( Like like);

}
