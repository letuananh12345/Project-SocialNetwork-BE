package com.meta.socialnetwork.repository;

import com.meta.socialnetwork.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICommentRepo extends JpaRepository<Comment, Long> {
    @Query("select cmt from Comment cmt where cmt.post.id = ?1")
    Iterable<Comment> findAllByPost_Id(Long id);
    void deleteAll(Iterable<? extends Comment> iterable);
}
