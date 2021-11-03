package com.meta.socialnetwork.service.comment;

import com.meta.socialnetwork.model.Comment;
import com.meta.socialnetwork.repository.ICommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService implements ICommentService{
    @Autowired
    ICommentRepo commentRepo;
    @Override
    public Iterable<Comment> findAll() {
        return commentRepo.findAll();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepo.findById(id);
    }

    @Override
    public void save(Comment comment) {
        commentRepo.save(comment);
    }

    @Override
    public void remove(Long id) {
        commentRepo.deleteById(id);
    }

    @Override
    public Iterable<Comment> findAllByPost_Id(Long id) {
        return commentRepo.findAllByPost_Id(id);
    }

    @Override
    public void deleteAll(Iterable<? extends Comment> iterable) {
        commentRepo.deleteAll(iterable);
    }
}
