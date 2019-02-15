package com.angrigrizley.rsoilab2new.gatewayservice;

import org.json.JSONException;
import org.springframework.data.domain.PageRequest;
import java.io.IOException;

public interface GatewayService  {
    void addUser(String user) throws IOException;
    String getUsers() throws IOException;
    String getUserById(Long id) throws IOException;
}
