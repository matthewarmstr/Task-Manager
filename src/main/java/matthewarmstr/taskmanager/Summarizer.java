package matthewarmstr.taskmanager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Summarizer {
    private static Summarizer instance;
    private static final TaskBuilder taskbuilder = TaskBuilder.getInstance();

    public static Summarizer getInstance() {
        if (instance == null) {
            instance = new Summarizer();
        }
        return instance;
    }

    public void selectSummaryType(String[] userArgs) throws SummaryException {
        List<Task> tasks = taskbuilder.buildTasks();
        if (userArgs.length == 1) {
            // General summary of all tasks requested
            generateMultipleTaskSummary(tasks, List.of("", "S", "M", "L", "XL"));
        } else if (getTask(tasks, userArgs[1]) != null) {
            // Summary of specific task in list was requested
            printSummaryForSingleTask(getTask(tasks, userArgs[1]));
        } else if (UsageChecker.isValidSize(userArgs[1])) {
            // Summary for valid size was requested
            generateMultipleTaskSummary(tasks, List.of(userArgs[1]));
        } else {
            throw new SummaryException("'" + userArgs[1] + "' is not a name for a task, and is not a valid size.");
        }
    }

    private Task getTask(List<Task> tasks, String taskNameToFind) {
        // Get task from list of tasks with matching name
        for (Task task : tasks) {
            if (task.name.equals(taskNameToFind)) { return task; }
        }
        return null;
    }

    private void printSummaryForSingleTask(Task task) {
        System.out.println("Summary for task\t: " + task.name);
        System.out.println("Size of task\t\t: " + task.size);
        System.out.println("Description\t\t\t: " + task.description);
        System.out.println("Total Time on task\t: " + convertTimeToFormat(task.timeElapsed));
        System.out.println();
    }

    private void generateMultipleTaskSummary(List<Task> tasks, List<String> sizesToSummarize) {
        // Create collection of tasks to display in summary
        List<Task> tasksToSummarize = tasks.stream().filter(task -> sizesToSummarize.contains(task.size))
                                                    .toList();

        // Print summaries for all these tasks, along with total time across summarized tasks
        for (Task task : tasksToSummarize) { printSummaryForSingleTask(task); }
        if (tasksToSummarize.size() > 1) { System.out.println("Total time spent on displayed tasks: " + totalTaskTimeString(tasksToSummarize.stream()) + "\n"); }

        // Collect statistics for tasks within each sizing group
        for (String currentSize : sizesToSummarize) {
            ArrayList<Task> tasksOfSameSize = new ArrayList<>();
            for (Task task : tasksToSummarize) {
                if (task.size.equals(currentSize)) { tasksOfSameSize.add(task); }
            }
            printSizeStats(tasksOfSameSize, currentSize);
        }
    }

    private void printSizeStats(List<Task> tasksOfSameSize, String taskSize) {
        // Print additional statistics for sizing groups with more than one task
        if (tasksOfSameSize.size() > 1) {
            String taskSizeToPrint = taskSize;
            if (taskSize.isEmpty()) { taskSizeToPrint = "(empty)"; }
            System.out.println("Statistics for size\t| " + taskSizeToPrint);
            System.out.println("Total time\t\t\t| " + totalTaskTimeString(tasksOfSameSize.stream()));
            System.out.println("Min time\t\t\t| " + minTaskTimeString(tasksOfSameSize.stream()));
            System.out.println("Max time\t\t\t| " + maxTaskTimeString(tasksOfSameSize.stream()));
            System.out.println("Average time\t\t| " + averageTaskTimeString(tasksOfSameSize.stream(), tasksOfSameSize.size()));
            System.out.println();
        }
    }

    private String totalTaskTimeString(Stream<Task> tasks) {
        return convertTimeToFormat(getTotalTaskTime(tasks));
    }

    private String minTaskTimeString(Stream<Task> tasks) {
        return convertTimeToFormat(getMinTaskTime(tasks));
    }

    private String maxTaskTimeString(Stream<Task> tasks) {
        return convertTimeToFormat(getMaxTaskTime(tasks));
    }

    private String averageTaskTimeString(Stream<Task> tasks, long numTasks) {
        return convertTimeToFormat(getAverageTime(tasks, numTasks));
    }

    private long getTotalTaskTime(Stream<Task> tasks) {
        return tasks.mapToLong(task -> task.timeElapsed).sum();
    }

    private long getMinTaskTime(Stream<Task> tasks) {
        Optional<Task> taskWithMinTime = tasks.min(Comparator.comparingLong(task -> task.timeElapsed));
        return taskWithMinTime.map(task -> task.timeElapsed).orElse(0L);
    }

    private long getMaxTaskTime(Stream<Task> tasks) {
        Optional<Task> taskWithMaxTime = tasks.max(Comparator.comparingLong(task -> task.timeElapsed));
        return taskWithMaxTime.map(task -> task.timeElapsed).orElse(0L);
    }

    private long getAverageTime(Stream<Task> tasks, long numTasks) {
        return getTotalTaskTime(tasks) / numTasks;
    }

    private String convertTimeToFormat(long microseconds) {
        long seconds = microseconds / 1000000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        // Handle hours that are over 2 digits (minimum requirement)
        String printedHours;
        if (hours > 99) {
            printedHours = hours + "";
        } else {
            printedHours = String.format("%02d", hours);
        }
        return printedHours + ":" + String.format("%02d", (minutes % 60)) + ":" + String.format("%02d", (seconds % 60));
    }
}
