package com.angrygrizley.rsoilab2new.usersservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImplementation implements UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImplementation(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @Override
    public void addUser(User user) {
        usersRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return usersRepository.findAll();
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        return usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User getUserByLogin(String login) {
        return usersRepository.findByLogin(login);
    }

    @Override
    public User putUser(User newUser) throws UserNotFoundException {
        return usersRepository.findById(newUser.getId()).map(User -> {
            User.setName(newUser.getName());
            User.setLogin(newUser.getLogin());
            User.setGroupNum(newUser.getGroupNum());
            return usersRepository.save(User);
        }).orElseThrow(() -> new UserNotFoundException(newUser.getId()));
    }

    @Override
    public void deleteUser(Long id) {
        usersRepository.deleteById(id);
    }
}
