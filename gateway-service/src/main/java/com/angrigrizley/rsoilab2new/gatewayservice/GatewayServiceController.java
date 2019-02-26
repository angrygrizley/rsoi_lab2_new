package com.angrigrizley.rsoilab2new.gatewayservice;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
public class GatewayServiceController {
    private final GatewayService gatewayService;
    private Logger logger;
    private String authServiceUrl = "http://localhost:8090";



    @Autowired
    public GatewayServiceController(GatewayService gatewayService) {
        logger = LoggerFactory.getLogger(GatewayServiceController.class);
        this.gatewayService = gatewayService;
    }


    @GetMapping(path = "/login")
    public ResponseEntity login(@RequestParam(value = "username") String username,
                                @RequestParam(value = "password") String password,
                                @RequestHeader("Authorization") String clientCred) throws IOException {

        logger.info("[GET] /login");
        clientCred = clientCred.replace("Basic", "");
        HttpResponse hr = gatewayService.requestToken(username, password, authServiceUrl, clientCred);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @GetMapping(path = "/oauth/getcode")
    public ResponseEntity getCode(@RequestParam(value = "client_id") String client_id,
                                  @RequestParam(value = "redirect_uri") String redirect_uri) throws IOException {
        logger.info("[GET] /oauth/getcode");

        return gatewayService.getCode(client_id, redirect_uri, authServiceUrl);
    }

    @GetMapping(path = "/oauth/codetotoken")
    public ResponseEntity codeToToken(@RequestParam(value="redirect_uri") String redirect_uri,
                                      @RequestParam(value="code") String code,
                                      @RequestHeader("Authorization") String clientCred) throws IOException {
        logger.info("[GET} /oauth/codetotoken");
        HttpResponse hr =  gatewayService.codeToToken(redirect_uri, code, authServiceUrl, clientCred);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @PostMapping(path="/users")
    public String addUser(@RequestBody String user,
                          @RequestHeader("Authorization") String token) throws IOException {
        logger.info("[POST] /users" + user + ", token = " + token);

        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";

        return gatewayService.addUser(user);
    }

    @GetMapping(path="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUsers(@RequestHeader("Authorization") String token) throws IOException {
        logger.info("[GET] /users, token = " + token);

        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";

        return gatewayService.getUsers();
    }

    @GetMapping(path="/users/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserById(@PathVariable Long id,
                              @RequestHeader("Authorization") String token) throws IOException {
        logger.info("[GET] /users/id/" + id + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";

        return gatewayService.getUserById(id);
    }

    @PostMapping(path = "/games")
    public String addGame(@RequestBody String game,
                          @RequestHeader("Authorization") String token) throws IOException {
        logger.info("[POST] /games" + game + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";
        return gatewayService.addGame(game);
    }

    @GetMapping(path = "/games")
    public String getGames(@RequestParam (value = "page", required = false, defaultValue = "0") int page,
                           @RequestParam (value = "size", required = false, defaultValue = "5") int size,
                           @RequestHeader("Authorization") String token) throws IOException {

        logger.info("[GET] /games, token = " + token);

        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";

        return gatewayService.getGames(page, size);
    }

    @GetMapping(path = "/games/id/{id}")
    public String getGameById(@PathVariable Long id,
                              @RequestHeader("Authorization") String token) throws IOException{
        logger.info("[GET} /games/id/" + id + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";
        return gatewayService.getGameById(id);
    }

    @GetMapping(path = "/games/search")
    public String searchGames(@RequestParam (value = "genre", required = false, defaultValue = "") String genre,
                              @RequestParam (value = "playernum", required = false, defaultValue = "-1") int num,
                              @RequestHeader("Authorization") String token) throws IOException {
        logger.info("[GET] /games/search genre:" + genre + " playernum:" + num + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";
        return gatewayService.searchGames(genre, num);
    }


    @PostMapping(path = "/groups")
    public String addGroup(@RequestBody String group,
                           @RequestHeader("Authorization") String token) throws IOException, JSONException {
        logger.info("[POST] /groups"+group + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";
        return gatewayService.addGroup(group);
    }

    @DeleteMapping(path = "/groups/delete/{id}")
    public boolean deleteGroup(@PathVariable Long id,
                               @RequestHeader("Authorization") String token) throws IOException, JSONException {
        logger.info("[DELETE] /groups/delete/" + id + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return false;
        return gatewayService.deleteGroup(id);
    }

    @GetMapping(path = "/groups")
    public String getGroups(@RequestHeader("Authorization") String token) throws IOException {
        logger.info("[GET] /groups, token=" + token);

        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";

        return gatewayService.getGroups();
    }

    @GetMapping(path="/groups/id/{id}")
    public String getGroupdById(@PathVariable Long id,
                                @RequestHeader("Authorization") String token) throws IOException, JSONException {
        logger.info("[GET] /groups/id" + id + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";
        return gatewayService.getGroupById(id);
    }

    @GetMapping(path = "/groups/free")
    public String getFreeGroups(@RequestHeader("Authorization") String token) throws IOException {
        logger.info("[GET] /groups/free, token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";
        return gatewayService.getFreeGroups();
    }

    @GetMapping(path = "/groups/game/{gameId}")
    public String getGroupsByGame(@PathVariable Long gameId,
                                  @RequestHeader("Authorization") String token) throws  IOException {
        logger.info("[GET] /groups/game/" + gameId + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";
        return gatewayService.getGroupsByGame(gameId);
    }

    @PostMapping(path = "/groups/players/add")
    public String addPlayer(@RequestParam (value = "userid") Long userId,
                            @RequestParam (value = "groupid") Long groupId,
                            @RequestHeader("Authorization") String token) throws IOException, JSONException {
        logger.info("[POST] /groups/players/add" + userId + " to group " + groupId + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return "Invalid token";
        return gatewayService.addPlayer(userId, groupId);
    }

    @DeleteMapping(path = "/groups/players/remove")
    public boolean removePlayer(@RequestParam (value = "userid") Long userId,
                                @RequestParam (value = "groupid") Long groupId,
                                @RequestHeader("Authorization") String token) throws IOException, JSONException {
        logger.info("[POST] /groups/players/remove" + userId + " from group " + groupId + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return false;
        return gatewayService.removePlayer(userId, groupId);
    }


}
