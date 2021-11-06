package com.meta.socialnetwork.service.like;

import com.meta.socialnetwork.model.Friend;
import com.meta.socialnetwork.model.Like;
import com.meta.socialnetwork.model.User;
import com.meta.socialnetwork.repository.ILikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService implements ILikeService{
    @Autowired
    ILikeRepo likeRepo;
    @Override
    public Iterable<Like> findAll() {
        return likeRepo.findAll();
    }

    @Override
    public Optional<Like> findById(Long id) {
        return likeRepo.findById(id);
    }

    @Override
    public void save(Like like) {
        likeRepo.save(like);
    }

    @Override
    public void remove(Long id) {
        likeRepo.deleteById(id);
    }

    @Override
    public Iterable<Like> findAllLikeByPosts_Id(Long id) {
        return likeRepo.findAllLikeByPosts_Id(id);
    }

    @Override
    public void deleteAll(Iterable<? extends Like> likes) {
        likeRepo.deleteAll(likes);
    }

    @Override
    public Like findByPostsIdAndUserId(Long idPost, Long idUser) {
        return likeRepo.findByPostsIdAndUserId(idPost, idUser);
    }

    @Override
    public Like saves(Like like) {
        return likeRepo.save(like);
    }

}
