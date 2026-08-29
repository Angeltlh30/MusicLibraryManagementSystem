package view;

import controller.PlaybackController;
import data.HistoryRepository;
import data.PlaylistRepository;
import data.SongRepository;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import model.HistoryEntry;
import model.Playlist;
import model.Song;
import util.Inputter;
import util.PlaylistNotFoundException;
import util.SongNotFoundException;

public class PlaybackView {

    private static final DateTimeFormatter PLAYED_AT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private PlaybackController playbackController;

    public PlaybackView() {
        SongRepository songRepository = new SongRepository();
        PlaylistRepository playlistRepository = new PlaylistRepository();
        HistoryRepository historyRepository = new HistoryRepository();
        this.playbackController = new PlaybackController(songRepository, playlistRepository, historyRepository);
    }

    void handleChoice(int choice) {
        switch (choice) {
            case 1:
                playSong();
                break;
            case 2:
                playPlaylist();
                break;
            case 3:
                viewRecentlyPlayed();
                break;
        }
    }

    private void playSong() {
        int id = Inputter.getAnPositiveInteger("Enter song id to play: ", "Id must be a positive integer: ");
        try {
            Song song = playbackController.playSong(id);
            System.out.println("Now playing:");
            printSongsTable(Collections.singletonList(song));
        } catch (SongNotFoundException e) {
            System.out.println(AnsiColors.colorize("Error: " + e.getMessage(), AnsiColors.ERROR));
        }
    }

    private void playPlaylist() {
        int playlistId = Inputter.getAnPositiveInteger("Enter playlist id: ", "Id must be a positive integer: ");
        Playlist playlist;
        try {
            playlist = playbackController.getPlaylistById(playlistId);
        } catch (PlaylistNotFoundException e) {
            System.out.println(AnsiColors.colorize("Error: " + e.getMessage(), AnsiColors.ERROR));
            return;
        }

        if (playlist.getSongCount() == 0) {
            System.out.println("This playlist has no songs to play.");
            return;
        }

        boolean shuffle = Inputter.getYesNo("Shuffle play order? (Y/N): ", "Please enter Y or N: ");

        System.out.println("Repeat mode:");
        System.out.println("0. No repeat");
        System.out.println("1. Repeat all");
        System.out.println("2. Repeat one song");
        int repeatMode = Inputter.getAnInteger("Enter your choice: ", "Invalid choice, please try again: ", 0, 2);

        Integer repeatSongId = null;
        int rounds = 1;
        if (repeatMode == PlaybackController.REPEAT_ALL) {
            rounds = Inputter.getAnPositiveInteger("How many times to loop through the playlist? ", "Must be a positive integer: ");
        } else if (repeatMode == PlaybackController.REPEAT_ONE) {
            repeatSongId = Inputter.getAnPositiveInteger("Enter id of the song to repeat: ", "Id must be a positive integer: ");
            rounds = Inputter.getAnPositiveInteger("How many times to repeat this song? ", "Must be a positive integer: ");
        }

        try {
            List<Song> playedOrder = playbackController.playPlaylist(playlistId, shuffle, repeatMode, repeatSongId, rounds);
            System.out.println("Playback finished. Play order:");
            printSongsTable(playedOrder);
        } catch (PlaylistNotFoundException | SongNotFoundException | IllegalArgumentException e) {
            System.out.println(AnsiColors.colorize("Error: " + e.getMessage(), AnsiColors.ERROR));
        }
    }

    private void viewRecentlyPlayed() {
        int limit = Inputter.getAnPositiveInteger("How many recent entries do you want to see? ", "Must be a positive integer: ");
        List<HistoryEntry> entries = playbackController.getRecentlyPlayed(limit);
        if (entries.isEmpty()) {
            System.out.println("No playback history yet.");
            return;
        }
        printHistoryTable(entries);
    }

    private void printSongsTable(List<Song> songs) {
        ConsoleTablePrinter.printTable(SongView.TABLE_HEADERS, SongView.buildRows(songs));
    }

    private void printHistoryTable(List<HistoryEntry> entries) {
        String[] headers = { "Song ID", "Title", "Artist", "Played At" };
        String[][] rows = new String[entries.size()][headers.length];
        for (int i = 0; i < entries.size(); i++) {
            HistoryEntry entry = entries.get(i);
            Song song = playbackController.getSongById(entry.getSongId());
            rows[i][0] = String.valueOf(entry.getSongId());
            rows[i][1] = song != null ? song.getTitle() : AnsiColors.colorize("(deleted)", AnsiColors.DIM);
            rows[i][2] = song != null ? song.getArtist() : AnsiColors.colorize("(deleted)", AnsiColors.DIM);
            rows[i][3] = entry.getPlayedAt().format(PLAYED_AT_FORMAT);
        }
        ConsoleTablePrinter.printTable(headers, rows);
    }
}
