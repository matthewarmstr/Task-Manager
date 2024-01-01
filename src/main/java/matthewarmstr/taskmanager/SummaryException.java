package matthewarmstr.taskmanager;

public class SummaryException extends Exception {
    String message;

    public SummaryException() {}

    public SummaryException(String message) {
        this.message = message;
    }
}
