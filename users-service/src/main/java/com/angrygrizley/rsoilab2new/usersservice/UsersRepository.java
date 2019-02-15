package com.angrygrizley.rsoilab2new.usersservice;

import com.angrygrizley.rsoilab2new.usersservice.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
