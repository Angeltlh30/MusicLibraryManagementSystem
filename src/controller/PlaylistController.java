package controller;

import algorithms.LinearSearch;
import data.PlaylistRepository;
import data.SongRepository;
import java.util.ArrayList;
import java.util.List;
import model.Playlist;
import model.Song;
import structures.Stack;
import util.PlaylistNotFoundException;
import util.SongNotFoundException;
import util.Validator;

public class PlaylistController {

    private PlaylistRepository playlistRepository;
    private SongRepository songRepository;
    private Stack<PlaylistAction> undoStack;
    private Stack<PlaylistAction> redoStack;

    public PlaylistController() {
        this.playlistRepository = new PlaylistRepository();
        this.songRepository = new SongRepository();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
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

    public boolean isPlaylistNameTaken(String name, Integer excludeId) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return playlistRepository.existsByName(name.trim(), excludeId);
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
        applyAddSong(playlistId, songId);
        undoStack.push(new PlaylistAction(PlaylistAction.ADD_SONG, playlistId, songId));
        redoStack.clear();
        return getPlaylistById(playlistId);
    }

    public Playlist removeSongFromPlaylist(int playlistId, int songId) throws PlaylistNotFoundException {
        applyRemoveSong(playlistId, songId);
        undoStack.push(new PlaylistAction(PlaylistAction.REMOVE_SONG, playlistId, songId));
        redoStack.clear();
        return getPlaylistById(playlistId);
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public String undo() throws PlaylistNotFoundException, SongNotFoundException {
        if (undoStack.isEmpty()) {
            throw new IllegalStateException("Nothing to undo.");
        }

        PlaylistAction action = undoStack.pop();
        if (action.type == PlaylistAction.ADD_SONG) {
            applyRemoveSong(action.playlistId, action.songId);
            redoStack.push(action);
            return "Undid: add song #" + action.songId + " to playlist #" + action.playlistId;
        } else {
            applyAddSong(action.playlistId, action.songId);
            redoStack.push(action);
            return "Undid: remove song #" + action.songId + " from playlist #" + action.playlistId;
        }
    }

    public String redo() throws PlaylistNotFoundException, SongNotFoundException {
        if (redoStack.isEmpty()) {
            throw new IllegalStateException("Nothing to redo.");
        }

        PlaylistAction action = redoStack.pop();
        if (action.type == PlaylistAction.ADD_SONG) {
            applyAddSong(action.playlistId, action.songId);
            undoStack.push(action);
            return "Redid: add song #" + action.songId + " to playlist #" + action.playlistId;
        } else {
            applyRemoveSong(action.playlistId, action.songId);
            undoStack.push(action);
            return "Redid: remove song #" + action.songId + " from playlist #" + action.playlistId;
        }
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

    public List<Song> searchSongsInPlaylistByTitle(int playlistId, String title) throws PlaylistNotFoundException {
        return LinearSearch.searchByTitle(getSongsInPlaylist(playlistId), title);
    }

    public List<Song> searchSongsInPlaylistByArtist(int playlistId, String artist) throws PlaylistNotFoundException {
        return LinearSearch.searchByArtist(getSongsInPlaylist(playlistId), artist);
    }

    public List<Song> searchSongsInPlaylistByGenre(int playlistId, String genre) throws PlaylistNotFoundException {
        return LinearSearch.searchByGenre(getSongsInPlaylist(playlistId), genre);
    }

    private void applyAddSong(int playlistId, int songId) throws PlaylistNotFoundException, SongNotFoundException {
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
    }

    private void applyRemoveSong(int playlistId, int songId) throws PlaylistNotFoundException {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.removeSongId(songId)) {
            throw new IllegalArgumentException("Song #" + songId + " is not in this playlist");
        }

        playlistRepository.update(playlist);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private static class PlaylistAction {
        private static final int ADD_SONG = 0;
        private static final int REMOVE_SONG = 1;

        private final int type;
        private final int playlistId;
        private final int songId;

        private PlaylistAction(int type, int playlistId, int songId) {
            this.type = type;
            this.playlistId = playlistId;
            this.songId = songId;
        }
    }
}
