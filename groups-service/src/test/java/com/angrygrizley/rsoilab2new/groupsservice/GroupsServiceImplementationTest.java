package com.angrygrizley.rsoilab2new.groupsservice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class GroupsServiceImplementationTest {

    @Mock
    GroupsRepository groupsRepository;

    @Mock
    PlacementRepository placementRepository;

    private GroupsService groupsService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        groupsService = new GroupsServiceImplementation(groupsRepository, placementRepository);
    }

    @Test
    public void addGroup() {
        Group group = new Group();
        group.setGroupName("Club d6");
        group.setGameId(1);
        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);
        group.setPlayers(placementList);
        group.setFreeSpace(2);

        given(groupsRepository.save(group)).willReturn(group);

        Group g = groupsService.addGroup(group);
        assertEquals(group, g);
    }

    @Test
    public void deleteGroup() {
        Group group = new Group();
        group.setGroupName("Club d6");
        group.setGameId(1);
        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);
        group.setPlayers(placementList);
        group.setFreeSpace(2);

        groupsService.addGroup(group);
        groupsService.deleteGroup(1L);
        assertEquals(groupsService.getGroups().isEmpty(), true);
    }

    @Test
    public void getGroups() {
        Group group = new Group();
        group.setGroupName("Club d6");
        group.setGameId(1);
        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);
        group.setPlayers(placementList);
        group.setFreeSpace(2);
        List<Group> lg = new ArrayList<>();
        lg.add(group);

        given(groupsRepository.findAll()).willReturn(lg);
        List<Group> listg = groupsService.getGroups();

        assertEquals(lg, listg);
    }

    @Test
    public void getGroupById() throws Exception {
        Group group = new Group();
        group.setGroupName("Club d6");
        group.setGameId(1);
        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);
        group.setPlayers(placementList);
        group.setFreeSpace(2);
        given(groupsRepository.findById(1L)).willReturn(Optional.of(group));

        Group g = groupsService.getGroupById(1L);
        assertEquals(group, g);
    }

    @Test
    public void putGroup() throws GroupNotFoundException {
        Group group = new Group();
        group.setGroupName("Club d6");
        group.setGameId(1);
        group.setId(1L);
        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);
        group.setPlayers(placementList);
        group.setFreeSpace(2);

        given(groupsRepository.findById(1L)).willReturn((Optional.of(group)));
        given(groupsRepository.save(group)).willReturn(group);
        Group g = groupsService.putGroup(group);
        assertEquals(g, group);
    }

    @Test
    public void getFreeGroups() {
        Group group = new Group();
        group.setGroupName("Club d6");
        group.setGameId(1);
        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);
        group.setPlayers(placementList);
        group.setFreeSpace(1);

        List<Group> lg = new ArrayList<>();
        lg.add(group);

        given(groupsRepository.findAllGroupsByFreeSpace()).willReturn(lg);
        given(groupsService.getFreeGroups()).willReturn(lg);

        List<Group> listgroup = groupsService.getFreeGroups();
        assertEquals(lg, listgroup);
    }

    @Test
    public void getGroupsForGame() {
        Group group = new Group();
        group.setGroupName("Club d6");
        group.setGameId(1);
        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);
        group.setPlayers(placementList);
        group.setFreeSpace(1);

        List<Group> lg = new ArrayList<>();
        lg.add(group);

        given(groupsRepository.findAllByGameId(1L)).willReturn(lg);
        given(groupsService.getGroupsForGame(1L)).willReturn(lg);

        List<Group> listgroup = groupsService.getGroupsForGame(1L);
        assertEquals(lg, listgroup);
    }

    @Test
    public void addPlayer() {
        //
    }

    @Test
    public void removePlayer() throws GroupNotFoundException, PlacementNotFoundException {
        Group group = new Group();
        group.setGroupName("Club d6");
        group.setGameId(1);
        group.setId(1L);

        Placement placement = new Placement();
        placement.setGroupId(1L);
        placement.setPlayerId(1L);
        placement.setId(1L);
        List<Placement> placementList = new ArrayList<>();
        placementList.add(placement);
        group.setPlayers(placementList);
        group.setFreeSpace(1);

        given(groupsRepository.findById(1L)).willReturn(Optional.of(group));
        given(placementRepository.findByGroupIdAndPlayerId(1L, 1L)).willReturn(Optional.of(placement));


        groupsService.removePlayer(placement.getGroupId(), placement.getPlayerId());

    }
}