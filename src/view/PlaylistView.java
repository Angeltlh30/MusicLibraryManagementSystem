package view;

import controller.PlaylistController;
import java.util.Collections;
import java.util.List;
import model.Playlist;
import model.Song;
import util.Inputter;
import util.PlaylistNotFoundException;
import util.SongNotFoundException;

public class PlaylistView {

    private PlaylistController playlistController;

    private static final String[] TABLE_HEADERS = {"ID", "Name", "Description", "Song count"};

    public PlaylistView() {
        this.playlistController = new PlaylistController();
    }

    public void showMenu() {
        int choice;
        do {
            printMenu();
            choice = Inputter.getAnInteger("Enter your choice: ", "Invalid choice, please try again: ", 0, 10);
            handleChoice(choice);
        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println();
        System.out.println("========== PLAYLIST MANAGEMENT ==========");
        System.out.println("1. Create playlist");
        System.out.println("2. View playlist list");
        System.out.println("3. View playlist detail");
        System.out.println("4. Update playlist");
        System.out.println("5. Delete playlist");
        System.out.println("6. Add song to playlist");
        System.out.println("7. Remove song from playlist");
        System.out.println("8. Search songs in playlist");
        System.out.println("9. Undo last add/remove song");
        System.out.println("10. Redo last undone action");
        System.out.println("0. Back");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                createPlaylist();
                break;
            case 2:
                listPlaylists();
                break;
            case 3:
                viewPlaylistDetail();
                break;
            case 4:
                updatePlaylist();
                break;
            case 5:
                deletePlaylist();
                break;
            case 6:
                addSongToPlaylist();
                break;
            case 7:
                removeSongFromPlaylist();
                break;
            case 8:
                searchSongsInPlaylist();
                break;
            case 9:
                undoAction();
                break;
            case 10:
                redoAction();
                break;
            case 0:
                System.out.println("Back to main menu.");
                break;
        }
    }

