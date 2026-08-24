package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class FileHandler<T extends Persistable> {

    public boolean load(String pathFile, List<T> list) {
        File f = new File(pathFile);
        if (!f.exists()) {
            return true;
        }
        list.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    T item = handleLine(line);
                    if (item != null) {
                        list.add(item);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error read file text: " + e.getMessage());
            return false;
        }
    }

    public boolean save(List<T> list, String pathFile) {
        if (list == null) {
            return false;
        }
        File f = new File(pathFile);
        if (f.getParentFile() != null) {
            f.getParentFile().mkdirs();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
            for (T item : list) {
                writer.write(item.toDataString());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error save file text: " + e.getMessage());
            return false;
        }
    }

    public abstract T handleLine(String line);
}
