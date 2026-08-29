package view;

import controller.SongController;
import java.util.Collections;
import java.util.List;
import model.Song;
import util.Inputter;
import util.SongNotFoundException;

public class SongView {

    private SongController songController;

    public static final String[] TABLE_HEADERS = { "ID", "Title", "Artist", "Album", "Genre", "Duration", "Play count", "Rating", "Favorite"  };

    public SongView() {
        this.songController = new SongController();
    }

    void handleChoice(int choice) {
        switch (choice) {
            case 1:
                addSong();
                break;
            case 2:
                listSongs();
                break;
            case 3:
                viewSongDetail();
                break;
            case 4:
                updateSong();
                break;
            case 5:
                deleteSong();
                break;
            case 6:
                searchSong();
                break;
            case 7:
                sortSongs();
                break;
            case 8:
                toggleFavorite();
                break;
        }
    }

    private void addSong() {
        String title = Inputter.getAString("Enter song title: ", "Song title must not be empty: ");
        String artist = Inputter.getAString("Enter artist: ", "Artist must not be empty: ");
        String album = Inputter.getAString("Enter album (optional): ");
        String genre = Inputter.getAString("Enter genre (optional): ");
        int duration = Inputter.getAnPositiveInteger("Enter duration (seconds): ", "Duration must be a positive integer: ");

        try {
            Song song = songController.addSong(title, artist, album, genre, duration);
            System.out.println(AnsiColors.colorize("Song added successfully, id = " + song.getId(), AnsiColors.SUCCESS));
            printSongsTable(Collections.singletonList(song));
        } catch (IllegalArgumentException e) {
            System.out.println(AnsiColors.colorize("Error: " + e.getMessage(), AnsiColors.ERROR));
        }
    }

    private void listSongs() {
        List<Song> songs = songController.getAllSongs();
        if (songs.isEmpty()) {
            System.out.println("No songs yet.");
            return;
        }
        printSongsTable(songs);
    }

    private void viewSongDetail() {
        int id = Inputter.getAnPositiveInteger("Enter song id: ", "Id must be a positive integer: ");
        try {
            Song song = songController.getSongById(id);
            printSongsTable(Collections.singletonList(song));
        } catch (SongNotFoundException e) {
            System.out.println(AnsiColors.colorize("Error: " + e.getMessage(), AnsiColors.ERROR));
        }
    }

    private void updateSong() {
        int id = Inputter.getAnPositiveInteger("Enter id of the song to update: ", "Id must be a positive integer: ");
        Song existing;
        try {
            existing = songController.getSongById(id);
        } catch (SongNotFoundException e) {
            System.out.println(AnsiColors.colorize("Error: " + e.getMessage(), AnsiColors.ERROR));
            return;
        }

        System.out.println("Current song info:");
        printSongsTable(Collections.singletonList(existing));
        System.out.println("Press Enter to keep the current value for a field.");

        String title = Inputter.getAString("Enter new title (current: " + existing.getTitle() + "): ");
        String artist = Inputter.getAString("Enter new artist (current: " + existing.getArtist() + "): ");
        String album = Inputter.getAString("Enter new album (current: " + existing.getAlbum() + "): ");
        String genre = Inputter.getAString("Enter new genre (current: " + existing.getGenre() + "): ");
        Integer duration = Inputter.getAnPositiveIntegerOptional(
                "Enter new duration in seconds (current: " + existing.getDurationInSeconds() + "): ",
                "Duration must be a positive integer: ");

        try {
            Song updated = songController.updateSong(id, title, artist, album, genre, duration);
            System.out.println(AnsiColors.colorize("Updated successfully.", AnsiColors.SUCCESS));
            printSongsTable(Collections.singletonList(updated));
        } catch (SongNotFoundException | IllegalArgumentException e) {
            System.out.println(AnsiColors.colorize("Error: " + e.getMessage(), AnsiColors.ERROR));
        }
    }

