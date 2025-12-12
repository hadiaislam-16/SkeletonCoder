package com.skeletoncoder.project.fileHandling;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileInputLoader {
    public static String loadInput(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath))).trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
