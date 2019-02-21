package com.angrigrizley.rsoilab2new.gatewayservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GatewayServiceControllerTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Mock
    private GatewayServiceController gatewayServiceController;

    @MockBean
    private GatewayService gatewayService;

    @Before
    public void setUp() throws Exception {

        gatewayService = Mockito.spy(new GatewayServiceImplementation());
        gatewayServiceController = new GatewayServiceController(gatewayService);
        mvc = MockMvcBuilders.standaloneSetup(gatewayServiceController).build();
        initMocks(this);
    }

    @Test
    public void addUser() throws Exception {
        JSONObject user = new JSONObject();
        user.put("name", "UserName");
        user.put("login", "UserLogin");
        user.put("groupNum", 0);

//        given(gatewayService.addUser(user.toString())).willReturn(user.toString());
        doReturn(user.toString()).when(gatewayService).addUser(user.toString());

        String json = mapper.writeValueAsString(user);
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isOk());
    }

    @Test
    public void getUsers() throws Exception {
        JSONObject user = new JSONObject();

        user.put("groupNum", 0);
        user.put("login", "UserLogin");
        user.put("name", "UserName");

        JSONArray userList = new JSONArray();
        userList.put(user);

        doReturn(userList.toString()).when(gatewayService).getUsers();

        mvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserById() throws Exception {
        JSONObject user = new JSONObject();

        user.put("groupNum", 0);
        user.put("login", "UserLogin");
        user.put("name", "UserName");
        user.put("id", 1L);

        doReturn(user.toString()).when(gatewayService).getUserById(1L);

        mvc.perform(get("/users/id/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void addGame() throws Exception {
        JSONObject game = new JSONObject();
        game.put("groupNum", 0);
        game.put("iitle", "Carcasson");
        game.put("genre", "strategy");
        game.put("minNum", 2);
        game.put("maxNum", 5);

//        given(gatewayService.addUser(user.toString())).willReturn(user.toString());
        doReturn(game.toString()).when(gatewayService).addGame(game.toString());

        String json = mapper.writeValueAsString(game);
        mvc.perform(post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isOk());
    }

    @Test
    public void getGames() throws Exception {
        JSONObject game = new JSONObject();
        game.put("groupNum", 0);
        game.put("iitle", "Carcasson");
        game.put("genre", "strategy");
        game.put("minNum", 2);
        game.put("maxNum", 5);

        JSONArray gameList = new JSONArray();
        gameList.put(game);

        doReturn(gameList.toString()).when(gatewayService).getGames(1, 1);

        mvc.perform(get("/games"))
                .andExpect(status().isOk());
    }

    @Test
    public void getGameById() throws Exception {
        JSONObject game = new JSONObject();
        game.put("groupNum", 0);
        game.put("iitle", "Carcasson");
        game.put("genre", "strategy");
        game.put("minNum", 2);
        game.put("maxNum", 5);
        game.put("id", 1L);

        doReturn(game.toString()).when(gatewayService).getGameById(1L);

        mvc.perform(get("/games/id/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void searchGames() throws Exception {
        JSONObject game = new JSONObject();
        game.put("groupNum", 0);
        game.put("iitle", "Carcasson");
        game.put("genre", "strategy");
        game.put("minNum", 2);
        game.put("maxNum", 5);

        doReturn(game.toString()).when(gatewayService).searchGames("strategy", 3);

        mvc.perform(get("/games/search?genre=strategy&playernum=3"))
                .andExpect(status().isOk());
    }

    @Test
    public void addGroup() throws Exception {
        JSONObject group = new JSONObject();
        group.put("groupName", "Club d6");
        group.put("freeSpace", 3);
        group.put("gameId", 1);
        group.put("id", 1);

        JSONObject placement = new JSONObject();
        placement.put("groupId", 1);
        placement.put("playerId", 2);

        JSONArray playerList = new JSONArray();
        playerList.put(placement);
        group.put("players", playerList.toString());

        doReturn(group.toString()).when(gatewayService).addGroup(group.toString());

        String json = mapper.writeValueAsString(group);
        mvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isOk());
    }

    @Test
    public void deleteGroup() throws Exception {
        JSONObject group = new JSONObject();
        group.put("groupName", "Club d6");
        group.put("freeSpace", 3);
        group.put("gameId", 1);
        group.put("id", 1);

        JSONObject placement = new JSONObject();
        placement.put("groupId", 1);
        placement.put("playerId", 2);

        JSONArray playerList = new JSONArray();
        playerList.put(placement);
        group.put("players", playerList.toString());

        doReturn(true).when(gatewayService).deleteGroup(1L);

        String json = mapper.writeValueAsString(group);
        mvc.perform(delete("/groups/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getGroups() throws Exception {
        JSONObject group = new JSONObject();
        group.put("groupName", "Club d6");
        group.put("freeSpace", 3);
        group.put("gameId", 1);
        group.put("id", 1);

        JSONObject placement = new JSONObject();
        placement.put("groupId", 1);
        placement.put("playerId", 2);

        JSONArray playerList = new JSONArray();
        playerList.put(placement);
        group.put("players", playerList.toString());

        JSONArray groupList = new JSONArray();
        groupList.put(group);

        doReturn(groupList.toString()).when(gatewayService).getGroups();

        mvc.perform(get("/groups"))
                .andExpect(status().isOk());
    }

    @Test
    public void getGroupdById() throws Exception {
        JSONObject group = new JSONObject();
        group.put("groupName", "Club d6");
        group.put("freeSpace", 3);
        group.put("gameId", 1);
        group.put("id", 1);

        JSONObject placement = new JSONObject();
        placement.put("groupId", 1);
        placement.put("playerId", 2);

        JSONArray playerList = new JSONArray();
        playerList.put(placement);
        group.put("players", playerList.toString());

        doReturn(group.toString()).when(gatewayService).getGroupById(1L);

        mvc.perform(get("/groups/id/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getFreeGroups() throws Exception {
        JSONObject group = new JSONObject();
        group.put("groupName", "Club d6");
        group.put("freeSpace", 3);
        group.put("gameId", 1);
        group.put("id", 1);

        JSONObject placement = new JSONObject();
        placement.put("groupId", 1);
        placement.put("playerId", 2);

        JSONArray playerList = new JSONArray();
        playerList.put(placement);
        group.put("players", playerList.toString());

        JSONArray groupList = new JSONArray();
        groupList.put(group);

        doReturn(groupList.toString()).when(gatewayService).getFreeGroups();

        mvc.perform(get("/groups/free"))
                .andExpect(status().isOk());
    }

    @Test
    public void getGroupsByGame() throws Exception {
        JSONObject group = new JSONObject();
        group.put("groupName", "Club d6");
        group.put("freeSpace", 3);
        group.put("gameId", 1L);
        group.put("id", 1);

        JSONObject placement = new JSONObject();
        placement.put("groupId", 1);
        placement.put("playerId", 2);

        JSONArray playerList = new JSONArray();
        playerList.put(placement);
        group.put("players", playerList.toString());

        JSONArray groupList = new JSONArray();
        groupList.put(group);

        doReturn(groupList.toString()).when(gatewayService).getGroupsByGame(1L);

        mvc.perform(get("/groups/game/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void addPlayer() throws Exception {
        JSONObject group = new JSONObject();
        group.put("groupName", "Club d6");
        group.put("freeSpace", 3);
        group.put("gameId", 1L);
        group.put("id", 1);

        JSONObject placement = new JSONObject();
        placement.put("groupId", 1);
        placement.put("playerId", 2);

        JSONArray playerList = new JSONArray();
        playerList.put(placement);
        group.put("players", playerList.toString());


        doReturn(group.toString()).when(gatewayService).addPlayer(3L, 1L);

        mvc.perform(post("/groups/players/add")
                .param("userid", String.valueOf(3L))
                .param("groupid", String.valueOf(1L)))
                .andExpect(status().isOk());
    }

    @Test
    public void removePlayer() throws Exception {
        JSONObject group = new JSONObject();
        group.put("groupName", "Club d6");
        group.put("freeSpace", 3);
        group.put("gameId", 1L);
        group.put("id", 1);

        JSONObject placement = new JSONObject();
        placement.put("groupId", 1);
        placement.put("playerId", 2);

        JSONArray playerList = new JSONArray();
        playerList.put(placement);
        group.put("players", playerList.toString());


        doReturn(true).when(gatewayService).removePlayer(2L, 1L);

        mvc.perform(delete("/groups/players/remove")
                .param("userid", String.valueOf(2L))
                .param("groupid", String.valueOf(1L)))
                .andExpect(status().isOk());
    }
}