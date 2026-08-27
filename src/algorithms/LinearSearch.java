package algorithms;

import java.util.ArrayList;
import java.util.List;
import model.Song;

public class LinearSearch {

    public static List<Song> searchByTitle(List<Song> songs, String title) {
        List<Song> result = new ArrayList<>();
        String keyword = title == null ? "" : title.trim().toLowerCase();
        for (Song song : songs) {
            if (song.getTitle() != null && song.getTitle().toLowerCase().contains(keyword)) {
                result.add(song);
            }
        }
        return result;
    }

    public static List<Song> searchByArtist(List<Song> songs, String artist) {
        List<Song> result = new ArrayList<>();
        String keyword = artist == null ? "" : artist.trim().toLowerCase();
        for (Song song : songs) {
            if (song.getArtist() != null && song.getArtist().toLowerCase().contains(keyword)) {
                result.add(song);
            }
        }
        return result;
    }

    public static List<Song> searchByGenre(List<Song> songs, String genre) {
        List<Song> result = new ArrayList<>();
        String keyword = genre == null ? "" : genre.trim().toLowerCase();
        for (Song song : songs) {
            if (song.getGenre() != null && song.getGenre().toLowerCase().contains(keyword)) {
                result.add(song);
            }
        }
        return result;
    }
}