    private void createPlaylist() {
        String name = promptUniquePlaylistName(null, null);
        String description = Inputter.getAString("Enter description (optional): ");

        try {
            Playlist playlist = playlistController.createPlaylist(name, description);
            System.out.println("Playlist created successfully, id = " + playlist.getId());
            printPlaylistsTable(Collections.singletonList(playlist));
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listPlaylists() {
        List<Playlist> playlists = playlistController.getAllPlaylists();
        if (playlists.isEmpty()) {
            System.out.println("No playlists yet.");
            return;
        }
        printPlaylistsTable(playlists);
    }

    private void viewPlaylistDetail() {
        int id = Inputter.getAnPositiveInteger("Enter playlist id: ", "Id must be a positive integer: ");
        try {
            Playlist playlist = playlistController.getPlaylistById(id);
            printPlaylistsTable(Collections.singletonList(playlist));
            printSongsInPlaylist(id);
        } catch (PlaylistNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updatePlaylist() {
        int id = Inputter.getAnPositiveInteger("Enter id of the playlist to update: ", "Id must be a positive integer: ");
        Playlist existing;
        try {
            existing = playlistController.getPlaylistById(id);
        } catch (PlaylistNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Current playlist info:");
        printPlaylistsTable(Collections.singletonList(existing));
        System.out.println("Press Enter to keep the current value for a field.");

        String name = promptUniquePlaylistName(existing.getName(), existing.getId());
        String description = Inputter.getAString("Enter new description (current: " + existing.getDescription() + "): ");

        try {
            Playlist updated = playlistController.updatePlaylist(id, name, description);
            System.out.println("Updated successfully.");
            printPlaylistsTable(Collections.singletonList(updated));
        } catch (PlaylistNotFoundException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String promptUniquePlaylistName(String currentName, Integer excludeId) {
        boolean isUpdate = currentName != null;
        while (true) {
            String name;
            if (isUpdate) {
                name = Inputter.getAString("Enter new name (current: " + currentName + "): ");
                if (name == null) {
                    return null;
                }
            } else {
                name = Inputter.getAString("Enter playlist name: ", "Playlist name must not be empty: ");
            }

            if (playlistController.isPlaylistNameTaken(name, excludeId)) {
                if (isUpdate) {
                    System.out.println("Error: Playlist name \"" + name + "\" already exists. "
                            + "Enter a different name, or press Enter to keep the current name (" + currentName + ").");
                } else {
                    System.out.println("Error: Playlist name \"" + name + "\" already exists. Please enter a different name.");
                }
                continue;
            }
            return name;
        }
    }

    private void deletePlaylist() {
        int id = Inputter.getAnPositiveInteger("Enter id of the playlist to delete: ", "Id must be a positive integer: ");
        Playlist existing;
        try {
            existing = playlistController.getPlaylistById(id);
        } catch (PlaylistNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("You are about to delete:");
        printPlaylistsTable(Collections.singletonList(existing));
        boolean confirmed = Inputter.getYesNo("Are you sure you want to delete this playlist? (Y/N): ", "Please enter Y or N: ");
        if (!confirmed) {
            System.out.println("Delete cancelled.");
            return;
        }

        try {
            playlistController.deletePlaylist(id);
            System.out.println("Deleted successfully.");
        } catch (PlaylistNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addSongToPlaylist() {
        int playlistId = Inputter.getAnPositiveInteger("Enter playlist id: ", "Id must be a positive integer: ");
        try {
            playlistController.getPlaylistById(playlistId);
        } catch (PlaylistNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        int songId = Inputter.getAnPositiveInteger("Enter song id to add: ", "Id must be a positive integer: ");

        try {
            playlistController.addSongToPlaylist(playlistId, songId);
            System.out.println("Song added to playlist successfully.");
            printSongsInPlaylist(playlistId);
        } catch (PlaylistNotFoundException | SongNotFoundException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeSongFromPlaylist() {
        int playlistId = Inputter.getAnPositiveInteger("Enter playlist id: ", "Id must be a positive integer: ");
        Playlist playlist;
        try {
            playlist = playlistController.getPlaylistById(playlistId);
        } catch (PlaylistNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        if (playlist.getSongCount() == 0) {
            System.out.println("This playlist has no songs to remove.");
            return;
        }

        int songId = Inputter.getAnPositiveInteger("Enter song id to remove: ", "Id must be a positive integer: ");

        try {
            playlistController.removeSongFromPlaylist(playlistId, songId);
            System.out.println("Song removed from playlist successfully.");
            printSongsInPlaylist(playlistId);
        } catch (PlaylistNotFoundException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void searchSongsInPlaylist() {
        int playlistId = Inputter.getAnPositiveInteger("Enter playlist id: ", "Id must be a positive integer: ");
        try {
            playlistController.getPlaylistById(playlistId);
        } catch (PlaylistNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("Search by:");
        System.out.println("1. Title");
        System.out.println("2. Artist");
        System.out.println("3. Genre");
        int type = Inputter.getAnInteger("Enter your choice: ", "Invalid choice, please try again: ", 1, 3);
        String keyword = Inputter.getAString("Enter keyword: ", "Keyword must not be empty: ");

        List<Song> result;
        try {
            switch (type) {
                case 1:
                    result = playlistController.searchSongsInPlaylistByTitle(playlistId, keyword);
                    break;
                case 2:
                    result = playlistController.searchSongsInPlaylistByArtist(playlistId, keyword);
                    break;
                default:
                    result = playlistController.searchSongsInPlaylistByGenre(playlistId, keyword);
                    break;
            }
        } catch (PlaylistNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        if (result.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }
        ConsoleTablePrinter.printTable(SongView.TABLE_HEADERS, SongView.buildRows(result));
    }

    private void undoAction() {
        try {
            String message = playlistController.undo();
            System.out.println(message);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (PlaylistNotFoundException | SongNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void redoAction() {
        try {
            String message = playlistController.redo();
            System.out.println(message);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        } catch (PlaylistNotFoundException | SongNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void printSongsInPlaylist(int playlistId) {
        try {
            List<Song> songs = playlistController.getSongsInPlaylist(playlistId);
            if (songs.isEmpty()) {
                System.out.println("This playlist has no songs yet.");
                return;
            }
            System.out.println("Songs in this playlist:");
            ConsoleTablePrinter.printTable(SongView.TABLE_HEADERS, SongView.buildRows(songs));
        } catch (PlaylistNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void printPlaylistsTable(List<Playlist> playlists) {
        String[][] rows = new String[playlists.size()][TABLE_HEADERS.length];
        for (int i = 0; i < playlists.size(); i++) {
            Playlist p = playlists.get(i);
            rows[i][0] = String.valueOf(p.getId());
            rows[i][1] = p.getName();
            rows[i][2] = p.getDescription();
            rows[i][3] = String.valueOf(p.getSongCount());
        }
        ConsoleTablePrinter.printTable(TABLE_HEADERS, rows);
    }
}
