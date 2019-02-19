package com.angrygrizley.rsoilab2new.usersservice;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Long id){
        super("User with id= " + id + " not found");
    }

    public UserNotFoundException(String login){
        super("User with login= " + login + " not found");
    }
}
