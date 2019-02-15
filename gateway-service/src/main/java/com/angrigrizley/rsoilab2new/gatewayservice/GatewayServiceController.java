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

    @GetMapping("path = /games/id/{id}")
    public String getGameById(@PathVariable Long id) throws IOException{
        logger.info("[GET} /games/id/" + id);
        return gatewayService.getGameById(id);
    }

    @GetMapping("path = /games/genre/{genre}")
    public String getGamesByGenre(@PathVariable String genre) throws IOException {
        logger.info("[GET] /games/genre/" + genre);
        return gatewayService.getGamesByGenre(genre);
    }

    @GetMapping("path = /games/playernum/{num}")
    public String getGamesByPlayerNum(@PathVariable int num) throws IOException {
        logger.info("[GET] /games/playernum/" + num);
        return gatewayService.getGamesByPlayerNum(num);
    }
}
