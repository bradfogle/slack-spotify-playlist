package com.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrapper.spotify.models.PlaylistTrack;
import redis.clients.jedis.Jedis;

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

    public RedisClient() {

        try {
            this.client = new Jedis(new URI(System.getenv("REDISTOGO_URL")));
        } catch (URISyntaxException e) {
            System.out.println("Error parsing Redis URI: " + e.getMessage());
        }
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
