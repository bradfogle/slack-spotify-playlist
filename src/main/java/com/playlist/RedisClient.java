package com.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrapper.spotify.models.PlaylistTrack;
import redis.clients.jedis.Jedis;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Helper class built on the Jedis project for persisting and retrieving the playlist as a set.
 */
public class RedisClient {

    private final String redisHost = System.getenv("REDIS_HOST");
    private Jedis client;

    public RedisClient() {
        this.client = new Jedis(redisHost);
    }

    public List<String> getRedisPlaylist() {
        Set<String> playlistSet = client.smembers("playlist");
        List<String> playlistTrackList = new ArrayList<String>(playlistSet);
        return playlistTrackList;
    }

    public void addNewTrack(PlaylistTrack track) throws Exception {
        StringWriter playlistJson = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(playlistJson, track);
        client.sadd("playlist", playlistJson.toString());
    }
}
