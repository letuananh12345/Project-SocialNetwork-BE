package com.meta.socialnetwork.service.notification;

import com.meta.socialnetwork.model.Comment;
import com.meta.socialnetwork.model.Notification;
import com.meta.socialnetwork.model.User;
import com.meta.socialnetwork.service.IService;

public interface INotificationService extends IService<Notification> {
    Notification saves(Notification notification);

    Iterable<Notification> findAllByComment_Post_User(User user);

    Iterable<Notification> findByComment_Post_User(User user);

    Notification findByComment(Comment comment);

}
