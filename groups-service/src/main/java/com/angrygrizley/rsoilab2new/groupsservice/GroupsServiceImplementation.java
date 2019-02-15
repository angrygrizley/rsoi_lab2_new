package com.angrygrizley.rsoilab2new.groupsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupsServiceImplementation implements GroupsService {
    private final GroupsRepository groupsRepository;
    private final PlacementRepository placementRepository;

    @Autowired
    public GroupsServiceImplementation(GroupsRepository groupsRepository, PlacementRepository placementRepository) {
        this.groupsRepository = groupsRepository;
        this.placementRepository = placementRepository;
    }

    @Override
    public void addGroup(Group group) { groupsRepository.save(group); }

    @Override
    public void deleteGroup(Long id) { groupsRepository.deleteById(id); }


    @Override
    public List<Group> getGroups() { return groupsRepository.findAll();
    }

    @Override
    public Group getGroupById(Long id) throws GroupNotFoundException {
        return groupsRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
    }

    @Override
    public Group putGroup(Group newGroup) throws GroupNotFoundException {
        return groupsRepository.findById(newGroup.getId()).map(Group -> {
            Group.setFree_space(newGroup.getFree_space());
            Group.setGroup_name(newGroup.getGroup_name());
            Group.setGame_id(newGroup.getGame_id());
            Group.setPlayers(newGroup.getPlayers());
            return groupsRepository.save(Group);
        }).orElseThrow(() -> new GroupNotFoundException(newGroup.getId()));
    }

    @Override
    public List<Group> getFreeGroups() {
        return groupsRepository.findAllByFree_spaceIsNotNull();
    }

    @Override
    public List<Group> getGroupsForGame(Long gameId) {
        return groupsRepository.findAllByGame_id(gameId);
    }

    @Override
    public void addPlayer(Long groupId, Long playerId) throws GroupNotFoundException {
        Group g = groupsRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
        int freeSpace = g.getFree_space();
        if (freeSpace != 0) {
            placementRepository.save(new Placement(groupId, playerId));
            g.setFree_space(freeSpace-1);
        }
    }

    @Override
    public void removePlayer(Long groupId, Long playerId) throws GroupNotFoundException, PlacementNotFoundException {
        Group g = groupsRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));

        Placement placement = placementRepository.findByGroup_idAndPlayer_id(groupId, playerId).orElseThrow(
                () -> new PlacementNotFoundException(groupId, playerId));

        placementRepository.deleteByGroup_idAndPlayer_id(groupId, playerId);
        int freeSpace = g.getFree_space();
        g.setFree_space(freeSpace + 1);
    }
}
