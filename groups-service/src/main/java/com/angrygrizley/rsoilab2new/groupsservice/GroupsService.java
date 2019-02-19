package com.angrygrizley.rsoilab2new.groupsservice;

import java.util.List;

public interface GroupsService {
    Group addGroup(Group group);
    void deleteGroup(Long id);
    List<Group> getGroups();
    Group getGroupById(Long id) throws GroupNotFoundException;
    Group putGroup(Group newGroup) throws GroupNotFoundException;
    List<Group> getFreeGroups();
    List<Group> getGroupsForGame(Long gameId);
    Placement addPlayer(Long gameId, Long playerId) throws GroupNotFoundException, NoFreeSpaceException;
    void removePlayer(Long gameId, Long playerId) throws GroupNotFoundException, PlacementNotFoundException;
}
