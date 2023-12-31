package matthewarmstr.taskmanager;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class TM {
    public static void main(String[] args) {
        LogWriter logWriter = LogWriter.getInstance();
        Summarizer summarizer = Summarizer.getInstance();

        // Scan command line and check that input is formatted correctly
        try {
            UsageChecker.checkCorrectUsage(args);
            if (args[0].equals("summary")){
                // Create and print appropriate summary
                summarizer.selectSummaryType(args);
            } else {
                // Process user input
                logWriter.processUserInputLine(args);
            }
        } catch (UsageException e) { }
    }
}