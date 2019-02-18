package com.angrigrizley.rsoilab2new.gatewayservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
public class GatewayServiceController {
    private final GatewayService gatewayService;
    private Logger logger;

    @Autowired
    public GatewayServiceController(GatewayService gatewayService) {
        logger = LoggerFactory.getLogger(GatewayServiceController.class);
        this.gatewayService = gatewayService;
    }

    @PostMapping(path="/users")
    public void addUser(@RequestBody String user) throws IOException {
        logger.info("[POST] /users" + user);
        gatewayService.addUser(user);
    }

    @GetMapping(path="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUsers() throws IOException {
        logger.info("[GET] /users");
        return gatewayService.getUsers();
    }

    @GetMapping(path="/users/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserById(Long id) throws IOException {
        logger.info("[GET] /users/id/" + id);
        return gatewayService.getUserById(id);
    }

    @PostMapping(path = "/games")
    public void addGame(@RequestBody String game) throws IOException {
        logger.info("[POST] /games" + game);
        gatewayService.addGame(game);
    }

    @GetMapping(path = "/games")
    public String getGames() throws IOException {
        logger.info("[GET] /games");
        return gatewayService.getGames();
    }

    @GetMapping(path = "/games/id/{id}")
    public String getGameById(@PathVariable Long id) throws IOException{
        logger.info("[GET} /games/id/" + id);
        return gatewayService.getGameById(id);
    }

    @GetMapping(path = "/games/genre/{genre}")
    public String getGamesByGenre(@PathVariable String genre) throws IOException {
        logger.info("[GET] /games/genre/" + genre);
        return gatewayService.getGamesByGenre(genre);
    }

    @GetMapping(path = "/games/playernum/{num}")
    public String getGamesByPlayerNum(@PathVariable int num) throws IOException {
        logger.info("[GET] /games/playernum/" + num);
        return gatewayService.getGamesByPlayerNum(num);
    }

    @PostMapping(path = "/groups")
    public void addGroup(@RequestBody String group) throws IOException {
        logger.info("[POST] /groups"+group);
        gatewayService.addGroup(group);
    }

    @DeleteMapping(path = "/groups/delete/{id}")
    public void deleteGroup(Long id) throws IOException {
        logger.info("[DELETE] /groups/delete/" + id);
        gatewayService.deleteGroup(id);
    }

    @GetMapping(path = "/groups")
    public String getGroups() throws IOException {
        logger.info("[GET] /groups");
        return gatewayService.getGroups();
    }

    @GetMapping(path="/groups/id/{id}")
    public String getGroupdById(@PathVariable Long id) throws IOException {
        logger.info("[GET] /groups/id" + id);
        return gatewayService.getGroupById(id);
    }

    @GetMapping(path = "/groups/free")
    public String getFreeGroups() throws IOException {
        logger.info("[GET] /groups/free");
        return gatewayService.getFreeGroups();
    }

    @GetMapping(path = "/groups/game/{id}")
    public String getGroupsByGame(@PathVariable Long gameId) throws  IOException {
        logger.info("[GET] /groups/game/" + gameId);
        return gatewayService.getGroupsByGame(gameId);
    }

    @PostMapping(path = "/groups/players/add")
    public void addPlayer(@RequestParam (value = "userid") Long userId, @RequestParam (value = "groupid") Long groupId)
            throws  IOException {
        logger.info("[POST] /groups/players/add" + userId + " to group " + groupId);
        gatewayService.addPlayer(userId, groupId);
    }

    @DeleteMapping(path = "/groups/players/remove")
    public void removePlayer(@RequestParam (value = "userid") Long userId, @RequestParam (value = "groupid") Long groupId)
            throws  IOException {
        logger.info("[POST] /groups/players/remove" + userId + " from group " + groupId);
        gatewayService.removePlayer(userId, groupId);
    }


}
