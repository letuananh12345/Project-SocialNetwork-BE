package com.meta.socialnetwork.repository;

import com.meta.socialnetwork.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ILikeRepo extends JpaRepository<Like, Long> {
    @Query("select l from Like l where l.posts.id = ?1")
    Iterable<Like> findAllLikeByPosts_Id(Long id);
    void deleteAll(Iterable<? extends Like> likes);
}
