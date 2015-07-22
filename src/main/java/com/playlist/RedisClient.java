package com.playlist;

import com.wrapper.spotify.models.PlaylistTrack;
import redis.clients.jedis.Jedis;

import java.util.Date;

/**
 * User: bradfogle
 * Date: 7/21/15
 * Time: 1:12 PM
 */
public class RedisClient {

    private final String redisHost = "";
    private Jedis client;

    private Date lastUpdatedTime = new Date(1432240043L);

    public RedisClient() {
        this.client = new Jedis(redisHost);
    }

    public void saveLastUpdatedTime(Date lastUpdatedTime) {
//        client.set("last_updated", lastUpdatedTime.toString());
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public Date getLastUpdatedTime() {
//        return new Date(new Long(client.get("last_updated")));
        return lastUpdatedTime;
    }
}
