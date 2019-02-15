package com.angrigrizley.rsoilab2new.gatewayservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GatewayServiceImplementation implements GatewayService {
    final private String usersServiceUrl = "http://localhost:8070";

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
}