package util;

public class SongNotFoundException extends Exception {

    public SongNotFoundException(int id) {
        super("Song with id " + id + " not found");
    }
}
