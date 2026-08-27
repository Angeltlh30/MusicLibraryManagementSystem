package view;

import controller.SongController;
import java.util.Collections;
import java.util.List;
import model.Song;
import util.Inputter;
import util.SongNotFoundException;

public class SongView {

    private SongController songController;

    private static final String[] TABLE_HEADERS = {
        "ID", "Title", "Artist", "Album", "Genre", "Duration", "Play count", "Rating", "Favorite"
    };

    public SongView() {
        this.songController = new SongController();
    }

    public void showMenu() {
        int choice;
        do {
            printMenu();
            choice = Inputter.getAnInteger("Enter your choice: ", "Invalid choice, please try again: ", 0, 5);
            handleChoice(choice);
        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println();
        System.out.println("========== SONG MANAGEMENT ==========");
        System.out.println("1. Add song");
        System.out.println("2. View song list");
        System.out.println("3. View song detail");
        System.out.println("4. Update song");
        System.out.println("5. Delete song");
        System.out.println("0. Exit");
    }

    private void handleChoice(int choice) {
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
            case 0:
                System.out.println("Goodbye!");
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
            System.out.println("Song added successfully, id = " + song.getId());
            printSongsTable(Collections.singletonList(song));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
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
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateSong() {
        int id = Inputter.getAnPositiveInteger("Enter id of the song to update: ", "Id must be a positive integer: ");
        Song existing;
        try {
            existing = songController.getSongById(id);
        } catch (SongNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
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
            System.out.println("Updated successfully.");
            printSongsTable(Collections.singletonList(updated));
        } catch (SongNotFoundException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteSong() {
        int id = Inputter.getAnPositiveInteger("Enter id of the song to delete: ", "Id must be a positive integer: ");
        Song existing;
        try {
            existing = songController.getSongById(id);
        } catch (SongNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
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
            System.out.println("Deleted successfully.");
        } catch (SongNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void printSongsTable(List<Song> songs) {
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
            rows[i][8] = s.isFavorite() ? "Yes" : "No";
        }

        int[] widths = new int[TABLE_HEADERS.length];
        for (int c = 0; c < TABLE_HEADERS.length; c++) {
            widths[c] = TABLE_HEADERS[c].length();
        }
        for (String[] row : rows) {
            for (int c = 0; c < row.length; c++) {
                String value = row[c] == null ? "" : row[c];
                if (value.length() > widths[c]) {
                    widths[c] = value.length();
                }
            }
        }

        printTableSeparator(widths);
        printTableRow(TABLE_HEADERS, widths);
        printTableSeparator(widths);
        for (String[] row : rows) {
            printTableRow(row, widths);
        }
        printTableSeparator(widths);
    }

    private void printTableSeparator(int[] widths) {
        StringBuilder line = new StringBuilder("+");
        for (int w : widths) {
            for (int i = 0; i < w + 2; i++) {
                line.append('-');
            }
            line.append('+');
        }
        System.out.println(line);
    }

    private void printTableRow(String[] cells, int[] widths) {
        StringBuilder line = new StringBuilder("|");
        for (int c = 0; c < cells.length; c++) {
            String value = cells[c] == null ? "" : cells[c];
            line.append(' ').append(value);
            for (int i = value.length(); i < widths[c]; i++) {
                line.append(' ');
            }
            line.append(" |");
        }
        System.out.println(line);
    }
}
