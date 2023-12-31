package matthewarmstr.taskmanager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Task {
    public String name = "";
    public String size = "";
    public String description = "";
    public LocalDateTime startTime;
    public long timeElapsed = 0;

    private long calculateElapsedTime(String stopTimestamp) {
        // Calculate time between start and stop timestamps
        LocalDateTime stopTime = LocalDateTime.parse(stopTimestamp);
        return startTime.until(stopTime, ChronoUnit.MICROS);
    }

    public Task(String name) {
        this.name = name;
    }

    public void start(String startTimestamp) {
        startTime = LocalDateTime.parse(startTimestamp);
    }

    public void stop(String stopTimestamp) {
        timeElapsed += calculateElapsedTime(stopTimestamp);
    }
}
