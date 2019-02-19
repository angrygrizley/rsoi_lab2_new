package com.angrygrizley.rsoilab2new.usersservice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersServiceImplementationTest {

    @Mock
    UsersRepository usersRepository;

    private UsersService usersService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        usersService = new UsersServiceImplementation(usersRepository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addUser() {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");

        given(usersRepository.save(user)).willReturn(user);

        assertEquals(usersService.addUser(user), user);
    }

    @Test
    public void getUsers() {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");
        List<User> lg = new ArrayList<>();
        lg.add(user);

        given(usersRepository.findAll()).willReturn(lg);

        List<User> listU = usersService.getUsers();
        assertEquals(listU.get(0), lg.get(0));
    }

    @Test
    public void getUserById() throws UserNotFoundException {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");

        given(usersRepository.findById(1L)).willReturn(Optional.of(user));


        User u = usersService.getUserById(1L);
        assertEquals(u, user);
    }

    @Test
    public void getUserByLogin() {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");

        given(usersRepository.findByLogin("UserLogin")).willReturn(user);


        User u = usersService.getUserByLogin("UserLogin");
        assertEquals(u, user);
    }

    @Test
    public void putUser() throws UserNotFoundException {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");


        given(usersRepository.findById(1L)).willReturn(Optional.of(user));
        given(usersRepository.save(user)).willReturn(user);

        User u = usersService.putUser(user);
        assertEquals(u, user);
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");

        usersService.addUser(user);
        usersService.deleteUser(1L);
        assertEquals(usersService.getUsers().isEmpty(), true);
    }
}