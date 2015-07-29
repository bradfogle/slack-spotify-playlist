package com.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrapper.spotify.models.PlaylistTrack;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Helper class built on the Jedis project for persisting and retrieving the playlist as a set.
 */
public class RedisClient {

    private Jedis client;
    private JedisPool pool;

    public RedisClient() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(1);
            this.pool = new JedisPool(config, new URI(System.getenv("REDISTOGO_URL")));
        } catch (URISyntaxException e) {
            System.out.println("Error parsing Redis URI: " + e.getMessage());
        }
    }

    public List<String> getRedisPlaylist() {
        List<String> playlistTrackList;

        try {
            client = pool.getResource();
            Set<String> playlistSet = client.smembers("playlist");
            playlistTrackList = new ArrayList<String>(playlistSet);
        } finally {
            if(client != null) {
                client.close();
            }
        }

        return playlistTrackList;
    }

    public void addNewTrack(PlaylistTrack track) throws Exception {
        try {
            client = pool.getResource();
            StringWriter playlistJson = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(playlistJson, track);
            client.sadd("playlist", playlistJson.toString());
        } finally {
            if(client != null) {
                client.close();
            }
        }
    }
}
