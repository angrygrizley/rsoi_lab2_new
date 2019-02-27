package com.angrigrizley.rsoilab2new.gatewayservice;

import org.apache.http.HttpResponse;

import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
                                @RequestHeader("Authorization") String clientCred) throws Exception {

        logger.info("[GET] /login");
        clientCred = clientCred.replace("Basic", "");
        HttpResponse hr = gatewayService.requestToken(username, password, authServiceUrl, clientCred);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @GetMapping(path = "/oauth/getcode")
    public ResponseEntity getCode(@RequestParam(value = "client_id") String client_id,
                                  @RequestParam(value = "redirect_uri") String redirect_uri) throws Exception {
        logger.info("[GET] /oauth/getcode");

        return gatewayService.getCode(client_id, redirect_uri, authServiceUrl);
    }

    @GetMapping(path = "/oauth/codetotoken")
    public ResponseEntity codeToToken(@RequestParam(value="redirect_uri") String redirect_uri,
                                      @RequestParam(value="code") String code,
                                      @RequestHeader("Authorization") String clientCred) throws Exception {
        logger.info("[GET} /oauth/codetotoken");
        HttpResponse hr =  gatewayService.codeToToken(redirect_uri, code, authServiceUrl, clientCred);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @PostMapping(path="/users")
    public ResponseEntity addUser(@RequestBody String user,
                                  @RequestHeader("Authorization") String token) throws Exception {
        logger.info("[POST] /users" + user + ", token = " + token);

        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");
        HttpResponse hr = gatewayService.addUser(user);

        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @GetMapping(path="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsers(@RequestHeader("Authorization") String token) throws Exception {
        logger.info("[GET] /users, token = " + token);

        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");

        HttpResponse hr = gatewayService.getUsers();
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @GetMapping(path="/users/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable Long id,
                              @RequestHeader("Authorization") String token) throws Exception {
        logger.info("[GET] /users/id/" + id + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");

        HttpResponse hr = gatewayService.getUserById(id);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @PostMapping(path = "/games")
    public ResponseEntity addGame(@RequestBody String game,
                          @RequestHeader("Authorization") String token) throws Exception {
        logger.info("[POST] /games" + game + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");

        HttpResponse hr = gatewayService.addGame(game);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @GetMapping(path = "/games")
    public ResponseEntity getGames(@RequestParam (value = "page", required = false, defaultValue = "0") int page,
                           @RequestParam (value = "size", required = false, defaultValue = "5") int size,
                           @RequestHeader("Authorization") String token) throws Exception {

        logger.info("[GET] /games, token = " + token);

        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");

        HttpResponse hr = gatewayService.getGames(page, size);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @GetMapping(path = "/games/id/{id}")
    public ResponseEntity getGameById(@PathVariable Long id,
                              @RequestHeader("Authorization") String token) throws Exception{
        logger.info("[GET} /games/id/" + id + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");
        HttpResponse hr = gatewayService.getGameById(id);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @GetMapping(path = "/games/search")
    public ResponseEntity searchGames(@RequestParam (value = "genre", required = false, defaultValue = "") String genre,
                              @RequestParam (value = "playernum", required = false, defaultValue = "-1") int num,
                              @RequestHeader("Authorization") String token) throws Exception {
        logger.info("[GET] /games/search genre:" + genre + " playernum:" + num + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");
        HttpResponse hr = gatewayService.searchGames(genre, num);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }


    @PostMapping(path = "/groups")
    public ResponseEntity addGroup(@RequestBody String group,
                           @RequestHeader("Authorization") String token) throws Exception, JSONException {
        logger.info("[POST] /groups"+group + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");
        HttpResponse hr = gatewayService.addGroup(group);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @DeleteMapping(path = "/groups/delete/{id}")
    public ResponseEntity deleteGroup(@PathVariable Long id,
                               @RequestHeader("Authorization") String token) throws Exception, JSONException {
        logger.info("[DELETE] /groups/delete/" + id + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");
        HttpResponse hr = gatewayService.deleteGroup(id);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @GetMapping(path = "/groups")
    public ResponseEntity getGroups(@RequestHeader("Authorization") String token) throws Exception {
        logger.info("[GET] /groups, token=" + token);

        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");
        HttpResponse hr = gatewayService.getGroups();
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @GetMapping(path="/groups/id/{id}")
    public ResponseEntity getGroupdById(@PathVariable Long id,
                                @RequestHeader("Authorization") String token) throws Exception, JSONException {
        logger.info("[GET] /groups/id" + id + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");
        return gatewayService.getGroupById(id);
    }

    @GetMapping(path = "/groups/free")
    public ResponseEntity getFreeGroups(@RequestHeader("Authorization") String token) throws Exception {
        logger.info("[GET] /groups/free, token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");
        HttpResponse hr = gatewayService.getFreeGroups();
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @GetMapping(path = "/groups/game/{gameId}")
    public ResponseEntity getGroupsByGame(@PathVariable Long gameId,
                                  @RequestHeader("Authorization") String token) throws  Exception {
        logger.info("[GET] /groups/game/" + gameId + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");
        HttpResponse hr = gatewayService.getGroupsByGame(gameId);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @PostMapping(path = "/groups/players/add")
    public ResponseEntity addPlayer(@RequestParam (value = "userid") Long userId,
                            @RequestParam (value = "groupid") Long groupId,
                            @RequestHeader("Authorization") String token) throws Exception, JSONException {
        logger.info("[POST] /groups/players/add" + userId + " to group " + groupId + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");
        HttpResponse hr = gatewayService.addPlayer(userId, groupId);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }

    @DeleteMapping(path = "/groups/players/remove")
    public ResponseEntity removePlayer(@RequestParam (value = "userid") Long userId,
                                @RequestParam (value = "groupid") Long groupId,
                                @RequestHeader("Authorization") String token) throws Exception, JSONException {
        logger.info("[POST] /groups/players/remove" + userId + " from group " + groupId + ", token = " + token);
        token = token.replace("Bearer ", "");
        if (!gatewayService.checkToken(token, authServiceUrl))
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).body("Invalid token");
        HttpResponse hr = gatewayService.removePlayer(userId, groupId);
        return ResponseEntity.status(hr.getStatusLine().getStatusCode()).body(EntityUtils.toString(hr.getEntity()));
    }


}
