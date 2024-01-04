package matthewarmstr.taskmanager;

import java.util.Scanner;

public class TerminalDriver {
    private static final LogWriter logWriter = LogWriter.getInstance();
    private static final Summarizer summarizer = Summarizer.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    private static TerminalDriver instance;

    public static TerminalDriver getInstance() {
        if (instance == null) {
            instance = new TerminalDriver();
        }
        return instance;
    }

    public boolean processUserCommandLine() {
        String userInputLine = collectInputString();
        if (userInputLine.equals("exit")) { return false; }
        if (userInputLine.isEmpty()) { return true; }

        // Scan command line and check that input is formatted correctly
        String[] userArgs = userInputLine.split(" ");
        try {
            UsageChecker.checkCorrectCommandUsage(userArgs);

            if (userArgs[0].equals("summary")){
                // Create and print appropriate summary
                summarizer.selectSummaryType(userArgs);
            } else {
                // Process user input
                logWriter.logUserInputLine(userArgs);
            }
        } catch (MalformedInputException e) { System.out.println(e.message); }
        catch (SummaryException e) { System.out.println(e.message); }

        return true;
    }

    public String processSortingMethod() {
        // Get sorting method from user
        return getSortingMethod();
    }

    private String collectInputString() {
        System.out.print(">> ");
        return scanner.nextLine();
    }

    private String getSortingMethod() {
        String sortingMethods;
        while (true) {
            System.out.println(getSortingOptionsString());
            String userInputLine = collectInputString();
            if (userInputLine.isEmpty()) { continue; }
            String[] userArgs = userInputLine.split(" ");

            // Don't advance until user indicates valid sorting method
            try {
                UsageChecker.checkIfValidSortingMethod(userArgs);
                sortingMethods = userInputLine;
                break;
            } catch (SummaryException e) { System.out.println(e.message); }
        }
        return sortingMethods;
    }

    private String getSortingOptionsString() {
        return """
            Select sorting method from these options (up --> ascending, down --> descending):
              name {up|down}    | "Sort by task name alphabetically (uppercase letters sequenced before lowercase letters in ascending order)"
              size {up|down}    | "Sort by task size"
              time {up|down}    | "Sort by time elapsed on each task\"""";
    }
}
