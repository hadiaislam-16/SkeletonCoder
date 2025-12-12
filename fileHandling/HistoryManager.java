package com.skeletoncoder.project.fileHandling;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {

    private static final String FILE_NAME = "history.ser";
    private static List<String> history = loadHistory();


    public static void addHistory(String input) {
        history.add(input);
    }


    public static void saveHistoryToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(history);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<String> loadHistory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<String>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
