package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.model.UserRole;
import com.backend.se_project_backend.repository.UserRoleRepository;
import com.backend.se_project_backend.utils.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService{

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole getUserRoleByEnumName(UserRoleEnum userRoleEnum) {
        Optional<UserRole> byRole = this.userRoleRepository.findByRole(userRoleEnum);
        if (byRole.isPresent()) {
            return byRole.get();
        } else {
            throw new IllegalStateException("User role not found. Please seed the roles.");
        }
    }

    /**
     * Called at program start
     * @param event
     */
    @EventListener
    public void seedRoles(ContextRefreshedEvent event) {
        //valid login user: root passw: rootroot
        List<User> seededUsers = new ArrayList<>();
        //simple user
        if (userRoleRepository.count() == 0) {
            UserRole userRole = new UserRole();
            userRole.setRole(UserRoleEnum.USER);
            this.userRoleRepository.save(userRole);
        }
    }
}
