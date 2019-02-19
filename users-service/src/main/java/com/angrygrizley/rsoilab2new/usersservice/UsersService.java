package com.angrygrizley.rsoilab2new.usersservice;

import java.util.List;

public interface UsersService {
     User addUser(User user);
     List<User> getUsers();
     User getUserById(Long id) throws UserNotFoundException;
     User getUserByLogin(String login);
     User putUser(User newUser) throws UserNotFoundException;
     void deleteUser(Long id);
}
