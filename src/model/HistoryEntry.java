package model;

import java.time.LocalDateTime;
import util.Persistable;

public class HistoryEntry implements Persistable {
    private int songId;
    private LocalDateTime playedAt;

    public HistoryEntry(int songId, LocalDateTime playedAt) {
        this.songId = songId;
        this.playedAt = playedAt;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }

    @Override
    public String toDataString() {
        return songId + "|" + playedAt.toString();
    }

    @Override
    public String toString() {
        return "Song #" + songId + " played at " + playedAt;
    }
}
