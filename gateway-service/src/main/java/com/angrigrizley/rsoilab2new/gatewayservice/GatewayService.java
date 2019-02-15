package com.angrigrizley.rsoilab2new.gatewayservice;

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
}
