package util;

public class PlaylistNotFoundException extends Exception {

    public PlaylistNotFoundException(int id) {
        super("Playlist with id " + id + " not found");
    }
}
