package com.meta.socialnetwork.service.post;

import com.meta.socialnetwork.model.Post;
import com.meta.socialnetwork.service.IService;

import java.util.List;

public interface IPostService extends IService<Post> {
    List<Post> findPostsByStatusIsContaining(String status);

    List<Post> findPostsByUser_Id(Long id);

    List<Post> findPostsByStatus(Long id, String status, String status1);

    Iterable<Post> findAllByOrderByIdDesc();

    Iterable<Post> findByStatusOrderByIdDesc(String status);
}
