package com.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.PlaylistTrack;
import redis.clients.jedis.Jedis;

import java.io.StringWriter;
import java.util.*;

/**
 * User: bradfogle
 * Date: 7/21/15
 * Time: 1:12 PM
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

    public List<PlaylistTrack> getNewPlaylistTracks(Playlist playlist) {
        List<String> redisPlaylistList = getRedisPlaylist();
        List<PlaylistTrack> spotifyPlaylistTracks = playlist.getTracks().getItems();

        ObjectMapper mapper = new ObjectMapper();
        List<String> newAdditionsList = new ArrayList<String>();

        try {
            for(PlaylistTrack t : spotifyPlaylistTracks) {
                StringWriter sw = new StringWriter();
                mapper.writeValue(sw, t);
                newAdditionsList.add(sw.toString());
            }
        } catch (Exception e) {
            System.out.println("Mapping exception: " + e.getMessage());
        }

        newAdditionsList.removeAll(redisPlaylistList);

        //Attempt to rebuild playlist track list with new songs
        try {
            spotifyPlaylistTracks.clear();
            for(String s : newAdditionsList) {
                spotifyPlaylistTracks.add(mapper.readValue(s, PlaylistTrack.class));
            }
        } catch (Exception e) {
            System.out.println("Mapping exception: " + e.getMessage());
        }

        return spotifyPlaylistTracks;
    }

    public void addNewTrack(PlaylistTrack track) throws Exception {
        StringWriter playlistJson = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(playlistJson, track);
        client.sadd("playlist", playlistJson.toString());
    }
}
