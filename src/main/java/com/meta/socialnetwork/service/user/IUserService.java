package com.meta.socialnetwork.service.user;

import com.meta.socialnetwork.model.User;
import com.meta.socialnetwork.service.IService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IService<User> {
    User loadUserByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    List<User> finAllByRoleName(String roleName);
}
