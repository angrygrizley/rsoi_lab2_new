package com.angrigrizley.rsoilab2new.gatewayservice;

import org.json.JSONException;

import java.io.IOException;

public interface GatewayService  {
    //user service
    String addUser(String user) throws IOException;
    String getUsers() throws IOException;
    String getUserById(Long id) throws IOException;

    //game service
    String addGame(String game) throws IOException;
    String getGames() throws IOException;
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

}
