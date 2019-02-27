package com.angrigrizley.rsoilab2new.gatewayservice;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.*;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

public class RedisQueue {
    private Jedis bq = null;

    public void setBlockingQueue(Jedis bq) {
        this.bq = bq;
    }

    public RedisQueue(Jedis j) {
        this.setBlockingQueue(j);
    }

    public void addReqToQueue(HttpPut req) throws InterruptedException {
        JSONObject object = new JSONObject();

        try {
            object.put("url", req.getRequestLine().getUri());
            object.put("body", EntityUtils.toString((req).getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        bq.lpush("put", object.toString());
    }

    public void addReqToQueue(HttpGet req) throws InterruptedException {
        JSONObject object = new JSONObject();

        try {
            object.put("url", req.getRequestLine().getUri());
        } catch (Exception e) {
            e.printStackTrace();
        }

        bq.lpush("get", object.toString());
    }

    public void addReqToQueue(HttpPost req) throws InterruptedException {
        JSONObject object = new JSONObject();

        try {
            object.put("url", req.getRequestLine().getUri());
            object.put("body", EntityUtils.toString((req).getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        bq.lpush("post", object.toString());
    }
    public void addReqToQueue(HttpDelete req) throws InterruptedException {
        JSONObject object = new JSONObject();

        try {
            object.put("url", req.getRequestLine().getUri());
        } catch (Exception e) {
            e.printStackTrace();
        }

        bq.lpush("delete", object.toString());
    }

}