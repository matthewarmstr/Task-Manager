package matthewarmstr.taskmanager;

import java.util.Scanner;

public class TerminalDriver {
    private static LogWriter logWriter = LogWriter.getInstance();
    private static Summarizer summarizer = Summarizer.getInstance();
    private static Scanner scanner = new Scanner(System.in);

    private static TerminalDriver instance;

    public static TerminalDriver getInstance() {
        if (instance == null) {
            instance = new TerminalDriver();
        }
        return instance;
    }

    public boolean processUserCommandLine() {
        System.out.print(">> ");
        String userInputLine = scanner.nextLine();
        if (userInputLine.equals("exit")) { return false; }

        // Scan command line and check that input is formatted correctly
        String[] userArgs = userInputLine.split(" ");
        try {
            UsageChecker.checkCorrectUsage(userArgs);
            if (userArgs[0].equals("summary")){
                // Create and print appropriate summary
                summarizer.selectSummaryType(userArgs);
            } else {
                // Process user input
                logWriter.logUserInputLine(userArgs);
            }
        } catch (MalformedInputException e) { System.out.println(e.message); }
        return true;
    }
}
