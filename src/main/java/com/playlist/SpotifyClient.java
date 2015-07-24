package com.playlist;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;

import java.io.IOException;
import java.util.Date;

/**
 * Helper class built on the spotify-web-api-java project for creation of an authenticated client object.
 */
public class SpotifyClient {

    private String clientId = System.getenv("SPOTIFY_CLIENT_ID");
    private String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");
    private Date accessTokenReceivedDate;
    private Integer accessTokenExpiresIn;
    private Api spotifyApi;

    public Api getSpotifyApi() {
        //TODO: still need to test this logic
        try {
            //If we've never received an access token or if the access token is expired
            if(accessTokenReceivedDate == null ||
                    System.currentTimeMillis() - accessTokenReceivedDate.getTime() >= accessTokenExpiresIn) {
                spotifyApi = Api.builder()
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .build();

                //Create a request object
                ClientCredentialsGrantRequest request = spotifyApi.clientCredentialsGrant().build();

                accessTokenReceivedDate = new Date(System.currentTimeMillis());
                ClientCredentials clientCredentials = request.get();
                accessTokenExpiresIn = clientCredentials.getExpiresIn();

                System.out.println("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
                System.out.println("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");

                //Set access token on the Api object so that it's used going forward
                spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            }
        } catch (WebApiException wae) {
            System.out.println("Api client error: " + wae.getMessage());
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe.getMessage());
        }

        return spotifyApi;
    }
}
