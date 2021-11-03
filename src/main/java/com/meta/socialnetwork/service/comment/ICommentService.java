package com.meta.socialnetwork.service.comment;

import com.meta.socialnetwork.model.Comment;
import com.meta.socialnetwork.service.IService;
import org.springframework.data.jpa.repository.Query;

public interface ICommentService extends IService<Comment> {
    @Query("select cmt from Comment cmt where cmt.post.id = ?1")
    Iterable<Comment> findAllByPost_Id(Long id);
    void deleteAll(Iterable<? extends Comment> iterable);
}
