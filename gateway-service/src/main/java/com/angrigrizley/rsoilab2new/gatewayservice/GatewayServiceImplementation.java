package com.angrigrizley.rsoilab2new.gatewayservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GatewayServiceImplementation implements GatewayService {
    final private String usersServiceUrl = "http://localhost:8070";
    final private String groupsServiceUrl = "http://localhost:8071";
    final private String gamesServiceUrl = "http://localhost:8072";

    @Override
    public void addUser(String user) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(usersServiceUrl + "/users");
        StringEntity parameters = new StringEntity(user);
        request.addHeader("content-type", "application/json");
        request.setEntity(parameters);
        httpClient.execute(request);
    }

    @Override
    public String getUsers() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(usersServiceUrl + "/users");
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getUserById(Long id) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(usersServiceUrl + "/users/id/"+id);
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public void addGame(String game) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(gamesServiceUrl + "/games");
        StringEntity parameters = new StringEntity(game);
        request.addHeader("content-type", "application/json");
        request.setEntity(parameters);
        httpClient.execute(request);
    }

    @Override
    public String getGames() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(gamesServiceUrl + "/games");
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getGameById(Long id) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(gamesServiceUrl + "/games/id"+id);
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getGamesByGenre(String genre) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(gamesServiceUrl + "/games/genre/"+genre);
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getGamesByPlayerNum(int num) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(gamesServiceUrl + "/games/playernum/"+num);
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public void addGroup(String group) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(groupsServiceUrl + "/groups");
        StringEntity parameters = new StringEntity(group);
        request.addHeader("content-type", "application/json");
        request.setEntity(parameters);
        httpClient.execute(request);
    }

    @Override
    public String getFreeGroups() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/free");
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getGroupsByGame(Long gameId) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/game/" + gameId);
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getGroups() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups");
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getGroupById(Long id) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/id/" + id);
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public void deleteGroup(Long id) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/id/" + id);
        HttpResponse response = httpClient.execute(request);
       /* try {
            JSONObject group = new JSONObject(EntityUtils.toString(response.getEntity()));
            group.getLong("game_id");
            
            HttpPut putrequest = new HttpPut(usersServiceUrl+"/users/edit");
            putrequest.setEntity(new StringEntity(user.toString()));
            response = httpClient.execute(putrequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/delete/" + id);
        HttpResponse response = httpClient.execute(request);*/

    }

    @Override
    public void addPlayer(Long userId, Long groupId) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/players/add?groupid="+groupId+"?userid="+userId);
        HttpResponse response = httpClient.execute(request);
        request = new HttpGet(usersServiceUrl + "/users/id/"+ userId);
        response = httpClient.execute(request);
        try {
            JSONObject user = new JSONObject(EntityUtils.toString(response.getEntity()));
            user.put("group_num", user.getInt("group_num")+1);
            HttpPut putrequest = new HttpPut(usersServiceUrl+"/users/edit");
            putrequest.setEntity(new StringEntity(user.toString()));
            response = httpClient.execute(putrequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removePlayer(Long userId, Long groupId) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/players/remove?groupid="+groupId+"?userid="+userId);
        HttpResponse response = httpClient.execute(request);
        request = new HttpGet(usersServiceUrl + "/users/id/"+ userId);
        response = httpClient.execute(request);
        try {
            JSONObject user = new JSONObject(EntityUtils.toString(response.getEntity()));
            user.put("group_num", user.getInt("group_num")-1);
            HttpPut putrequest = new HttpPut(usersServiceUrl+"/users/edit");
            putrequest.setEntity(new StringEntity(user.toString()));
            response = httpClient.execute(putrequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
