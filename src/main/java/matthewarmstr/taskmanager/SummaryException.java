package matthewarmstr.taskmanager;

public class SummaryException extends Exception {
    String message;

    public SummaryException(String message) {
        this.message = message;
    }
}
