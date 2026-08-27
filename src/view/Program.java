package view;

import util.Inputter;

public class Program {

    public static void main(String[] args) {
        SongView songView = new SongView();
        PlaylistView playlistView = new PlaylistView();

        int choice;
        do {
            System.out.println();
            System.out.println("========== MUSIC LIBRARY MANAGEMENT ==========");
            System.out.println("1. Song management");
            System.out.println("2. Playlist management");
            System.out.println("0. Exit");
            choice = Inputter.getAnInteger("Enter your choice: ", "Invalid choice, please try again: ", 0, 2);

            switch (choice) {
                case 1:
                    songView.showMenu();
                    break;
                case 2:
                    playlistView.showMenu();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
            }
        } while (choice != 0);
    }
}
