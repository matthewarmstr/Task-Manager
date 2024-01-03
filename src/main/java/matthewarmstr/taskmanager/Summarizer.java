package matthewarmstr.taskmanager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
            generateTaskSummary(tasks, List.of("NONE", "S", "M", "L", "XL"));
        } else if (getTask(tasks, userArgs[1]) != null) {
            // Summary of specific task in list was requested
            printSummaryForSingleTask(getTask(tasks, userArgs[1]));
        } else if (UsageChecker.isValidSize(userArgs[1])) {
            // Summary for valid size was requested
            generateTaskSummary(tasks, List.of(userArgs[1]));
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

    private void generateTaskSummary(List<Task> tasks, List<String> sizesToSummarize) {
        // Create collection of tasks to display in summary
        List<Task> tasksToSummarizeUnsorted = getUnsortedTasksToSummarize(tasks, sizesToSummarize);

        // If more than one task to summarize, order tasks as specified by sorting method from user
        List<Task>  tasksToSummarize = tasksToSummarizeUnsorted;
        if (tasksToSummarizeUnsorted.size() > 1) {
            try {
                tasksToSummarize = getSortedTasksToSummarize(tasksToSummarizeUnsorted);
            } catch (SummaryException e) { System.out.println(e.message); }
        }

        // Print summaries for all these tasks, along with total time across summarized tasks
        for (Task task : tasksToSummarize) { printSummaryForSingleTask(task); }
        if (tasksToSummarize.size() > 1) { printAllDisplayedTasksStats(tasksToSummarize); }

        // Collect statistics for tasks within each sizing group
        processSizeStats(tasksToSummarize, sizesToSummarize);
    }

    private List<Task> getUnsortedTasksToSummarize(List<Task> tasks, List<String> sizes) {
        ArrayList<Task> unsortedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (sizes.contains(getTaskSizeString(task))) {
                unsortedTasks.add(task);
            }
        }
        return unsortedTasks;
    }

    private List<Task> getSortedTasksToSummarize(List<Task> tasksUnsorted) throws SummaryException {
        // Wait for user response for sorting methods
        TerminalDriver terminal = TerminalDriver.getInstance();
        String sortingMethods = terminal.processSortingMethod();

        // Sort tasks to summarize as requested
        return sortSummarizedTaskList(tasksUnsorted, sortingMethods);
    }

    private List<Task> sortSummarizedTaskList(List<Task> tasks, String sortingParams) throws SummaryException {
        String[] sortingMethodParams = sortingParams.split(" ");
        String sortingTaskItemMethod = sortingMethodParams[0];
        String sortingTaskOrdering = sortingMethodParams[1];

        if (sortingTaskOrdering.equals("up")) {
            // Sort tasks by either their name, size, or time elapsed (ascending)
            return sortTasksAscending(tasks.stream(), sortingTaskItemMethod).collect(Collectors.toList());
        } else if (sortingTaskOrdering.equals("down")) {
            // Sort tasks by either their name, size, or time elapsed (descending)
            return sortTasksDescending(tasks.stream(), sortingTaskItemMethod).collect(Collectors.toList());
        } else {
            throw new SummaryException("Unable to sort tasks.");
        }
    }

    private Stream<Task> sortTasksAscending(Stream<Task>tasksToSort, String sortingItem) throws SummaryException {
        return switch (sortingItem) {
            case "name" ->
                    tasksToSort.sorted(Comparator.comparing(task -> task.name));
            case "size" ->
                    tasksToSort.sorted(Comparator.comparing(task -> task.size));
            case "time" ->
                    tasksToSort.sorted(Comparator.comparing(task -> task.timeElapsed));
            default -> throw new SummaryException("Unable to sort tasks in ascending order.");
        };
    }

    private Stream<Task> sortTasksDescending(Stream<Task>tasksToSort, String sortingItem) throws SummaryException {
        return switch (sortingItem) {
            case "name" ->
                    tasksToSort.sorted(Comparator.comparing(task -> task.name, Comparator.reverseOrder()));
            case "size" ->
                    tasksToSort.sorted(Comparator.comparing(task -> task.size, Comparator.reverseOrder()));
            case "time" ->
                    tasksToSort.sorted(Comparator.comparing(task -> task.timeElapsed, Comparator.reverseOrder()));
            default -> throw new SummaryException("Unable to sort tasks in descending order.");
        };
    }

    private void processSizeStats(List<Task> tasksToSummarize, List<String> sizesToSummarize) {
        for (String currentSize : sizesToSummarize) {
            ArrayList<Task> tasksOfSameSize = new ArrayList<>();
            for (Task task : tasksToSummarize) {
                if (getTaskSizeString(task).equals(currentSize)) { tasksOfSameSize.add(task); }
            }
            printSizeStats(tasksOfSameSize);
        }
    }

    private String getTaskSizeString(Task task) {
        return String.valueOf(task.size);
    }

    private void printSummaryForSingleTask(Task task) {
        Size taskSize = task.size;
        String taskSizeToPrint = String.valueOf(taskSize);
        if (taskSize == Size.NONE) { taskSizeToPrint = ""; }
        System.out.println("Summary for task     : " + task.name);
        System.out.println("Size of task         : " + taskSizeToPrint);
        System.out.println("Description          : " + task.description);
        System.out.println("Total Time on task   : " + convertTimeToFormat(task.timeElapsed));
        System.out.println();
    }

    public void printAllDisplayedTasksStats(List<Task> tasksToSummarize) {
        System.out.println("Total time spent on displayed tasks: " + totalTaskTimeString(tasksToSummarize.stream()) + "\n");
    }

    private void printSizeStats(List<Task> tasksOfSameSize) {
        // Print additional statistics for sizing groups with more than one task
        if (tasksOfSameSize.size() > 1) {
            Size taskSize = tasksOfSameSize.getFirst().size;
            String taskSizeToPrint = getTaskSizeString(tasksOfSameSize.getFirst());
            if (taskSize == Size.NONE) { taskSizeToPrint = "(no size)"; }
            System.out.println("Statistics for size  | " + taskSizeToPrint);
            System.out.println("Total time           | " + totalTaskTimeString(tasksOfSameSize.stream()));
            System.out.println("Min time             | " + minTaskTimeString(tasksOfSameSize.stream()));
            System.out.println("Max time             | " + maxTaskTimeString(tasksOfSameSize.stream()));
            System.out.println("Average time         | " + averageTaskTimeString(tasksOfSameSize.stream(), tasksOfSameSize.size()));
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
