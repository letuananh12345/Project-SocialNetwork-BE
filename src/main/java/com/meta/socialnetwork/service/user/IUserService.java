package com.meta.socialnetwork.service.user;

import com.meta.socialnetwork.model.User;
import com.meta.socialnetwork.service.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IService<User> {
    User loadUserByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);


    List<User> finAllByRoleName(String roleName);

    Iterable<User> findAllByUsernameIsContaining(String username);


    Iterable<User> findByFullNameIsContaining(String fullName);


    Page<User> findAll(Pageable pageable);

    Iterable<User> findAllByOrderByIdDesc();


}
