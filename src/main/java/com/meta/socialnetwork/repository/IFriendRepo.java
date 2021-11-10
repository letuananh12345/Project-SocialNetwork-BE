package com.meta.socialnetwork.repository;

import com.meta.socialnetwork.model.Friend;
import com.meta.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IFriendRepo extends JpaRepository<Friend,Long> {
    @Query("select f from Friend f where f.user = ?1 and  f.friend = ?2")
    Friend findByUser_idAndFriend_id(User user, User friend);

    @Query("select f from Friend f where f.user = ?1 and  f.friend = ?2 and f.status =?3 or f.friend =?4 and f.user =?5 and f.status =?6")
    Friend suggestion(User user, User friend, Boolean status1, User user1, User friend1, Boolean status);

    @Query("select f from Friend f where f.user = ?1 and f.status = ?2 or f.friend = ?3 and f.status = ?4")
    List<Friend> findAllByIdAcc(User account, Boolean status1, User friend, Boolean status2);

    @Query("select f from Friend f where f.user = ?1 and f.friend =?2 and f.status = ?3  or f.friend = ?4 and f.user =?5 and f.status = ?6")
    Boolean isFriend(User account, User friend1, Boolean status1, User user, User friend, Boolean status2);

    @Query("select f from Friend f where f.friend =?1 and f.status =?2")
    List<Friend> findFriendAdd(User user, Boolean status);

    @Query("select f from Friend f where f.user =?1 and f.status =?2")
    List<Friend> findFriendRequest(User user, Boolean status);

}
