package com.backend.se_project_backend.dto;

import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.utils.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetailsDTO {

    private String username;

    private String email;

    private UserRoleEnum role;

    private boolean hasActiveRide = false;

    private RideDTO currentRide;

    private String legalName;

    private String phoneNumber;

    private boolean isLocked = false; //can lock the account
}
