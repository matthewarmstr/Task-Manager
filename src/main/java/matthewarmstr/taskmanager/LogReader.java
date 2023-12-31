package matthewarmstr.taskmanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LogReader {
    private static final String LOG_FILE = "./logs/TM.log";

    /* Methods required for the reader to be singleton */
    private static LogReader instance;
    private FileReader fileReader;

    private BufferedReader bufferedReader;

    public LogReader() {
        // Open log file if it exists
        try {
            fileReader = new FileReader(LOG_FILE);
            bufferedReader = new BufferedReader(fileReader);
        } catch (IOException e) {}
    }

    public static LogReader getInstance() {
        if (instance == null) {
            instance = new LogReader();
        }
        return instance;
    }

    public ArrayList<String[]> readLog() throws IOException{
        ArrayList<String[]> listOfLogLines = new ArrayList<String[]>();
        String line;
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
}
