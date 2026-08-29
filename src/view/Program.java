package view;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import util.Inputter;

public class Program {

    private static final int BOX_GAP = 3;

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));

        SongView songView = new SongView();
        PlaylistView playlistView = new PlaylistView();
        PlaybackView playbackView = new PlaybackView();

        int choice;
        do {
            printMainMenu();
            choice = Inputter.getAnInteger(
                    AnsiColors.colorize("==>", AnsiColors.LABEL) + " Enter your selection: ",
                    "Invalid choice, please try again: ", 0, 21);
            dispatch(choice, songView, playlistView, playbackView);
        } while (choice != 0);
    }

    private static void printMainMenu() {
        String[][] songItems = {
                {"1", "Add song"},
                {"2", "View song list"},
                {"3", "View song detail"},
                {"4", "Update song"},
                {"5", "Delete song"},
                {"6", "Search song"},
                {"7", "Sort song list"},
                {"8", "Mark/Unmark favorite"},
        };

        String[][] playlistItems = {
                {"9", "Create playlist"},
                {"10", "View playlist list"},
                {"11", "View playlist detail"},
                {"12", "Update playlist"},
                {"13", "Delete playlist"},
                {"14", "Add song to playlist"},
                {"15", "Remove song from playlist"},
                {"16", "Search songs in playlist"},
                {"17", "Undo last add/remove song"},
                {"18", "Redo last undone action"},
        };

        String[][] playbackItems = {
                {"19", "Play a song"},
                {"20", "Play a playlist"},
                {"21", "View recently played"},
        };

        int sharedWidth = Math.max(MenuBoxRenderer.computeContentWidth(songItems),
                Math.max(MenuBoxRenderer.computeContentWidth(playlistItems),
                        MenuBoxRenderer.computeContentWidth(playbackItems)));
        int sharedRows = Math.max(songItems.length, Math.max(playlistItems.length, playbackItems.length));

        List<String> songBox = MenuBoxRenderer.buildBox("SONG MANAGEMENT", songItems, sharedWidth, sharedRows);
        List<String> playlistBox = MenuBoxRenderer.buildBox("PLAYLIST MANAGEMENT", playlistItems, sharedWidth, sharedRows);
        List<String> playbackBox = MenuBoxRenderer.buildBox("PLAYBACK", playbackItems, sharedWidth, sharedRows);
        List<String> systemBox = MenuBoxRenderer.buildBox("SYSTEM", new String[][]{{"0", "Exit"}}, 0, 1);

        int totalWidth = 3 * MenuBoxRenderer.outerWidth(sharedWidth) + 2 * BOX_GAP;

        System.out.println();
        System.out.println(AnsiColors.colorize(MenuBoxRenderer.buildTitleBar("MUSIC LIBRARY MANAGEMENT", totalWidth), AnsiColors.LABEL));
        System.out.println();

        MenuBoxRenderer.printRow(Arrays.asList(songBox, playlistBox, playbackBox), BOX_GAP);
        System.out.println();
        MenuBoxRenderer.printRow(Arrays.asList(systemBox), BOX_GAP);
    }

    private static void dispatch(int choice, SongView songView, PlaylistView playlistView, PlaybackView playbackView) {
        if (choice >= 1 && choice <= 8) {
            songView.handleChoice(choice);
        } else if (choice >= 9 && choice <= 18) {
            playlistView.handleChoice(choice - 8);
        } else if (choice >= 19 && choice <= 21) {
            playbackView.handleChoice(choice - 18);
        } else if (choice == 0) {
            System.out.println(AnsiColors.colorize("Goodbye!", AnsiColors.SUCCESS));
        }
    }
}
