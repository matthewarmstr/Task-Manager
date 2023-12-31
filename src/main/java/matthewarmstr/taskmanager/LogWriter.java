package matthewarmstr.taskmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogWriter {
    private static final String LOG_FILE = "./logs/TM.log";

    private static LogWriter instance;

    public LogWriter() {
        // Create the log if it does not exist
        File logFile = new File(LOG_FILE);
        try {
            logFile.createNewFile();
        } catch (IOException except) {
            System.out.println("Error in creation " + LOG_FILE);
        }
    }

    public static LogWriter getInstance() {
        if (instance == null) {
            instance = new LogWriter();
        }
        return instance;
    }

    public void processUserInputLine(String[] args) {
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

    private void writeLineToLog(String output){
        try {
            FileWriter filewriter = new FileWriter(LOG_FILE, true);
            filewriter.write(output);
            filewriter.close();
        } catch (IOException exception){
            System.out.println("Error occured writing to " + LOG_FILE);
        }
    }
}
