package controller;

import algorithms.Shuffle;
import data.HistoryRepository;
import data.PlaylistRepository;
import data.SongRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.HistoryEntry;
import model.Playlist;
import model.Song;
import util.PlaylistNotFoundException;
import util.SongNotFoundException;

public class PlaybackController {

    public static final int REPEAT_OFF = 0;
    public static final int REPEAT_ALL = 1;
    public static final int REPEAT_ONE = 2;

    private SongRepository songRepository;
    private PlaylistRepository playlistRepository;
    private HistoryRepository historyRepository;

    public PlaybackController(SongRepository songRepository, PlaylistRepository playlistRepository, HistoryRepository historyRepository) {
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
        this.historyRepository = historyRepository;
    }
    
    public Song playSong(int songId) throws SongNotFoundException{
        Song song = songRepository.findById(songId);
        if(song == null){
            throw new SongNotFoundException(songId);
        }else{
            return recordPlay(song);
        }
    }

    public Playlist getPlaylistById(int playlistId) throws PlaylistNotFoundException {
        Playlist playlist = playlistRepository.findById(playlistId);
        if (playlist == null) {
            throw new PlaylistNotFoundException(playlistId);
        }
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

    public List<Song> playPlaylist(int playlistId, boolean shuffle, int repeatMode, Integer repeatSongId, int rounds)
            throws PlaylistNotFoundException, SongNotFoundException {
        List<Song> songs = getSongsInPlaylist(playlistId);
        if (songs.isEmpty()) {
            throw new IllegalArgumentException("This playlist has no songs to play");
        }
        if (shuffle) {
            Shuffle.shuffle(songs);
        }

        List<Song> playedOrder = new ArrayList<>();
        if (repeatMode == REPEAT_ONE) {
            if (repeatSongId == null) {
                throw new IllegalArgumentException("Song id to repeat must be provided");
            }
            Song target = findSongInList(songs, repeatSongId);
            if (target == null) {
                throw new SongNotFoundException(repeatSongId);
            }
            for (int i = 0; i < rounds; i++) {
                playedOrder.add(recordPlay(target));
            }
        } else {
            int loops = repeatMode == REPEAT_ALL ? rounds : 1;
            for (int r = 0; r < loops; r++) {
                for (Song song : songs) {
                    playedOrder.add(recordPlay(song));
                }
            }
        }
        return playedOrder;
    }

    public List<HistoryEntry> getRecentlyPlayed(int limit) {
        List<HistoryEntry> all = historyRepository.getAll();
        List<HistoryEntry> recent = new ArrayList<>();
        for (int i = all.size() - 1; i >= 0 && recent.size() < limit; i--) {
            recent.add(all.get(i));
        }
        return recent;
    }

    public Song getSongById(int songId) {
        return songRepository.findById(songId);
    }

    private Song recordPlay(Song song) {
        song.setPlayCount(song.getPlayCount() + 1);
        songRepository.update(song);

        HistoryEntry entry = new HistoryEntry(song.getId(), LocalDateTime.now());
        historyRepository.add(entry);
        return song;
    }

    private Song findSongInList(List<Song> songs, int songId) {
        for (Song song : songs) {
            if (song.getId() == songId) {
                return song;
            }
        }
        return null;
    }

}
