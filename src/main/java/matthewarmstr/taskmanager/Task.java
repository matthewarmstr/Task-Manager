package matthewarmstr.taskmanager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Task {
    public String name = "";
    public String size = "";
    public String description = "";
    public long timeElapsed = 0;
    private LocalDateTime startTime;

    public Task(String name) {
        this.name = name;
    }

    public void start(String startTimestamp) {
        startTime = LocalDateTime.parse(startTimestamp);
    }

    public void stop(String stopTimestamp) {
        timeElapsed += calculateElapsedTime(LocalDateTime.parse(stopTimestamp));
    }

    public void forceTimeUpdate() {
        LocalDateTime timeNow = LocalDateTime.now();
        timeElapsed += calculateElapsedTime(timeNow);
        startTime = timeNow;
    }

    private long calculateElapsedTime(LocalDateTime stopTimestamp) {
        // Calculate time between start and stop timestamps
        return startTime.until(stopTimestamp, ChronoUnit.MICROS);
    }
}
