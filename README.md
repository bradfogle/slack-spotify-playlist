# slack-spotify-playlist
Publish shared Spotify playlist updates to Slack

Our team loves <a href="https://slack.com/">Slack</a> as a messaging and collaboration platform.  We also love music and
sharing our unique interests with the rest of the team.  We had been posting individual songs into a channel for everyone
to listen to, but found it much easier to work from a shared Spotify playlist for easier playback, curation, etc.

This app manages the retrieval of new playlist tracks and automates the process of posting them in a formatted way 
to a desired Slack channel.

## Prerequisites
### Slack Webhook Integration
The first step is to configure an incoming webhook integration for your desired channel, described here:
https://api.slack.com/incoming-webhooks.

### Spotify Client ID & Secret
The next step is to get a Spotify Client ID & Secret by creating an application, described here: 
https://developer.spotify.com/my-applications.

### Redis To Go Instance Running

### Shared Spotify Playlist

### Environment Variables Set
The following environment variables are expected to be set:
```
#Owner of the shared playlist. Used for retrieval of playlist data.
SPOTIFY_USER_ID=<userId>

#Spotify ID of the shared playlist. Used for retrieval of playlist data.
SPOTIFY_PLAYLIST_ID=<playlistId>

#Spotify Client ID. Used for authentication purposes.
SPOTIFY_CLIENT_ID=<clientId>

#Spotify Client Secret. Used for authentication purposes.
SPOTIFY_CLIENT_SECRET=<clientSecret>

#Slack Webhook URL. Used for posting new tracks into a channel.
SLACK_WEBHOOK_URL=<webhookUrl>

#URI of your Redis instance (e.g. redis://redistogo:password@carp.redistogo.com:9727/
REDIS_URI=<redisUri>
```