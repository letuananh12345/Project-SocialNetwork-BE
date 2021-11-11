package com.meta.socialnetwork.repository;

import com.meta.socialnetwork.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPostRepo extends JpaRepository<Post, Long> {
    List<Post> findPostsByStatusIsContaining(String status);

    List<Post> findPostsByUser_Id(Long id);

    @Query("select p from Post p where p.user.id=?1 and p.status=?2 or p.status=?3")
    List<Post> findPostsByStatus(Long id, String status, String status1);

    List<Post> findAllByOrderByIdDesc();

    Iterable<Post> findByStatusOrderByIdDesc(String status);
}
