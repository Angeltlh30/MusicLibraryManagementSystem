package view;

import controller.PlaybackController;
import data.HistoryRepository;
import data.PlaylistRepository;
import data.SongRepository;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import model.HistoryEntry;
import model.Song;
import util.Inputter;
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

    public void showMenu() {
        int choice;
        do {
            printMenu();
            choice = Inputter.getAnInteger("Enter your choice: ", "Invalid choice, please try again: ", 0, 2);
            handleChoice(choice);
        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println();
        System.out.println("========== PLAYBACK ==========");
        System.out.println("1. Play a song");
        System.out.println("2. View recently played");
        System.out.println("0. Back");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                playSong();
                break;
            case 2:
                viewRecentlyPlayed();
                break;
            case 0:
                System.out.println("Back to main menu.");
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
            System.out.println("Error: " + e.getMessage());
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
            rows[i][1] = song != null ? song.getTitle() : "(deleted)";
            rows[i][2] = song != null ? song.getArtist() : "(deleted)";
            rows[i][3] = entry.getPlayedAt().format(PLAYED_AT_FORMAT);
        }
        ConsoleTablePrinter.printTable(headers, rows);
    }
}
