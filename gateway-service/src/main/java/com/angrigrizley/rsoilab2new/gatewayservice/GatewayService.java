package com.angrigrizley.rsoilab2new.gatewayservice;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.JSONException;
import org.springframework.data.domain.PageRequest;
import java.io.IOException;

public interface GatewayService  {
    //user service
    void addUser(String user) throws IOException;
    String getUsers() throws IOException;
    String getUserById(Long id) throws IOException;

    //game service
    void addGame(String game) throws IOException;
    String getGames() throws IOException;
    String getGameById(Long id) throws IOException;
    String getGamesByGenre(String genre) throws IOException;
    String getGamesByPlayerNum(int num) throws IOException;

    //group + game service
    void addGroup(String group) throws IOException;
    String getFreeGroups() throws IOException;
    String getGroupsByGame(Long gameId) throws IOException;
    String getGroups() throws IOException;

    //group + user service
    String getGroupById(Long id) throws IOException;
    void deleteGroup(Long id) throws IOException;

    //group + game + user service
    void addPlayer(Long userId, Long groupId) throws IOException;
    void removePlayer(Long userId, Long groupId) throws IOException;

}
