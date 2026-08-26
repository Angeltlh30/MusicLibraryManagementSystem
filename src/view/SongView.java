package view;

import controller.SongController;
import java.util.List;
import model.Song;
import util.Inputter;
import util.SongNotFoundException;

public class SongView {

    private SongController songController;

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
        for (Song song : songs) {
            System.out.println(song);
        }
    }

    private void viewSongDetail() {
        int id = Inputter.getAnPositiveInteger("Enter song id: ", "Id must be a positive integer: ");
        try {
            Song song = songController.getSongById(id);
            System.out.println(song);
        } catch (SongNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateSong() {
        int id = Inputter.getAnPositiveInteger("Enter id of the song to update: ", "Id must be a positive integer: ");
        String title = Inputter.getAString("Enter new title: ", "Song title must not be empty: ");
        String artist = Inputter.getAString("Enter new artist: ", "Artist must not be empty: ");
        String album = Inputter.getAString("Enter new album (optional): ");
        String genre = Inputter.getAString("Enter new genre (optional): ");
        int duration = Inputter.getAnPositiveInteger("Enter new duration (seconds): ", "Duration must be a positive integer: ");

        try {
            songController.updateSong(id, title, artist, album, genre, duration);
            System.out.println("Updated successfully.");
        } catch (SongNotFoundException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteSong() {
        int id = Inputter.getAnPositiveInteger("Enter id of the song to delete: ", "Id must be a positive integer: ");
        try {
            songController.deleteSong(id);
            System.out.println("Deleted successfully.");
        } catch (SongNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
