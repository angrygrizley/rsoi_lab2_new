package com.angrigrizley.rsoilab2new.gatewayservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.net.MalformedURLException;
import java.util.Base64;
import java.net.URL;

public class QueueWorker implements Runnable {
    protected Jedis queue = null;
    Thread thread;

    public QueueWorker(Jedis queue) {
        this.queue = queue;
        this.thread = new Thread(this);
    }
    public void start() {
        this.thread.start();
    }

    public void run() {
        while (true) {
            while (queue.exists("get")) {
                System.out.println("get req");
                JSONObject req = null;
                try {
                    req = new JSONObject(queue.rpop("get"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CloseableHttpResponse httpResponse = null;
                try {
                    CloseableHttpClient httpClient = HttpClients.createDefault();


                    String url = null;
                    try {
                        url = req.getString("url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpGet request = new HttpGet(url);

                    String t = this.getToken(new URL(new URL(url.toString()), "/").toString());

                    request.addHeader("Authorization", "Bearer " + t);
                    httpResponse = httpClient.execute(request);

                } catch (Exception e) {
                    //e.printStackTrace();
               //     if (httpResponse.getStatusLine().getStatusCode() != 200 || httpResponse == null) {
                        queue.lpush("get", req.toString());
                 //   }
                }
            }
            while (queue.exists("put")) {
                System.out.println("put req");
                JSONObject req = null;
                try {
                    req = new JSONObject(queue.rpop("put"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CloseableHttpResponse httpResponse = null;
                try {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    String url = null;
                    String body = "";
                    try {
                        url = req.getString("url");
                        body = req.getString("body");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    HttpPut request = new HttpPut(url);
                    request.setEntity(new StringEntity(body));

                    String t = this.getToken(new URL(new URL(url.toString()), "/").toString());

                    request.addHeader("Authorization", "Bearer " + t);
                    httpResponse = httpClient.execute(request);

                } catch (Exception e) {
                    //e.printStackTrace();
                 //   if (httpResponse.getStatusLine().getStatusCode() != 200 || httpResponse == null) {
                        queue.lpush("put", req.toString());
                 //   }
                }
            }
            while (queue.exists("post")) {
                System.out.println("post req");
                JSONObject req = null;
                try {
                    req = new JSONObject(queue.rpop("push"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CloseableHttpResponse httpResponse = null;
                try {
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    String url = null;
                    String body = "";
                    try {
                        url = req.getString("url");
                        body = req.getString("body");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    HttpPost request = new HttpPost(url);
                    request.setEntity(new StringEntity(body));

                    String t = this.getToken(new URL(new URL(url.toString()), "/").toString());

                    request.addHeader("Authorization", "Bearer " + t);
                    httpResponse = httpClient.execute(request);

                } catch (Exception e) {
                    //e.printStackTrace();
                   // if (httpResponse.getStatusLine().getStatusCode() != 200 || httpResponse == null) {
                        queue.lpush("post", req.toString());
                   // }
                }
            }
            while (queue.exists("delete")) {
                System.out.println("delete req");
                JSONObject req = null;
                try {
                    req = new JSONObject(queue.rpop("delete"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CloseableHttpResponse httpResponse = null;
                try {
                    CloseableHttpClient httpClient = HttpClients.createDefault();


                    String url = null;
                    try {
                        url = req.getString("url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpDelete request = new HttpDelete(url);

                    String t = this.getToken(new URL(new URL(url.toString()), "/").toString());

                    request.addHeader("Authorization", "Bearer " + t);
                    httpResponse = httpClient.execute(request);

                } catch (Exception e) {
                   // e.printStackTrace();
                   // if (httpResponse.getStatusLine().getStatusCode() != 200 || httpResponse == null) {
                        queue.lpush("delete", req.toString());
                    //}
                }
            }

            try {
                System.out.println("no more works. Wait.");
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String getToken(String url) {
        String t = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url + "/oauth/token?grant_type=client_credentials");
        request.addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(("gateway:secret").getBytes()));
        try {
            HttpResponse hr = httpClient.execute(request);
            JSONObject p = new JSONObject(EntityUtils.toString(hr.getEntity()));
            t = p.getString("access_token");
        } catch (Exception e) {
            t = "";
        }
        return t;
    }

}
