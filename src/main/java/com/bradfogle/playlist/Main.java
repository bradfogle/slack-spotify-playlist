package com.bradfogle.playlist;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.PlaylistRequest;
import com.wrapper.spotify.methods.UserRequest;
import com.wrapper.spotify.models.Playlist;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: bradfogle
 * Date: 7/20/15
 * Time: 3:37 PM
 */
public class Main {
    final static RedisClient redisClient = new RedisClient();
    final static SpotifyClient spotifyClient = new SpotifyClient();

    final static String userId = System.getenv("SPOTIFY_USER_ID");
    final static String playlistId = System.getenv("SPOTIFY_PLAYLIST_ID");

    final static DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    private static Date startDate = new Date();

    public static void main(String[] args) {
        try {
            startDate = format.parse(System.getenv("START_DATE"));

            while(true) {
                Api spotifyApi = spotifyClient.getSpotifyApi();
                spotifyApi.getPlaylistTracks(userId, playlistId);

                PlaylistRequest request = spotifyApi.getPlaylist(userId, playlistId).build();

                //Check if we've stored a newer updated time (from a playlist update) otherwise use default
                Date lastUpdatedTime = redisClient.getLastUpdatedTime() != null ? redisClient.getLastUpdatedTime() : startDate;

                try {
                    Playlist playlist = request.get();
                    List<PlaylistTrack> playlistTracks = playlist.getTracks().getItems();

                    //Reverse order of items to speed up new track lookup
                    Collections.reverse(playlistTracks);

                    Date newLastUpdatedTime = lastUpdatedTime;

                    //Loop through playlist tracks, starting with most recently added
                    for(PlaylistTrack t : playlistTracks) {
                        //If current track was added after last execution
                        if(lastUpdatedTime.before(t.getAddedAt())) {
                            postToSlack(spotifyApi, playlist, t);

                            //If processing multiple tracks, calculate newLastUpdatedTime based on latest addition
                            if(newLastUpdatedTime.before(t.getAddedAt())) {
                                newLastUpdatedTime = t.getAddedAt();
                            }
                        }
                    }

                    //Update last updated time with latest playlist track addition time
                    if(lastUpdatedTime.before(newLastUpdatedTime)) {
                        redisClient.saveLastUpdatedTime(newLastUpdatedTime);
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                //Repeat playlist check every minute
                Thread.sleep(60 * 1000);
            }
        } catch (InterruptedException ie) {
            System.out.println("Thread interrupted!!");
        } catch (ParseException pe) {
            System.out.println("Unable to parse date: " + System.getenv("START_DATE"));
        }
    }

    private static void postToSlack(Api spotifyApi, Playlist playlist, PlaylistTrack track) {
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
