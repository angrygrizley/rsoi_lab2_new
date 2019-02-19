package com.angrigrizley.rsoilab2new.gatewayservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
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
    public String addUser(String user) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(usersServiceUrl + "/users");
        StringEntity parameters = new StringEntity(user);
        request.addHeader("content-type", "application/json");
        request.setEntity(parameters);
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
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
    public String addGame(String game) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(gamesServiceUrl + "/games");
        StringEntity parameters = new StringEntity(game);
        request.addHeader("content-type", "application/json");
        request.setEntity(parameters);
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @Override
    public String getGames(int page, int size) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(gamesServiceUrl + "/games?page="+page+"&size="+size);
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
    public String searchGames(String genre, int num) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(gamesServiceUrl + "/games/search?genre="+genre+"&playernum="+num);
        HttpResponse response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }


    @Override
    public String addGroup(String group) throws IOException, JSONException {
        JSONObject jo = new JSONObject(group);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet getRequest = new HttpGet(gamesServiceUrl + "/games/id/" + jo.get("gameId"));
        HttpResponse response = httpClient.execute(getRequest);
        String game = EntityUtils.toString(response.getEntity());
        JSONObject jgame = new JSONObject(game);
        jgame.put("groupNum", String.valueOf(jgame.get("groupNum"))+1);

        HttpPut putRequest = new HttpPut(gamesServiceUrl+ "/games/edit");
        StringEntity putParameters = new StringEntity(jgame.toString());
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(putParameters);
        httpClient.execute(putRequest);

        HttpPost request = new HttpPost(groupsServiceUrl + "/groups");
        StringEntity parameters = new StringEntity(group);
        request.addHeader("content-type", "application/json");
        request.setEntity(parameters);
        response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
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
    public String getGroupById(Long id) throws IOException, JSONException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/id/" + id);
        HttpResponse response = httpClient.execute(request);
        JSONObject group = new JSONObject(EntityUtils.toString(response.getEntity()));

        request = new HttpGet(gamesServiceUrl + "/games/id/" + group.get("gameId"));
        response = httpClient.execute(request);
        JSONObject game = new JSONObject(EntityUtils.toString(response.getEntity()));

        JSONObject result = new JSONObject();
        result.put("game", game);

        JSONArray placements = new JSONArray(group.get("players"));

        int k = placements.length();
        JSONArray usersList = new JSONArray();
        JSONObject user = new JSONObject();

        for (int i = 0; i < k; i++) {
            int pid = Integer.valueOf(placements.getJSONObject(i).getString("playerId"));

            request = new HttpGet(usersServiceUrl + "/users/id/" + pid);
            response = httpClient.execute(request);
            user = new JSONObject(EntityUtils.toString(response.getEntity()));
            usersList.put(user);
        }

        result.put("players", usersList);
        result.put("group", group);
        return result.toString();
    }

    @Override
    public boolean deleteGroup(Long id) throws IOException, JSONException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/id/" + id);
        HttpResponse response = httpClient.execute(request);
        JSONObject group = new JSONObject(EntityUtils.toString(response.getEntity()));

        request = new HttpGet(gamesServiceUrl + "/games/id/" + group.get("gameId"));
        response = httpClient.execute(request);
        JSONObject game = new JSONObject(EntityUtils.toString(response.getEntity()));
        game.put("groupNum", String.valueOf(game.getInt("groupNum")-1));

        HttpPut putRequest = new HttpPut(gamesServiceUrl + "/games/edit");
        StringEntity putParameters = new StringEntity(game.toString());
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(putParameters);
        httpClient.execute(putRequest);

        JSONArray placements = new JSONArray(group.get("players"));

        int k = placements.length();
        JSONArray usersList = new JSONArray();
        JSONObject user = new JSONObject();

        for (int i = 0; i < k; i++) {
            int pid = Integer.valueOf(placements.getJSONObject(i).getString("playerId"));

            request = new HttpGet(usersServiceUrl + "/users/id/" + pid);
            response = httpClient.execute(request);

            user = new JSONObject(EntityUtils.toString(response.getEntity()));
            user.put("groupNum", String.valueOf(user.getInt("groupNum")-1));

            putRequest = new HttpPut(usersServiceUrl + "users/edit");
            putParameters = new StringEntity(user.toString());
            putRequest.addHeader("content-type", "application/json");
            putRequest.setEntity(putParameters);
            httpClient.execute(putRequest);
        }

        HttpDelete delRequest = new HttpDelete(groupsServiceUrl + "groups/delete/" + group.get("gameId"));
        httpClient.execute(delRequest);
        return true;
    }

    @Override
    public String addPlayer(Long userId, Long groupId) throws IOException, JSONException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(groupsServiceUrl + "/groups/id/" + groupId);
        HttpResponse response = httpClient.execute(getRequest);
        String group = EntityUtils.toString(response.getEntity());
        JSONObject jgroup = new JSONObject(group);
        if (Integer.valueOf(jgroup.getString("freeSpace"))==0)
            return "";

        getRequest = new HttpGet(usersServiceUrl + "/users/id/" + userId);
        response = httpClient.execute(getRequest);

        String user = EntityUtils.toString(response.getEntity());
        JSONObject juser = new JSONObject(user);
        juser.put("groupNum", String.valueOf(juser.getInt("groupNum")+1));

        HttpPut putRequest = new HttpPut(usersServiceUrl + "/users/edit");
        StringEntity putParameters = new StringEntity(juser.toString());
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(putParameters);
        httpClient.execute(putRequest);

        HttpPost request = new HttpPost(groupsServiceUrl + "/groups/players/add?groupId="+groupId+"&userId="+userId);
        response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());

    }

    @Override
    public boolean removePlayer(Long userId, Long groupId) throws IOException, JSONException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(usersServiceUrl + "/users/id/" + userId);
        HttpResponse response = httpClient.execute(getRequest);
        String user = EntityUtils.toString(response.getEntity());
        JSONObject juser = new JSONObject(user);
        juser.put("groupNum", String.valueOf(juser.getInt("groupNum")-1));

        HttpPut putRequest = new HttpPut(usersServiceUrl + "/users/edit");
        StringEntity putParameters = new StringEntity(juser.toString());
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(putParameters);
        httpClient.execute(putRequest);

        HttpDelete request = new HttpDelete(groupsServiceUrl + "/groups/players/remove?groupId=" + groupId +
                "&userId=" + userId);
        httpClient.execute(request);
        return true;
    }
}
