package com.meta.socialnetwork.repository;

import com.meta.socialnetwork.model.Comment;
import com.meta.socialnetwork.model.Like;
import com.meta.socialnetwork.model.Notification;
import com.meta.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INotificationRepo extends JpaRepository<Notification, Long> {
    Iterable<Notification> findAllByComment_Post_User(User user);

    Iterable<Notification> findAllByComment_Post_UserOrderByCommentDesc(User user);

    Iterable<Notification> findAllByLike_Posts_UserOrderByCommentDesc(User user);

    Iterable<Notification> findByComment_Post_User(User user);

    Notification findByComment(Comment comment);

    Notification findByLike(Like like);
}
