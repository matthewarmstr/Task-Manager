package matthewarmstr.taskmanager;

public class TM {
    public static void main(String[] args) {
        TerminalDriver terminal = TerminalDriver.getInstance();

        printWelcomeMessage();
        boolean running = true;
        while (running) {
            running = terminal.processUserCommandLine();
        }
    }

    private static void printWelcomeMessage() {
        System.out.println("\nTask Manager - by Matthew Armstrong");
        System.out.println("(for a list of commands, type \"commands\")\n");
    }
}