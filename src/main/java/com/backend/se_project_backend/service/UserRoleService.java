package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.UserRole;
import com.backend.se_project_backend.utils.UserRoleEnum;

public interface UserRoleService {
    UserRole getUserRoleByEnumName(UserRoleEnum userRoleEnum);
}
