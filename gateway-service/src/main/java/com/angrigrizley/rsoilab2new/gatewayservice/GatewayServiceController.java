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
}
