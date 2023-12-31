package matthewarmstr.taskmanager;

import java.util.ArrayList;

public class Summarizer {
    /* Methods required for the writer to be singleton */
    private static Summarizer instance;
    private static TaskBuilder taskbuilder = TaskBuilder.getInstance();

    public static Summarizer getInstance() {
        if (instance == null) {
            instance = new Summarizer();
        }
        return instance;
    }

    public void selectSummaryType(String[] args) throws UsageException {
        ArrayList<Task> taskList = taskbuilder.buildTasks();

        // Check if general summary requested
        if (args.length == 1) {
            // Complete general summary
            summarizeAllTasks(taskList);
        } else if (getTask(taskList, args[1]) != null) {
            // Provide summary of specified task if its in task list
            summarizeSingleTask(getTask(taskList, args[1]));

        } else if (UsageChecker.isValidSize(args[1])) {
            // Summarize tasks of specified size
            summarizeTasksBySingleSize(taskList, args[1]);
        } else {
            throw new UsageException();
        }
    }

    private Task getTask(ArrayList<Task> tasks, String taskNameToFind) {
        for (Task task : tasks) {
            if (task.name.equals(taskNameToFind)) {
                return task;
            }
        }
        return null;
    }

    private void summarizeSingleTask(Task task) {
        System.out.println("Summary for task\t: " + task.name);
        System.out.println("Size of task\t\t: " + task.size);
        System.out.println("Description\t\t: " + task.description);
        System.out.println("Total Time on task\t: "
                + convertTimeToFormat(task.timeElapsed));
        System.out.println();
    }

    private void summarizeAllTasks(ArrayList<Task> tasks) {
        for (Task task : tasks) {
            summarizeSingleTask(task);
        }

        if (tasks.size() > 0) {
            // Print total time (FORMAT: HH:MM:SS)
            System.out.println("Total time spent on all tasks: " + displayTotalTaskTime(tasks));
            System.out.println();
        }

        // Collect and print statistics on tasks within sizing groups
        for (String validSize : new String[]{"S", "M", "L", "XL"}) {
            ArrayList<Task> tasksOfSameSize = new ArrayList<Task>();
            for (Task task : tasks) {
                if (task.size.equals(validSize)) {
                    tasksOfSameSize.add(task);
                }
            }
            printSizeStats(tasksOfSameSize);
        }
    }

    private void summarizeTasksBySingleSize(ArrayList<Task> tasks, String size) {
        ArrayList<Task> tasksOfSameSize = new ArrayList<Task>();
        for (Task task : tasks) {
            if (task.size.equals(size)) {
                tasksOfSameSize.add(task);
                summarizeSingleTask(task);
            }
        }
        printSizeStats(tasksOfSameSize);
    }

    private void printSizeStats(ArrayList<Task> tasksOfSameSize) {
        // Print additional statistics for tasks of grouped size as long as
        // there is more than one such task
        if (tasksOfSameSize.size() > 1) {
            String size = tasksOfSameSize.get(0).size;
            System.out.println("Statistics for size\t| " + size);
            System.out.println("Total time\t\t| "
                    + displayTotalTaskTime(tasksOfSameSize));
            System.out.println("Min time\t\t| "
                    + displayMinTaskTime(tasksOfSameSize));
            System.out.println("Max time\t\t| "
                    + displayMaxTaskTime(tasksOfSameSize));
            System.out.println("Average time\t\t| "
                    + displayAverageTaskTime(tasksOfSameSize));
            System.out.println();
        }
    }

    private long getTotalTaskTime(ArrayList<Task> tasks) {
        long totalTime = 0;
        for (Task task : tasks) {
            totalTime += task.timeElapsed;
        }
        return totalTime;
    }

    private long getMinTaskTime(ArrayList<Task> tasks) {
        long minTime = tasks.get(0).timeElapsed;
        for (Task task : tasks) {
            if (task.timeElapsed < minTime) {
                minTime = task.timeElapsed;
            }
        }
        return minTime;
    }

    private long getMaxTaskTime(ArrayList<Task> tasks) {
        long maxTime = tasks.get(0).timeElapsed;
        for (Task task : tasks) {
            if (task.timeElapsed > maxTime) {
                maxTime = task.timeElapsed;
            }
        }
        return maxTime;
    }

    private long getAverageTime(ArrayList<Task> tasks) {
        return getTotalTaskTime(tasks) / tasks.size();
    }

    private String displayTotalTaskTime(ArrayList<Task> tasks) {
        return convertTimeToFormat(getTotalTaskTime(tasks));
    }

    private String displayMinTaskTime(ArrayList<Task> tasks) {
        return convertTimeToFormat(getMinTaskTime(tasks));
    }

    private String displayMaxTaskTime(ArrayList<Task> tasks) {
        return convertTimeToFormat(getMaxTaskTime(tasks));
    }

    private String displayAverageTaskTime(ArrayList<Task> tasks) {
        return convertTimeToFormat(getAverageTime(tasks));
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
