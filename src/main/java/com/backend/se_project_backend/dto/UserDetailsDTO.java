package com.backend.se_project_backend.dto;

import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.utils.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    private Ride currentRide;

    private String legalName;

    private List<Ride> rideList;

    private String phoneNumber;

    private boolean isLocked = false; //can lock the account
}
