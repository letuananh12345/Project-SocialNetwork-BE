package com.meta.socialnetwork.service.role;


import com.meta.socialnetwork.model.Role;
import com.meta.socialnetwork.model.RoleName;
import com.meta.socialnetwork.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RoleService implements IRoleService{

    @Autowired
    IRoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
