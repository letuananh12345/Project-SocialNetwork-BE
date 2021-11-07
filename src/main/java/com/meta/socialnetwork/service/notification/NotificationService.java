package com.meta.socialnetwork.service.notification;

import com.meta.socialnetwork.model.Comment;
import com.meta.socialnetwork.model.Like;
import com.meta.socialnetwork.model.Notification;
import com.meta.socialnetwork.model.User;
import com.meta.socialnetwork.repository.INotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService implements INotificationService{
    @Autowired
    INotificationRepo notificationRepo;
    @Override
    public Iterable<Notification> findAll() {
        return notificationRepo.findAll();
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepo.findById(id);
    }

    @Override
    public void save(Notification notification) {


    }

    @Override
    public void remove(Long id) {
        notificationRepo.findById(id);
    }

    @Override
    public Notification saves(Notification notification) {
        return notificationRepo.save(notification);
    }

    @Override
    public Iterable<Notification> findAllByComment_Post_User(User user) {
        return notificationRepo.findAllByComment_Post_User(user);
    }

    @Override
    public Iterable<Notification> findByComment_Post_User(User user) {
        return notificationRepo.findByComment_Post_User(user);
    }

    @Override
    public Notification findByComment(Comment comment) {
        return notificationRepo.findByComment(comment);
    }

    @Override
    public Iterable<Notification> findAllByComment_Post_UserOrderByComment(User user) {
        Iterable<Notification> notifications = notificationRepo.findAllByComment_Post_UserOrderByCommentDesc(user);
        notifications.forEach(notification -> {
            notification.setPostId(notification.getComment().getPost().getId());
        });
        return notifications;

    }

    @Override
    public Iterable<Notification> findAllByLike_Posts_UserOrderByCommentDesc(User user) {
        return notificationRepo.findAllByLike_Posts_UserOrderByCommentDesc(user);
    }

    @Override
    public Notification findByLike(Like like) {
        return notificationRepo.findByLike(like);
    }


}
