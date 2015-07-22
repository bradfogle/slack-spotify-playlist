package com.playlist;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;

import java.io.IOException;

/**
 * User: bradfogle
 * Date: 7/20/15
 * Time: 3:45 PM
 */
public class SpotifyClient {

    private String clientId = System.getenv("SPOTIFY_CLIENT_ID");
    private String clientSecret = System.getenv("SPOTIFY_CLIENT_SECRET");

    public Api getSpotifyApi() {
        //Authorize client
        Api spotifyApi = Api.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        //Create a request object
        ClientCredentialsGrantRequest request = spotifyApi.clientCredentialsGrant().build();

        try {
            ClientCredentials clientCredentials = request.get();

            System.out.println("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
            System.out.println("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");

            //Set access token on the Api object so that it's used going forward
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        } catch (WebApiException wae) {
            System.out.println("Api client error: " + wae.getMessage());
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe.getMessage());
        }

        return spotifyApi;
    }
}
