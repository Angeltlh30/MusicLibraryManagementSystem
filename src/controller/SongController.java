package controller;

import algorithms.LinearSearch;
import algorithms.MergeSort;
import data.SongRepository;
import java.util.Comparator;
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
        Validator.requireNonEmpty(title, "Song title");
        Validator.requireNonEmpty(artist, "Artist");
        Validator.requirePositive(durationInSeconds, "Duration");

        int id = songRepository.generateNextId();
        Song song = new Song(id, title.trim(), artist.trim(), normalize(album), normalize(genre), durationInSeconds);
        songRepository.add(song);
        return song;
    }

    public Song updateSong(int id, String title, String artist, String album, String genre, Integer durationInSeconds)
            throws SongNotFoundException {
        Song song = getSongById(id);

        if (title != null) {
            Validator.requireNonEmpty(title, "Song title");
            song.setTitle(title.trim());
        }
        if (artist != null) {
            Validator.requireNonEmpty(artist, "Artist");
            song.setArtist(artist.trim());
        }
        if (album != null) {
            song.setAlbum(normalize(album));
        }
        if (genre != null) {
            song.setGenre(normalize(genre));
        }
        if (durationInSeconds != null) {
            Validator.requirePositive(durationInSeconds, "Duration");
            song.setDurationInSeconds(durationInSeconds);
        }

        songRepository.update(song);
        return song;
    }

    public void deleteSong(int id) throws SongNotFoundException {
        getSongById(id);
        songRepository.deleteById(id);
    }

    public List<Song> searchByTitle(String title) {
        return LinearSearch.searchByTitle(songRepository.getAll(), title);
    }

    public List<Song> searchByArtist(String artist) {
        return LinearSearch.searchByArtist(songRepository.getAll(), artist);
    }

    public List<Song> searchByGenre(String genre) {
        return LinearSearch.searchByGenre(songRepository.getAll(), genre);
    }

    public List<Song> sortByTitle(boolean ascending) {
        Comparator<Song> comparator = Comparator.comparing(Song::getTitle, String.CASE_INSENSITIVE_ORDER);
        return MergeSort.sort(songRepository.getAll(), ascending ? comparator : comparator.reversed());
    }

    public List<Song> sortByArtist(boolean ascending) {
        Comparator<Song> comparator = Comparator.comparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER);
        return MergeSort.sort(songRepository.getAll(), ascending ? comparator : comparator.reversed());
    }

    public List<Song> sortByDuration(boolean ascending) {
        Comparator<Song> comparator = Comparator.comparingInt(Song::getDurationInSeconds);
        return MergeSort.sort(songRepository.getAll(), ascending ? comparator : comparator.reversed());
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
