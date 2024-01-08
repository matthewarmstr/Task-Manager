package matthewarmstr.taskmanager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LogReader {
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "logs/TM.log";

    private static LogReader instance;

    public static LogReader getInstance() {
        if (instance == null) {
            instance = new LogReader();
        }
        return instance;
    }

    public ArrayList<String[]> readAllLinesFromLog() {
        ArrayList<String[]> listOfLogLines = new ArrayList<>();

        try {
            String line;
            createLogAndDirIfNeeded();
            BufferedReader buffReader = Files.newBufferedReader(Paths.get(LOG_FILE));

            while ((line = buffReader.readLine()) != null) {
                // Store all lines from log
                String[] lineArgs = line.split("\t");
                listOfLogLines.add(lineArgs);
            }
        } catch (IOException e) {
            System.out.println("Error in reading buffered reader");
        }

        return listOfLogLines;
    }

    private void createLogAndDirIfNeeded() throws IOException {
        // Create log directory if it doesn't exist
        Path logDirPath = Paths.get(LOG_DIR);
        if (!Files.exists(logDirPath)) {
            Files.createDirectory(logDirPath);
        }

        // Create log file if it doesn't exist
        File logFile = new File(LOG_FILE);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
    }
}
