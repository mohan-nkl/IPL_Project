package utils;

import models.Match;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public static List<Match> loadMatches() {
        List<Match> matches = new ArrayList<>();
        List<String[]> linesToParts = readCsvFile("matches.csv");
        for (String[] parts: linesToParts) {
            matches.add(new Match(
                    getInt(parts, 0), getInt(parts, 1),
                    getString(parts, 2), getString(parts, 3), getString(parts, 4), getString(parts, 5),
                    getString(parts, 6), getString(parts, 7), getString(parts, 8),
                    getInt(parts, 9), getString(parts, 10), getInt(parts, 11), getInt(parts, 12),
                    getString(parts, 13), getString(parts, 14),getString(parts, 15), getString(parts, 16),
                    getString(parts, 17)

            ));
        }
        return matches;
    }

    private static List<String[]> readCsvFile(String fileName) {
        List<String[]> linesToParts = new ArrayList<>();
        try {
            InputStream inputStream = CsvReader.class.getClassLoader().getResourceAsStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                linesToParts.add(line.split(","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linesToParts;
    }

    private static int getInt(String[] parts, int index) {
        return parts.length > index && !parts[index].isEmpty() ? Integer.parseInt(parts[index]) : 0;
    }

    private static String getString(String[] parts, int index) {
        return parts.length > index ? parts[index] : "";
    }
}
