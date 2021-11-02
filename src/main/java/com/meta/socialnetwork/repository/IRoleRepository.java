package com.meta.socialnetwork.repository;

import com.meta.socialnetwork.model.Role;
import com.meta.socialnetwork.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName name);
}
