package com.angrigrizley.rsoilab2new.gatewayservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface GatewayService  {
    //user service
    String addUser(String user) throws IOException;
    String getUsers() throws IOException;
    String getUserById(Long id) throws IOException;

    //game service
    String addGame(String game) throws IOException;
    String getGames(int page, int size) throws IOException;
    String getGameById(Long id) throws IOException;
    String searchGames(String genre, int num) throws IOException;

    //group + game service
    String addGroup(String group) throws IOException, JSONException;
    String getFreeGroups() throws IOException;
    String getGroupsByGame(Long gameId) throws IOException;
    String getGroups() throws IOException;

    //group + user service
    String getGroupById(Long id) throws IOException, JSONException;
    boolean deleteGroup(Long id) throws IOException, JSONException;

    //group + game + user service
    String addPlayer(Long userId, Long groupId) throws IOException, JSONException;
    boolean removePlayer(Long userId, Long groupId) throws IOException, JSONException;

    HttpResponse requestToken(String username, String password, String url, String clientCred) throws IOException;
    ResponseEntity<String> getCode(String client_id, String redirect_uri, String url) throws IOException;
    HttpResponse codeToToken(String redirect_uri, String code, String url, String client_cred) throws IOException;

    boolean checkToken(String token, String url) throws IOException;
    HttpResponse oauthExecute(HttpUriRequest request, StringBuilder token, String serviceUrl) throws IOException;
}
