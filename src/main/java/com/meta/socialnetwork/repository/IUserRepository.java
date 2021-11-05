package com.meta.socialnetwork.repository;

import com.meta.socialnetwork.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);


    Iterable<User> findByFullNameIsContaining(String fullName);


    User findByEmail(String email);

    Iterable<User> findAllByUsernameIsContaining(String username);

    Page<User> findAll(Pageable pageable);
}