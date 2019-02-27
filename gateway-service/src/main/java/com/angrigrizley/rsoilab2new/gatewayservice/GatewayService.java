package com.angrigrizley.rsoilab2new.gatewayservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;


public interface GatewayService  {
    //user service
    HttpResponse addUser(String user) throws Exception;
    HttpResponse getUsers() throws Exception;
    HttpResponse getUserById(Long id) throws Exception;

    //game service
    HttpResponse addGame(String game) throws Exception;
    HttpResponse getGames(int page, int size) throws Exception;
    HttpResponse getGameById(Long id) throws Exception;
    HttpResponse searchGames(String genre, int num) throws Exception;

    //group + game service
    HttpResponse addGroup(String group) throws Exception;
    HttpResponse getFreeGroups() throws Exception;
    HttpResponse getGroupsByGame(Long gameId) throws Exception;
    HttpResponse getGroups() throws Exception;

    //group + user service
    ResponseEntity getGroupById(Long id) throws Exception;
    HttpResponse deleteGroup(Long id) throws Exception;

    //group + game + user service
    HttpResponse addPlayer(Long userId, Long groupId) throws Exception;
    HttpResponse removePlayer(Long userId, Long groupId) throws Exception;

    HttpResponse requestToken(String username, String password, String url, String clientCred) throws Exception;
    ResponseEntity<String> getCode(String client_id, String redirect_uri, String url) throws Exception;
    HttpResponse codeToToken(String redirect_uri, String code, String url, String client_cred) throws Exception;

    boolean checkToken(String token, String url) throws Exception;
    HttpResponse oauthExecute(HttpUriRequest request, StringBuilder token, String serviceUrl) throws Exception;
}
