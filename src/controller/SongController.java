package controller;

import data.SongRepository;
import java.util.List;
import model.Song;
import util.SongNotFoundException;
import util.Validator;

public class SongController {

    private SongRepository songRepository;

    public SongController() {
        this.songRepository = new SongRepository();
    }

    public List<Song> getAllSongs() {
        return songRepository.getAll();
    }

    public Song getSongById(int id) throws SongNotFoundException {
        Song song = songRepository.findById(id);
        if (song == null) {
            throw new SongNotFoundException(id);
        }
        return song;
    }

    public Song addSong(String title, String artist, String album, String genre, int durationInSeconds) {
        Validator.requireNonEmpty(title, "Ten bai hat");
        Validator.requireNonEmpty(artist, "Ca si");
        Validator.requirePositive(durationInSeconds, "Thoi luong");

        int id = songRepository.generateNextId();
        Song song = new Song(id, title.trim(), artist.trim(), normalize(album), normalize(genre), durationInSeconds);
        songRepository.add(song);
        return song;
    }

    public Song updateSong(int id, String title, String artist, String album, String genre, int durationInSeconds)
            throws SongNotFoundException {
        Song song = getSongById(id);
        Validator.requireNonEmpty(title, "Ten bai hat");
        Validator.requireNonEmpty(artist, "Ca si");
        Validator.requirePositive(durationInSeconds, "Thoi luong");

        song.setTitle(title.trim());
        song.setArtist(artist.trim());
        song.setAlbum(normalize(album));
        song.setGenre(normalize(genre));
        song.setDurationInSeconds(durationInSeconds);
        songRepository.update(song);
        return song;
    }

    public void deleteSong(int id) throws SongNotFoundException {
        getSongById(id);
        songRepository.deleteById(id);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
