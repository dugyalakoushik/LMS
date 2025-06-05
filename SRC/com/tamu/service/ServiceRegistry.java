package com.tamu.service;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.Gson;
import com.tamu.dao.Config;

import redis.clients.jedis.Jedis;

public class ServiceRegistry {

    private static Jedis jedis;
    private static Gson gson;
    static {
		Properties properties = Config.loadProperties();

		String redisPort = properties.getProperty("redis.serviceRegistry.port");
		String redisPassword = properties.getProperty("redis.serviceRegistry.password");
//    	this.jedis = new Jedis("redis://default:rBYWOfDmQKCHv0Ji3TUCTC68wgjHJ89V@redis-12923.c10.us-east-1-2.ec2.cloud.redislabs.com:12923");

		jedis = new Jedis("redis://default:" + redisPassword +  "@redis-12923.c10.us-east-1-2.ec2.cloud.redislabs.com:" + redisPort);
        System.out.println("Reddis Connection Service Registry");
		gson = new Gson();

    };
    
    public static void registerService(String serviceName, int port) {
		jedis.set("service:" + serviceName, Integer.toString(port));

    }

    public static int getServicePort(String serviceName) {
    	String portId = jedis.get("service:" + serviceName);
    	return Integer.parseInt(portId);
    }
}
