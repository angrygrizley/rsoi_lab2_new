package com.angrygrizley.rsoilab2new.groupsservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GroupsServiceControllerTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private GroupsServiceController groupsServiceController;

    @MockBean
    GroupsService groupsService;

    @MockBean
    GroupsRepository groupsRepository;

    @MockBean
    PlacementRepository placementRepository;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        groupsService = new GroupsServiceImplementation(groupsRepository, placementRepository);
        groupsServiceController = new GroupsServiceController(groupsService);
        mvc = MockMvcBuilders.standaloneSetup(groupsServiceController).build();
    }

    @Test
    public void addGroup() throws Exception {
        Group group = new Group();
        group.setGroupName("GroupName");
        group.setGameId(1);
        group.setFreeSpace(1);
        group.setId(1L);

        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);

        group.setPlayers(placementList);

        given(groupsService.addGroup(group)).willReturn(group);

        String json = mapper.writeValueAsString(group);
        mvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isOk());
    }

    @Test
    public void deleteGroup() throws Exception {
        Group group = new Group();
        group.setGroupName("GroupName");
        group.setGameId(1);
        group.setFreeSpace(1);
        group.setId(1L);

        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);

        group.setPlayers(placementList);

        given(groupsService.addGroup(group)).willReturn(group);

        mvc.perform(delete("/groups/delete/1"))

                .andExpect(status().isOk());
    }

    @Test
    public void getGroups() throws Exception {
        Group group = new Group();
        group.setGroupName("GroupName");
        group.setGameId(1);
        group.setFreeSpace(1);
        group.setId(1L);

        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);

        group.setPlayers(placementList);


        List<Group> lg = new ArrayList<>();
        lg.add(group);

        given(groupsService.getGroups()).willReturn(lg);

        mvc.perform(get("/groups"))

                .andExpect(status().isOk());
    }

    @Test
    public void getGroupById() throws Exception {
        Group group = new Group();
        group.setGroupName("GroupName");
        group.setGameId(1);
        group.setFreeSpace(1);
        group.setId(1L);

        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);

        group.setPlayers(placementList);

        given(groupsRepository.findById(group.getId())).willReturn(Optional.of(group));

        mvc.perform(get("/groups/id/1"))

                .andExpect(status().isOk());
    }

    @Test
    public void putGroup() throws Exception {
        Group group = new Group();
        group.setGroupName("GroupName");
        group.setGameId(1);
        group.setFreeSpace(1);
        group.setId(1L);

        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);

        group.setPlayers(placementList);

        given(groupsRepository.findById(group.getId())).willReturn(Optional.of(group));
        given(groupsRepository.save(group)).willReturn(group);

        String json = mapper.writeValueAsString(group);
        mvc.perform(put("/groups/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isOk());
    }

    @Test
    public void getFreeGroups() throws Exception {
        Group group = new Group();
        group.setGroupName("GroupName");
        group.setGameId(1);
        group.setFreeSpace(1);
        group.setId(1L);

        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);

        group.setPlayers(placementList);

        List<Group> lg = new ArrayList<>();
        lg.add(group);


        given(groupsRepository.findAllGroupsByFreeSpace()).willReturn(lg);
        given(groupsService.getFreeGroups()).willReturn(lg);

        String json = mapper.writeValueAsString(group);
        mvc.perform(get("/groups/free")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(status().isOk());
    }

    public void getGroupsForGame() throws Exception {
        Group group = new Group();
        group.setGroupName("GroupName");
        group.setGameId(1);
        group.setFreeSpace(1);
        group.setId(1L);

        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);

        group.setPlayers(placementList);

        List<Group> lg = new ArrayList<>();
        lg.add(group);


        given(groupsRepository.findAllByGameId(1L)).willReturn(lg);
        given(groupsService.getGroupsForGame(1L)).willReturn(lg);

        String json = mapper.writeValueAsString(group);
        mvc.perform(get("/groups/game")
                .param("gameId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void addPlayers() throws Exception {
        /*
        Group group = new Group();
        group.setGroupName("GroupName");
        group.setGameId(1);
        group.setFreeSpace(5);
        group.setId(1L);


        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        group.setPlayers(placementList);

        given(placementRepository.save(placement)).willReturn(placement);
        given(groupsRepository.findById(1L)).willReturn(Optional.of(group));
        given(groupsService.addPlayer(placement.getGroupId(), placement.getPlayerId())).willReturn(placement);

        mvc.perform(post("/groups/players/add")
                .param("groupId", String.valueOf(1L))
                .param("userId", String.valueOf(1L)))

                .andExpect(status().isOk());*/
    }

    @Test
    public void removePlayers() throws Exception {
        Group group = new Group();
        group.setGroupName("GroupName");
        group.setGameId(1);
        group.setFreeSpace(1);
        group.setId(1L);

        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);

        group.setPlayers(placementList);

       // given(groupsService.removePlayer(placement.getGroupId(), placement.getPlayerId())).willReturn(group);

        //String json = mapper.removePlayer(group);
        mvc.perform(delete("/groups/players/remove")
                .param("groupId", String.valueOf(1L))
                .param("userId", String.valueOf(1L)))

                .andExpect(status().isOk());
    }
}