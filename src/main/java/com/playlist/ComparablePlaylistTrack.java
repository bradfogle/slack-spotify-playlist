package com.playlist;

import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.Track;
import com.wrapper.spotify.models.User;

import java.util.Date;

/**
 * Utility class to help manage comparison of PlaylistTrack objects.
 */
public class ComparablePlaylistTrack extends PlaylistTrack {

    private Date addedAt;
    private User addedBy;
    private Track track;

    public ComparablePlaylistTrack() {}

    public ComparablePlaylistTrack(PlaylistTrack playlistTrack) {
        this.addedAt = playlistTrack.getAddedAt();
        this.addedBy = playlistTrack.getAddedBy();
        this.track = playlistTrack.getTrack();
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComparablePlaylistTrack that = (ComparablePlaylistTrack) o;

        if (!track.equals(that.track)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return track.hashCode();
    }
}
