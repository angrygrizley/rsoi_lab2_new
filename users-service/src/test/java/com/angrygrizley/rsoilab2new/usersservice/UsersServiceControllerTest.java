package com.angrygrizley.rsoilab2new.usersservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersServiceControllerTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UsersServiceController usersServiceController;

    @MockBean
    UsersService usersService;

    @MockBean
    UsersRepository usersRepository;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        usersService = new UsersServiceImplementation(usersRepository);
        usersServiceController = new UsersServiceController(usersService);
        mvc = MockMvcBuilders.standaloneSetup(usersServiceController).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addUser() throws Exception {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");

        //given(usersService.addUser(user)).willReturn(user);

        String json = mapper.writeValueAsString(user);
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isOk())
       ;// .andExpect((ResultMatcher) jsonPath("title", is(User.getTitle())));
    }


    @Test
    public void getUsers() throws Exception {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");
        List<User> lg = new ArrayList<>();
        lg.add(user);

        //given(UsersService.getUsers()).willReturn(lg);

        mvc.perform(get("/users"))

                .andExpect(status().isOk())
        ;
    }

    @Test
    public void getUserById() throws Exception {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");

        given(usersRepository.findById(1L)).willReturn(Optional.of(user));
       // given(UsersService.getUserById(1L)).willReturn(User);

        mvc.perform(get("/users/id/1"))

                .andExpect(status().isOk())
        ;
    }

    @Test
    public void getUserByLogin() throws Exception {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");

        //given(usersRepository.findById(1L)).willReturn(Optional.of(user));
        given(usersRepository.findByLogin("UserLogin")).willReturn(user);
        // given(UsersService.getUserById(1L)).willReturn(User);

        mvc.perform(get("/users/login/UserLogin"))

                .andExpect(status().isOk())
        ;
    }


    @Test
    public void putUser() throws Exception {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");

        given(usersRepository.findById(1L)).willReturn(Optional.of(user));
        given(usersRepository.save(user)).willReturn(user);
        //given(UsersService.putUser(User)).willReturn(User);

        String json = mapper.writeValueAsString(user);
        mvc.perform(put("/users/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isOk())
        ;
    }


    @Test
    public void deleteUser() throws Exception {
        User user = new User();
        user.setGroupNum(0);
        user.setId(1L);
        user.setLogin("UserLogin");
        user.setLogin("UserName");
        List<User> lg = new ArrayList<>();
        lg.add(user);

        //String json = mapper.writeValueAsString(User);
        mvc.perform(delete("/users/delete/1"))

                .andExpect(status().isOk())
        ;
    }
}