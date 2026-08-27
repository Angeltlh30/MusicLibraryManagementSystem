package controller;

import data.PlaylistRepository;
import data.SongRepository;
import java.util.ArrayList;
import java.util.List;
import model.Playlist;
import model.Song;
import util.PlaylistNotFoundException;
import util.SongNotFoundException;
import util.Validator;

public class PlaylistController {

    private PlaylistRepository playlistRepository;
    private SongRepository songRepository;

    public PlaylistController() {
        this.playlistRepository = new PlaylistRepository();
        this.songRepository = new SongRepository();
    }

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.getAll();
    }

    public Playlist getPlaylistById(int id) throws PlaylistNotFoundException {
        Playlist playlist = playlistRepository.findById(id);
        if (playlist == null) {
            throw new PlaylistNotFoundException(id);
        }
        return playlist;
    }

    public Playlist createPlaylist(String name, String description) {
        Validator.requireNonEmpty(name, "Playlist name");

        int id = playlistRepository.generateNextId();
        Playlist playlist = new Playlist(id, name.trim(), normalize(description));
        playlistRepository.add(playlist);
        return playlist;
    }

    public Playlist updatePlaylist(int id, String name, String description) throws PlaylistNotFoundException {
        Playlist playlist = getPlaylistById(id);

        if (name != null) {
            Validator.requireNonEmpty(name, "Playlist name");
            playlist.setName(name.trim());
        }
        if (description != null) {
            playlist.setDescription(normalize(description));
        }

        playlistRepository.update(playlist);
        return playlist;
    }

    public void deletePlaylist(int id) throws PlaylistNotFoundException {
        getPlaylistById(id);
        playlistRepository.deleteById(id);
    }

    public Playlist addSongToPlaylist(int playlistId, int songId) throws PlaylistNotFoundException, SongNotFoundException {
        Playlist playlist = getPlaylistById(playlistId);
        Song song = songRepository.findById(songId);
        if (song == null) {
            throw new SongNotFoundException(songId);
        }
        if (playlist.containsSongId(songId)) {
            throw new IllegalArgumentException("Song #" + songId + " is already in this playlist");
        }

        playlist.addSongId(songId);
        playlistRepository.update(playlist);
        return playlist;
    }

    public Playlist removeSongFromPlaylist(int playlistId, int songId) throws PlaylistNotFoundException {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.removeSongId(songId)) {
            throw new IllegalArgumentException("Song #" + songId + " is not in this playlist");
        }

        playlistRepository.update(playlist);
        return playlist;
    }

    public List<Song> getSongsInPlaylist(int playlistId) throws PlaylistNotFoundException {
        Playlist playlist = getPlaylistById(playlistId);
        List<Song> songs = new ArrayList<>();
        for (Integer songId : playlist.getSongIds()) {
            Song song = songRepository.findById(songId);
            if (song != null) {
                songs.add(song);
            }
        }
        return songs;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
