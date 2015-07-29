package com.playlist;

import com.wrapper.spotify.models.*;

import java.util.List;

/**
 * Utility class to help manage comparison of Track objects.
 */
public class ComparableTrack extends Track {

    private SimpleAlbum album;
    private List<SimpleArtist> artists;
    private List<String> availableMarkets;
    private int discNumber;
    private int duration;
    private boolean explicit;
    private ExternalIds externalIds;
    private ExternalUrls externalUrls;
    private String href;
    private String id;
    private String name;
    private int popularity;
    private String previewUrl;
    private int trackNumber;
    private SpotifyEntityType type = SpotifyEntityType.TRACK;
    private String uri;

    public ComparableTrack(Track track) {
        this.album = track.getAlbum();
        this.artists = track.getArtists();
        this.availableMarkets = track.getAvailableMarkets();
        this.discNumber = track.getDiscNumber();
        this.duration = track.getDuration();
        this.explicit = track.isExplicit();
        this.externalIds = track.getExternalIds();
        this.externalUrls = track.getExternalUrls();
        this.href = track.getHref();
        this.id = track.getId();
        this.name = track.getName();
        this.popularity = track.getPopularity();
        this.previewUrl = track.getPreviewUrl();
        this.trackNumber = track.getTrackNumber();
        this.uri = track.getUri();
    }

    public SimpleAlbum getAlbum() {
        return album;
    }

    public void setAlbum(SimpleAlbum album) {
        this.album = album;
    }

    public List<SimpleArtist> getArtists() {
        return artists;
    }

    public void setArtists(List<SimpleArtist> artists) {
        this.artists = artists;
    }

    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public void setAvailableMarkets(List<String> availableMarkets) {
        this.availableMarkets = availableMarkets;
    }

    public int getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(int discNumber) {
        this.discNumber = discNumber;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public ExternalIds getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(ExternalIds externalIds) {
        this.externalIds = externalIds;
    }

    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public SpotifyEntityType getType() {
        return type;
    }

    public void setType(SpotifyEntityType type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComparableTrack that = (ComparableTrack) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
