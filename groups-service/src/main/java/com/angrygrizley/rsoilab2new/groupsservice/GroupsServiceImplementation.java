package com.angrygrizley.rsoilab2new.groupsservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Group addGroup(Group group) { return groupsRepository.save(group); }

    @Override
    public void deleteGroup(Long id) {
        groupsRepository.deleteById(id);
        placementRepository.deleteAllByGroupId(id);
    }


    @Override
    public List<Group> getGroups() { return groupsRepository.findAll();
    }

    @Override
    public Group getGroupById(Long id) throws GroupNotFoundException {
        return groupsRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
    }

    @Override
    public Group putGroup(Group newGroup) throws GroupNotFoundException {
        Group group =  groupsRepository.findById(newGroup.getId()).orElseThrow(() -> new GroupNotFoundException(newGroup.getId()));
        group.setPlayers(newGroup.getPlayers());
        group.setFreeSpace(newGroup.getFreeSpace());
        group.setGameId(newGroup.getGameId());
        group.setGroupName(newGroup.getGroupName());
        return groupsRepository.save(group);
    }

    @Override
    public List<Group> getFreeGroups() {
        return groupsRepository.findAllGroupsByFreeSpace();
    }

    @Override
    public List<Group> getGroupsForGame(Long gameId) {
        return groupsRepository.findAllByGameId(gameId);
    }

    @Override
    public Placement addPlayer(Long groupId, Long playerId) throws GroupNotFoundException, NoFreeSpaceException {
        Group g = groupsRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));
        int freeSpace = g.getFreeSpace();
        if (freeSpace != 0) {
            Placement newP = new Placement(groupId, playerId);
            Placement placement = placementRepository.save(newP);
            g.getPlayers().add(placement);
            g.setFreeSpace(freeSpace-1);
            groupsRepository.save(g);

            return placement;
        }
        else throw new NoFreeSpaceException(groupId);
    }

    @Override
    public void removePlayer(Long groupId, Long playerId) throws GroupNotFoundException, PlacementNotFoundException {
        Group g = groupsRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException(groupId));

        Placement placement = placementRepository.findByGroupIdAndPlayerId(groupId, playerId).orElseThrow(
                () -> new PlacementNotFoundException(groupId, playerId));

        int freeSpace = g.getFreeSpace();
        g.setFreeSpace(freeSpace + 1);
        g.getPlayers().remove(placement);

        groupsRepository.save(g);
        placementRepository.deleteById(placement.getId());

    }
}
