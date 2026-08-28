package data;

import java.util.ArrayList;
import java.util.List;
import model.Song;
import util.FileHandler;

public class SongRepository extends FileHandler<Song> {

    private static final String FILE_PATH = "database/songs.txt";

    private List<Song> songs;

    public SongRepository() {
        this.songs = new ArrayList<>();
        load(FILE_PATH, songs);
    }

    @Override
    public Song handleLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 9) {
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0]);
            String title = parts[1];
            String artist = parts[2];
            String album = parts[3];
            String genre = parts[4];
            int durationInSeconds = Integer.parseInt(parts[5]);
            int playCount = Integer.parseInt(parts[6]);
            double rating = Double.parseDouble(parts[7]);
            boolean favorite = Boolean.parseBoolean(parts[8]);

            Song song = new Song(id, title, artist, album, genre, durationInSeconds);
            song.setPlayCount(playCount);
            song.setRating(rating);
            song.setFavorite(favorite);
            return song;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public List<Song> getAll() {
        return songs;
    }

    public Song findById(int id) {
        for (Song song : songs) {
            if (song.getId() == id) {
                return song;
            }
        }
        return null;
    }

    public int generateNextId() {
        int maxId = 0;
        for (Song song : songs) {
            if (song.getId() > maxId) {
                maxId = song.getId();
            }
        }
        return maxId + 1;
    }

    public boolean add(Song song) {
        songs.add(song);
        return save(songs, FILE_PATH);
    }

    public boolean update(Song song) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getId() == song.getId()) {
                songs.set(i, song);
                return save(songs, FILE_PATH);
            }
        }
        return false;
    }

    public boolean deleteById(int id) {
        Song existing = findById(id);
        if (existing == null) {
            return false;
        }
        songs.remove(existing);
        return save(songs, FILE_PATH);
    }
}