    private void deleteSong() {
        int id = Inputter.getAnPositiveInteger("Enter id of the song to delete: ", "Id must be a positive integer: ");
        Song existing;
        try {
            existing = songController.getSongById(id);
        } catch (SongNotFoundException e) {
            System.out.println(AnsiColors.colorize("Error: " + e.getMessage(), AnsiColors.ERROR));
            return;
        }

        System.out.println("You are about to delete:");
        printSongsTable(Collections.singletonList(existing));
        boolean confirmed = Inputter.getYesNo("Are you sure you want to delete this song? (Y/N): ", "Please enter Y or N: ");
        if (!confirmed) {
            System.out.println("Delete cancelled.");
            return;
        }

        try {
            songController.deleteSong(id);
            System.out.println(AnsiColors.colorize("Deleted successfully.", AnsiColors.SUCCESS));
        } catch (SongNotFoundException e) {
            System.out.println(AnsiColors.colorize("Error: " + e.getMessage(), AnsiColors.ERROR));
        }
    }

    private void searchSong() {
        System.out.println("Search by:");
        System.out.println("1. Title");
        System.out.println("2. Artist");
        System.out.println("3. Genre");
        int type = Inputter.getAnInteger("Enter your choice: ", "Invalid choice, please try again: ", 1, 3);
        String keyword = Inputter.getAString("Enter keyword: ", "Keyword must not be empty: ");

        List<Song> result;
        switch (type) {
            case 1:
                result = songController.searchByTitle(keyword);
                break;
            case 2:
                result = songController.searchByArtist(keyword);
                break;
            default:
                result = songController.searchByGenre(keyword);
                break;
        }

        if (result.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }
        printSongsTable(result);
    }

    private void sortSongs() {
        List<Song> songs = songController.getAllSongs();
        if (songs.isEmpty()) {
            System.out.println("No songs yet.");
            return;
        }

        System.out.println("Sort by:");
        System.out.println("1. Title");
        System.out.println("2. Artist");
        System.out.println("3. Duration");
        int criterion = Inputter.getAnInteger("Enter your choice: ", "Invalid choice, please try again: ", 1, 3);

        System.out.println("Order:");
        System.out.println("1. Ascending");
        System.out.println("2. Descending");
        int order = Inputter.getAnInteger("Enter your choice: ", "Invalid choice, please try again: ", 1, 2);
        boolean ascending = order == 1;

        List<Song> result;
        switch (criterion) {
            case 1:
                result = songController.sortByTitle(ascending);
                break;
            case 2:
                result = songController.sortByArtist(ascending);
                break;
            default:
                result = songController.sortByDuration(ascending);
                break;
        }

        printSongsTable(result);
    }

    private void toggleFavorite() {
        int id = Inputter.getAnPositiveInteger("Enter song id: ", "Id must be a positive integer: ");
        try {
            Song song = songController.toggleFavorite(id);
            String message = song.isFavorite() ? "Song marked as favorite." : "Song removed from favorites.";
            System.out.println(AnsiColors.colorize(message, AnsiColors.SUCCESS));
            printSongsTable(Collections.singletonList(song));
        } catch (SongNotFoundException e) {
            System.out.println(AnsiColors.colorize("Error: " + e.getMessage(), AnsiColors.ERROR));
        }
    }

    private void printSongsTable(List<Song> songs) {
        ConsoleTablePrinter.printTable(TABLE_HEADERS, buildRows(songs));
    }

    public static String[][] buildRows(List<Song> songs) {
        String[][] rows = new String[songs.size()][TABLE_HEADERS.length];
        for (int i = 0; i < songs.size(); i++) {
            Song s = songs.get(i);
            rows[i][0] = String.valueOf(s.getId());
            rows[i][1] = s.getTitle();
            rows[i][2] = s.getArtist();
            rows[i][3] = s.getAlbum();
            rows[i][4] = s.getGenre();
            rows[i][5] = s.getDurationInSeconds() + "s";
            rows[i][6] = String.valueOf(s.getPlayCount());
            rows[i][7] = String.valueOf(s.getRating());
            rows[i][8] = s.isFavorite() ? AnsiColors.colorize("Yes", AnsiColors.ACCENT) : "No";
        }
        return rows;
    }
}
