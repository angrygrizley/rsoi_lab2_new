package com.angrygrizley.rsoilab2new.usersservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsersServiceController {
    private UsersService usersService;
    private Logger logger;

    @Autowired
    UsersServiceController(UsersService usersService) {
        this.usersService = usersService;
        logger = LoggerFactory.getLogger(UsersServiceController.class);
    }

    @PostMapping(value = "/users")
    public void addUser(@RequestBody User user){
        usersService.addUser(user);
        logger.info("[POST] /users", user);
    }

    @GetMapping(value = "/users")
    public List<User> getUsers(){
        logger.info("[GET] /users");
        return usersService.getUsers();
    }

    @GetMapping(value = "/users/id/{id}")
    public User getUserById(@PathVariable Long id) throws UserNotFoundException {
        logger.info("[GET] /users/" + id);
        return usersService.getUserById(id);
    }

    @GetMapping(value = "/users/login/{login}")
    public User getUserByLogin(@PathVariable String login) throws UserNotFoundException {
        logger.info("[GET] /users/" + login);
        return usersService.getUserByLogin(login);
    }

    @PutMapping(value="/users/edit")
    public void putUser(@RequestBody User user) throws UserNotFoundException {
        logger.info("[PUT] /users/edit");
        usersService.putUser(user);
    }

    @DeleteMapping(value="/users/delete/{id}")
    public void deleteUser(@PathVariable Long id){
        logger.info("[DELETE] /users/"+ id);
        usersService.deleteUser(id);
    }
}
