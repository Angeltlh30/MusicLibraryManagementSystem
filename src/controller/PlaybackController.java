package controller;

import data.HistoryRepository;
import data.PlaylistRepository;
import data.SongRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.HistoryEntry;
import model.Song;
import util.SongNotFoundException;

public class PlaybackController {
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
            song.setPlayCount(song.getPlayCount() + 1);
            songRepository.update(song);
           
            HistoryEntry entry = new HistoryEntry(songId, LocalDateTime.now());
            historyRepository.add(entry);
            return song;
        }
        
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

}
