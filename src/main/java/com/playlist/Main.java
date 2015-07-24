package com.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.methods.UserRequest;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.User;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of execution.
 */
public class Main {
    static Api spotifyApi;
    final static RedisClient redisClient = new RedisClient();
    final static SpotifyClient spotifyClient = new SpotifyClient();

    final static String userId = System.getenv("SPOTIFY_USER_ID");
    final static String playlistId = System.getenv("SPOTIFY_PLAYLIST_ID");

    public static void main(String[] args) {
        try {
            while(true) {
                //Get an authenticated client
                spotifyApi = spotifyClient.getSpotifyApi();
                spotifyApi.getPlaylistTracks(userId, playlistId);

                PlaylistRequest request = spotifyApi.getPlaylist(userId, playlistId).build();

                try {
                    Playlist playlist = request.get();
                    List<PlaylistTrack> newTracks = getNewPlaylistTracks(playlist);
                    for(PlaylistTrack t : newTracks) {
                        postToSlack(playlist, t);
                        redisClient.addNewTrack(t);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                //Repeat playlist check every minute
                Thread.sleep(60 * 1000);
            }
        } catch (InterruptedException ie) {
            System.out.println("Thread interrupted!!");
        }
    }

    public static List<PlaylistTrack> getNewPlaylistTracks(Playlist playlist) {
        List<String> redisPlaylistList = redisClient.getRedisPlaylist();
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

    private static void postToSlack(Playlist playlist, PlaylistTrack track) {
        UserRequest userRequest = spotifyApi.getUser(track.getAddedBy().getId()).build();
        User user = new User();
        String addedBy = "";

        try {
            user = userRequest.get();
            addedBy = user.getDisplayName() != null ? user.getDisplayName() : user.getId();
        } catch (Exception e) {
            System.out.println("Failed to get Spotify User object: " + e.getMessage());
        }

        String trackUrl = track.getTrack().getExternalUrls().get("spotify");

        String message = String.format("New track added to <%s|%s> by <%s|%s>: <%s|(link)>", playlist.getUri(), playlist.getName(), user.getUri(), addedBy, trackUrl);
        System.out.println(message);

        SlackClient slackClient = new SlackClient();
        try {
            slackClient.postMessageToSlack(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
