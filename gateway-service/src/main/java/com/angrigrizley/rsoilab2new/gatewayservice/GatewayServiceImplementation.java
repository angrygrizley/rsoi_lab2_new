package com.angrigrizley.rsoilab2new.gatewayservice;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpVersion;
import org.apache.http.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Base64;


@Service
public class GatewayServiceImplementation implements GatewayService {
    final private String usersServiceUrl = "http://localhost:8070";
    final private String groupsServiceUrl = "http://localhost:8071";
    final private String gamesServiceUrl = "http://localhost:8072";


    private String games_token = "";
    private String users_token = "";
    private String groups_token = "";

    private Jedis jedis = new Jedis("127.0.0.1", 6379);
    private RedisQueue redisQueue = new RedisQueue(jedis);
    private QueueWorker queueWorker = new QueueWorker(jedis);

    public GatewayServiceImplementation() {
        queueWorker.start();
        System.out.println("queue started");
    }

    @Override
    public HttpResponse oauthExecute(HttpUriRequest request, StringBuilder token, String serviceUrl) throws Exception, JSONException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpResponseFactory factory = new DefaultHttpResponseFactory();
        HttpResponse response = factory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_BAD_REQUEST, null), null);

        for (int i = 0; i < 5; i++) {
            request.removeHeaders("Authorization");
            request.addHeader("Authorization", "Bearer " + token.toString());
            try {
                response = httpClient.execute(request);
            } catch (Exception e) {
                System.out.println("Service " + serviceUrl + " unavailable");
                response.setStatusCode(502);
                JSONObject errorObject = new JSONObject();
                errorObject.put("error", serviceUrl + " unavailable");
                response.setEntity(new StringEntity(errorObject.toString()));
            }
            if (response.getStatusLine().getStatusCode() == 401 || response.getStatusLine().getStatusCode() == 403) {
                System.out.println("need new token for " + serviceUrl);
                HttpPost tok_req = new HttpPost(serviceUrl + "/oauth/token?grant_type=client_credentials");

                String creds = Base64.getEncoder().encodeToString(("gateway:secret").getBytes());
                tok_req.addHeader("Authorization", "Basic " + creds);
                HttpResponse tok_res = httpClient.execute(tok_req);

                try {
                    token.delete(0, token.length());
                    JSONObject token_json = new JSONObject(EntityUtils.toString(tok_res.getEntity()));
                    token.append(token_json.getString("access_token"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else
                break;
        }
        return response;
    }

    @Override
    public HttpResponse addUser(String user) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(usersServiceUrl + "/users");

        StringEntity parameters = new StringEntity(user);
        request.addHeader("content-type", "application/json");
        request.setEntity(parameters);

        StringBuilder sb = new StringBuilder(users_token);
        HttpResponse response = oauthExecute(request, sb, usersServiceUrl);
        users_token = sb.toString();

        return response;
    }

    @Override
    public HttpResponse getUsers() throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(usersServiceUrl + "/users");

        StringBuilder sb = new StringBuilder(users_token);
        HttpResponse response = oauthExecute(request, sb, usersServiceUrl);
        users_token = sb.toString();

        return response;
    }

    @Override
    public HttpResponse getUserById(Long id) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(usersServiceUrl + "/users/id/" + id);

        StringBuilder sb = new StringBuilder(users_token);
        HttpResponse response = oauthExecute(request, sb, usersServiceUrl);
        users_token = sb.toString();

        return response;
    }

    @Override
    public HttpResponse addGame(String game) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(gamesServiceUrl + "/games");
        StringEntity parameters = new StringEntity(game);
        request.addHeader("content-type", "application/json");
        request.setEntity(parameters);

        StringBuilder sb = new StringBuilder(games_token);
        HttpResponse response = oauthExecute(request, sb, gamesServiceUrl);
        games_token = sb.toString();

        return response;
    }

    @Override
    public HttpResponse getGames(int page, int size) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(gamesServiceUrl + "/games?page=" + page + "&size=" + size);

        StringBuilder sb = new StringBuilder(games_token);
        HttpResponse response = oauthExecute(request, sb, gamesServiceUrl);
        games_token = sb.toString();

        return response;
    }

    @Override
    public HttpResponse getGameById(Long id) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(gamesServiceUrl + "/games/id/" + id);

        StringBuilder sb = new StringBuilder(games_token);
        HttpResponse response = oauthExecute(request, sb, gamesServiceUrl);
        games_token = sb.toString();

        return response;
    }

    @Override
    public HttpResponse searchGames(String genre, int num) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(gamesServiceUrl + "/games/search?genre=" + genre + "&playernum=" + num);

        StringBuilder sb = new StringBuilder(games_token);
        HttpResponse response = oauthExecute(request, sb, gamesServiceUrl);
        games_token = sb.toString();

        return response;
    }


    @Override
    public HttpResponse addGroup(String group) throws Exception, JSONException {
        JSONObject jo = new JSONObject(group);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet getRequest = new HttpGet(gamesServiceUrl + "/games/id/" + jo.get("gameId"));

        StringBuilder sb = new StringBuilder(games_token);
        HttpResponse response = oauthExecute(getRequest, sb, gamesServiceUrl);
        games_token = sb.toString();


        String game = EntityUtils.toString(response.getEntity());
        JSONObject jgame = new JSONObject(game);
        jgame.put("groupNum", String.valueOf(jgame.get("groupNum")) + 1);

        HttpPut putRequest = new HttpPut(gamesServiceUrl + "/games/edit");
        StringEntity putParameters = new StringEntity(jgame.toString());
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(putParameters);

        sb.delete(0, sb.length());
        sb.append(games_token);
        response = oauthExecute(putRequest, sb, gamesServiceUrl);
        games_token = sb.toString();


        HttpPost request = new HttpPost(groupsServiceUrl + "/groups");
        StringEntity parameters = new StringEntity(group);
        request.addHeader("content-type", "application/json");
        request.setEntity(parameters);


        sb.delete(0, sb.length());
        sb.append(groups_token);
        response = oauthExecute(request, sb, groupsServiceUrl);
        groups_token = sb.toString();


        return response;
    }

    @Override
    public HttpResponse getFreeGroups() throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/free");

        StringBuilder sb = new StringBuilder(groups_token);
        HttpResponse response = oauthExecute(request, sb, groupsServiceUrl);
        groups_token = sb.toString();

        return response;
    }

    @Override
    public HttpResponse getGroupsByGame(Long gameId) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/game?gameId=" + gameId);

        StringBuilder sb = new StringBuilder(groups_token);
        HttpResponse response = oauthExecute(request, sb, groupsServiceUrl);
        groups_token = sb.toString();

        return response;
    }

    @Override
    public HttpResponse getGroups() throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups");

        StringBuilder sb = new StringBuilder(groups_token);
        HttpResponse response = oauthExecute(request, sb, groupsServiceUrl);
        groups_token = sb.toString();

        return response;
    }

    // Деградация
    @Override
    public ResponseEntity getGroupById(Long id) throws Exception, JSONException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/id/" + id);

        StringBuilder sb = new StringBuilder(groups_token);
        HttpResponse response = oauthExecute(request, sb, groupsServiceUrl);
        groups_token = sb.toString();

        JSONObject group = new JSONObject(EntityUtils.toString(response.getEntity()));
        request = new HttpGet(gamesServiceUrl + "/games/id/" + group.get("gameId"));

        sb.delete(0, sb.length());
        sb.append(games_token);
        response = oauthExecute(request, sb, gamesServiceUrl);
        games_token = sb.toString();

        JSONObject game = new JSONObject(EntityUtils.toString(response.getEntity()));
        JSONObject result = new JSONObject();
        result.put("game", game);
        JSONArray placements = group.getJSONArray("players");//new JSONArray(group.get("players"));

        int k = placements.length();
        JSONArray usersList = new JSONArray();
        JSONObject user = new JSONObject();

        for (int i = 0; i < k; i++) {
            int pid = Integer.valueOf(placements.getJSONObject(i).getString("playerId"));

            request = new HttpGet(usersServiceUrl + "/users/id/" + pid);

            sb.delete(0, sb.length());
            sb.append(users_token);
            response = oauthExecute(request, sb, usersServiceUrl);
            users_token = sb.toString();

            user = new JSONObject(EntityUtils.toString(response.getEntity()));
            usersList.put(user);
        }

        result.put("players", usersList);
        result.put("group", group);

        return ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(result.toString());
    }


    @Override
    public HttpResponse deleteGroup(Long id) throws Exception, JSONException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(groupsServiceUrl + "/groups/id/" + id);

        StringBuilder sb = new StringBuilder(groups_token);
        HttpResponse response = oauthExecute(request, sb, groupsServiceUrl);
        groups_token = sb.toString();

        JSONObject group = new JSONObject(EntityUtils.toString(response.getEntity()));

        request = new HttpGet(gamesServiceUrl + "/games/id/" + group.get("gameId"));

        sb.delete(0, sb.length());
        sb.append(games_token);
        response = oauthExecute(request, sb, gamesServiceUrl);
        games_token = sb.toString();

        JSONObject game = new JSONObject(EntityUtils.toString(response.getEntity()));
        game.put("groupNum", String.valueOf(game.getInt("groupNum") - 1));

        HttpPut putRequest = new HttpPut(gamesServiceUrl + "/games/edit");
        StringEntity putParameters = new StringEntity(game.toString());
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(putParameters);

        sb.delete(0, sb.length());
        sb.append(games_token);
        response = oauthExecute(request, sb, gamesServiceUrl);
        games_token = sb.toString();

        JSONArray placements = group.getJSONArray("players");

        int k = placements.length();
        JSONArray usersList = new JSONArray();
        JSONObject user = new JSONObject();

        for (int i = 0; i < k; i++) {
            int pid = Integer.valueOf(placements.getJSONObject(i).getString("playerId"));

            request = new HttpGet(usersServiceUrl + "/users/id/" + pid);

            sb.delete(0, sb.length());
            sb.append(users_token);
            response = oauthExecute(request, sb, usersServiceUrl);
            users_token = sb.toString();

            user = new JSONObject(EntityUtils.toString(response.getEntity()));
            user.put("groupNum", String.valueOf(user.getInt("groupNum") - 1));

            putRequest = new HttpPut(usersServiceUrl + "/users/edit");
            putParameters = new StringEntity(user.toString());
            putRequest.addHeader("content-type", "application/json");
            putRequest.setEntity(putParameters);

            sb.delete(0, sb.length());
            sb.append(users_token);
            response = oauthExecute(request, sb, usersServiceUrl);
            users_token = sb.toString();
        }

        HttpDelete delRequest = new HttpDelete(groupsServiceUrl + "/groups/delete/" + id);

        sb.delete(0, sb.length());
        sb.append(groups_token);
        response = oauthExecute(delRequest, sb, groupsServiceUrl);
        groups_token = sb.toString();
        return response;
    }

    // Откат
    @Override
    public HttpResponse addPlayer(Long userId, Long groupId) throws Exception, JSONException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(groupsServiceUrl + "/groups/id/" + groupId);

        StringBuilder sb = new StringBuilder(groups_token);
        HttpResponse response = oauthExecute(getRequest, sb, groupsServiceUrl);
        groups_token = sb.toString();

        String group = EntityUtils.toString(response.getEntity());
        JSONObject jgroup = new JSONObject(group);
        JSONObject jbackupgroup = jgroup;
        if (Integer.valueOf(jgroup.getString("freeSpace")) == 0)
            return response;

        getRequest = new HttpGet(usersServiceUrl + "/users/id/" + userId);

        sb.delete(0, sb.length());
        sb.append(users_token);
        response = oauthExecute(getRequest, sb, usersServiceUrl);
        users_token = sb.toString();

        String user = EntityUtils.toString(response.getEntity());

        JSONObject juser = new JSONObject(user);
        JSONObject jbackupuser = juser;
        juser.put("groupNum", String.valueOf(juser.getInt("groupNum") + 1));

        HttpPut putRequest = new HttpPut(usersServiceUrl + "/users/edit");
        StringEntity putParameters = new StringEntity(juser.toString());
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(putParameters);

        sb.delete(0, sb.length());
        sb.append(users_token);
        response = oauthExecute(putRequest, sb, usersServiceUrl);
        users_token = sb.toString();


        HttpPost request = new HttpPost(groupsServiceUrl + "/groups/players/add?groupId=" + groupId + "&userId=" + userId);

        sb.delete(0, sb.length());
        sb.append(groups_token);
        HttpResponse response2 = oauthExecute(request, sb, groupsServiceUrl);
        groups_token = sb.toString();


        // Если один из сервисов недоступен
        if (response.getStatusLine().getStatusCode() == 502 || response2.getStatusLine().getStatusCode() == 502) {
            HttpResponse backupResponse;

            HttpPut backupUserRequest = new HttpPut(usersServiceUrl + "/users/edit");
            putRequest.addHeader("content-type", "application/json");
            putRequest.setEntity(new StringEntity(jbackupuser.toString()));

            sb.delete(0, sb.length());
            sb.append(users_token);
            backupResponse = oauthExecute(backupUserRequest, sb, usersServiceUrl);
            users_token = sb.toString();


            HttpPut backupGroupRequest = new HttpPut(groupsServiceUrl + "/groups/edit");
            putRequest.addHeader("content-type", "application/json");
            putRequest.setEntity(new StringEntity(jbackupgroup.toString()));

            sb.delete(0, sb.length());
            sb.append(groups_token);
            backupResponse = oauthExecute(backupGroupRequest, sb, groupsServiceUrl);
            groups_token = sb.toString();
            backupResponse.setStatusCode(500);
            JSONObject backup = new JSONObject();
            backup.put("error", "Some service was unavailable. Rollback was triggered.");
            backupResponse.setEntity(new StringEntity(backup.toString()));

            return backupResponse;
        }

        HttpResponseFactory factory = new DefaultHttpResponseFactory();
        HttpResponse res = factory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_BAD_REQUEST, null), null);

        res.setStatusCode(200);
        JSONObject backup = new JSONObject();
        backup.put("information", "Operation success!");
        res.setEntity(new StringEntity(backup.toString()));

        return res;
    }

    // Очередь
    @Override
    public HttpResponse removePlayer(Long userId, Long groupId) throws Exception, JSONException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(usersServiceUrl + "/users/id/" + userId);

        StringBuilder sb = new StringBuilder(users_token);
        HttpResponse response = oauthExecute(getRequest, sb, usersServiceUrl);
        users_token = sb.toString();

        String user = EntityUtils.toString(response.getEntity());
        JSONObject juser = new JSONObject(user);
        juser.put("groupNum", String.valueOf(juser.getInt("groupNum") - 1));

        HttpDelete request = new HttpDelete(groupsServiceUrl + "/groups/players/remove?groupId=" + groupId +
                "&userId=" + userId);

        redisQueue.addReqToQueue(request);

        /*sb.delete(0, sb.length());
        sb.append(groups_token);
        response = oauthExecute(request, sb, groupsServiceUrl);
        groups_token = sb.toString();*/

        HttpPut putRequest = new HttpPut(usersServiceUrl + "/users/edit");
        StringEntity putParameters = new StringEntity(juser.toString());
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(putParameters);

        /*
        sb.delete(0, sb.length());
        sb.append(users_token);
        response = oauthExecute(putRequest, sb, groupsServiceUrl);
        users_token = sb.toString();
        */

        redisQueue.addReqToQueue(putRequest);

        HttpResponseFactory factory = new DefaultHttpResponseFactory();
        HttpResponse res = factory.newHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, null), null);
        JSONObject jo = new JSONObject();
        jo.put("info", "Request was queued");

        res.setEntity(new StringEntity(jo.toString()));
        return res;
    }

    @Override
    public HttpResponse requestToken(String username, String password, String url, String clientCred) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        url = url + "/oauth/token?grant_type=password&username=" + username + "&password=" + password;

        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", "Basic " + clientCred);

        return httpClient.execute(request);
    }

    @Override
    public ResponseEntity<String> getCode(String client_id, String redirect_uri, String url) throws Exception {
        url = url + "/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + redirect_uri;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", url);
        return new ResponseEntity<String>(headers, org.springframework.http.HttpStatus.FOUND);
    }

    @Override
    public HttpResponse codeToToken(String redirect_uri, String code, String url, String client_cred) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        url = url + "/oauth/token?grant_type=authorization_code&redirect_uri=" + redirect_uri + "&code=" + code;

        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", client_cred);

        return httpClient.execute(request);
    }

    @Override
    public boolean checkToken(String token, String url){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        url = url + "/oauth/check_token?token=" + token;
        boolean token_correct = false;
        HttpGet request = new HttpGet(url);

        String creds = Base64.getEncoder().encodeToString(("gateway:secret").getBytes());
        request.addHeader("Authorization", "Basic " + creds);
        HttpResponse hr;
        try {
            hr = httpClient.execute(request);
            JSONObject token_check = new JSONObject(EntityUtils.toString(hr.getEntity()));
            if (token_check.getBoolean("active")) {
                token_correct = true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return token_correct;
    }

}
