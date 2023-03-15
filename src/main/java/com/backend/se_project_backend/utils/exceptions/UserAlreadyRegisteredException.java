package com.backend.se_project_backend.utils.exceptions;

public class UserAlreadyRegisteredException extends Exception{
    public UserAlreadyRegisteredException() {
    }

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
