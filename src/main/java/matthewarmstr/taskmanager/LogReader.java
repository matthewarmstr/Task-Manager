package matthewarmstr.taskmanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LogReader {
    private static final String LOG_FILE = "./logs/TM.log";

    private static LogReader instance;

    private BufferedReader bufferedReader;

    public static LogReader getInstance() {
        if (instance == null) {
            instance = new LogReader();
        }
        return instance;
    }

    public ArrayList<String[]> readAllLinesFromLog() {
        ArrayList<String[]> listOfLogLines = new ArrayList<>();
        String line;
        prepareLogReader();
        try {
            while ((line = bufferedReader.readLine()) != null) {
                // Store all lines from log
                String[] lineArgs = line.split("\t");
                listOfLogLines.add(lineArgs);
            }
        } catch (IOException e) {
            System.out.println("Error reading " + LOG_FILE);
        }
        return listOfLogLines;
    }

    private void prepareLogReader() {
        // Open log file if it exists and always start reading from the top
        try {
            FileReader fileReader = new FileReader(LOG_FILE);
            bufferedReader = new BufferedReader(fileReader);
        } catch (IOException e) {
            System.out.println("Error occurred when preparing to read " + LOG_FILE);
        }
    }
}
