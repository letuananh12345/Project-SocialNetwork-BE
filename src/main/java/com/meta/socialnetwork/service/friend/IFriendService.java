package com.meta.socialnetwork.service.friend;

import com.meta.socialnetwork.model.Friend;
import com.meta.socialnetwork.model.User;
import com.meta.socialnetwork.service.IService;

import java.util.List;

public interface IFriendService extends IService<Friend> {
    Friend findByUser_idAndFriend_id(User user, User friend);

    List<Friend> findAllByIdAcc(User account, Boolean status1, User friend, Boolean status2);

    Boolean isFriend(User account, User friend1, Boolean status1, User user, User friend, Boolean status2);

}
