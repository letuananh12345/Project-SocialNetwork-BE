package com.meta.socialnetwork.service.post;

import com.meta.socialnetwork.model.Post;
import com.meta.socialnetwork.repository.IPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService{

    @Autowired
    IPostRepo postRepo;
    @Override
    public Iterable<Post> findAll() {
        return postRepo.findAll();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepo.findById(id);
    }

    @Override
    public void save(Post post) {
        postRepo.save(post);
    }

    @Override
    public void remove(Long id) {
        postRepo.deleteById(id);
    }

    @Override
    public List<Post> findPostsByStatusIsContaining(String status) {
        return postRepo.findPostsByStatusIsContaining(status);
    }

    @Override
    public List<Post> findPostsByUser_Id(Long id) {
        return postRepo.findPostsByUser_Id(id);
    }

    @Override
    public List<Post> findPostsByStatus(Long id, String status, String status1) {
        return postRepo.findPostsByStatus(id, status, status1);
    }

    @Override
    public List<Post> findAllByOrderByIdDesc() {
        List<Post> posts = postRepo.findAllByOrderByIdDesc();
        return posts;
    }

    @Override
    public Iterable<Post> findByStatusOrderByIdDesc(String status) {
        return postRepo.findByStatusOrderByIdDesc(status);
    }
}
