package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Account;
import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.model.User;
import com.backend.se_project_backend.model.UserRole;
import com.backend.se_project_backend.repository.AccountsRepository;
import com.backend.se_project_backend.repository.UserRepository;
import com.backend.se_project_backend.utils.UserRoleEnum;
import com.backend.se_project_backend.utils.dto.AccountDTO;
import com.backend.se_project_backend.utils.dto.UserEditDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;
    private final AccountsRepository accountsRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleService userRoleService, AccountsRepository accountsRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleService = userRoleService;
        this.accountsRepository = accountsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public User findUserByUsername(String username) {
        Optional<User> byUsername = this.userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        } else {
            throw new IllegalStateException("Can not find user with this username");
        }
    }

    @Override
    public boolean userByUsernameExists(String username) {
        Optional<User> byUsername = this.userRepository.findByUsername(username);
        return byUsername.isPresent();
    }

    @Override
    public boolean userByEmailExists(String email) {
        Optional<User> byEmail = this.userRepository.findByEmail(email);
        return byEmail.isPresent();
    }

    @Override
    public Account register(AccountDTO user) {
        UserRole userRole = this.userRoleService.getUserRoleByEnumName(UserRoleEnum.USER);
        Account newAccount = this.modelMapper.map(user, Account.class);
        newAccount.setRoles(List.of(userRole));
        newAccount.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return accountsRepository.save(newAccount);
    }

    @Override
    public Account findAccountByUsername(String username) {
        return this.accountsRepository.findByUsername(username).orElseThrow();
    }

    @Override
    public void delete(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        user.ifPresent(value -> this.userRepository.deleteById(value.getId()));
    }

    @Override
    public Account edit(UserEditDTO userEditDTO) {
        Optional<Account> user = this.accountsRepository.findByUsername(userEditDTO.getUsername());
        if(user.isPresent()) {
            user.get().setLegalName(userEditDTO.getLegalName());
            user.get().setPhoneNumber(userEditDTO.getPhoneNumber());
            return accountsRepository.save(user.get());
        }
        return null;
    }

    @Override
    public Account editStartRide(Ride ride, String username) {
        Optional<Account> user = this.accountsRepository.findByUsername(username);
        if(user.isPresent()) {
            if (!user.get().isActiveRide()) {
                user.get().setCurrentRide(ride);
                user.get().setActiveRide(true);
                return accountsRepository.save(user.get());
            }
        }
        return null;
    }
    @Override
    public Account editEndRide(Ride ride, String username) {
        Optional<Account> user = this.accountsRepository.findByUsername(username);
        if(user.isPresent()) {
            if (user.get().isActiveRide()) {
                user.get().setActiveRide(false);
                Account account = findAccountByUsername(username);
                account.addRide(ride);
                accountsRepository.save(account);
                return accountsRepository.save(user.get());
            }
        }
        return null;
    }
}
