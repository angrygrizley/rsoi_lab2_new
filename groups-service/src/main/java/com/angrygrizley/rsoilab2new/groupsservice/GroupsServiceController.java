package com.angrygrizley.rsoilab2new.groupsservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class GroupsServiceController {
    private GroupsService groupsService;
    private Logger logger;

    @Autowired
    GroupsServiceController(GroupsService groupsService) {
        this.groupsService = groupsService;
        logger = LoggerFactory.getLogger(GroupsServiceController.class);
    }

    @PostMapping(value = "/groups")
    public void addGroup(@RequestBody Group group){
        groupsService.addGroup(group);
        logger.info("[POST] /groups " + group);
    }

    @DeleteMapping(value = "groups/delete/{id}")
    public void deleteGroup(@PathVariable Long id) {
        groupsService.deleteGroup(id);
        logger.info("[DELETE] /groups/delete/" + id);
    }

    @GetMapping(value="/groups")
    public List<Group> getGroups(){
        logger.info("[GET] /groups");
        return groupsService.getGroups();
    }

    @GetMapping(value="/groups/id/{id}")
    public Group getGroupById(@PathVariable  Long id) throws GroupNotFoundException{
        logger.info("[GET] /groups/id/" + id);
        return groupsService.getGroupById(id);
    }

    @PutMapping(value="/groups/edit")
    public void putGroup(@RequestBody Group newGroup) throws GroupNotFoundException {
        logger.info("[PUT] /groups/edit");
        groupsService.putGroup(newGroup);
    }

    @GetMapping(value="/groups/free")
    public List<Group> getFreeGroups() {
        logger.info("[GET] /groups/free");
        return groupsService.getFreeGroups();
    }

    @PostMapping(value="/groups/players/add")
    public void addPlayers(@RequestParam (value = "userId") Long playerId, @RequestParam (value = "groupId") Long groupId){
        logger.info("[POST] /groups/players/add " + playerId + " to " + groupId);
        try {
            groupsService.addPlayer(groupId, playerId);
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping(value="/groups/players/remove")
    public void removePlayers(@RequestParam (value = "groupId") Long groupId, @RequestParam (value = "userId") Long playerId) {
        logger.info("[DELETE] /groups/players/remove " + playerId + " from " + groupId);
        try {
            groupsService.removePlayer(groupId, playerId);
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
        } catch (PlacementNotFoundException e) {
            e.printStackTrace();
        }
    }
}
