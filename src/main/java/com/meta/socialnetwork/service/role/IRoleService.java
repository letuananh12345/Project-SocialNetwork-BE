package com.meta.socialnetwork.service.role;

import com.meta.socialnetwork.model.Role;
import com.meta.socialnetwork.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name);
}
