package matthewarmstr.taskmanager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogWriter {
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "logs/TM.log";

    private static LogWriter instance;

    public LogWriter() {
        try {
            createLogAndDirIfNeeded();
        } catch (IOException except) {
            System.out.println("Error in creating " + LOG_FILE + " when initializing LogWriter");
        }
    }

    public static LogWriter getInstance() {
        if (instance == null) {
            instance = new LogWriter();
        }
        return instance;
    }

    public void logUserInputLine(String[] args) {
        String taskName = args[1];
        String[] taskArgs = new String[args.length - 1];
        taskArgs[0] = args[0];

        // Skip over user's action and collect its arguments
        for (int i = 2; i < args.length; i++) {
            taskArgs[i-1] = args[i];
        }

        // TM.log LINE FORMAT:
        // [timestamp] (tab) [task name] (tab) [additional args from user]
        String actionArgs = String.join(" ", taskArgs);
        writeLineToLog(java.time.LocalDateTime.now() + "\t" + taskName + "\t" + actionArgs + "\n");
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

    private void writeLineToLog(String output){
        try {
            createLogAndDirIfNeeded();
            FileWriter filewriter = new FileWriter(LOG_FILE, true);
            filewriter.write(output);
            filewriter.close();
        } catch (IOException exception){
            System.out.println("Error occurred when writing to " + LOG_FILE);
        }
    }
}
