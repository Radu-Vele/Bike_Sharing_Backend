package com.backend.se_project_backend.model;

import com.backend.se_project_backend.utils.UserRoleEnum;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class UserRole extends BaseEntity{
    private UserRoleEnum role;

    @Enumerated(EnumType.STRING)
    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
}
