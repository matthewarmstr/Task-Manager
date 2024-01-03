package matthewarmstr.taskmanager;

import java.util.ArrayList;
import java.util.List;

public class TaskBuilder {
    LogReader logReader = LogReader.getInstance();

    private static final String LOG_FILE = "./logs/TM.log";

    private static TaskBuilder instance;

    private ArrayList<Task> taskList;
    private String runningTaskName = null;

    public static TaskBuilder getInstance() {
        if (instance == null) {
            instance = new TaskBuilder();
        }
        return instance;
    }

    public List<Task> buildTasks() {
        // Prepare new task list
        taskList = new ArrayList<>();

        // Read lines from log file
        List<String[]> listOfLogLines = logReader.readAllLinesFromLog();

        // Convert log lines into equivalent tasks
        processAllLogLines(listOfLogLines);

        // If task is still running after the entire log has been read, force update the time appropriately
        if (runningTaskName != null && getTask(runningTaskName) != null) {
            getTask(runningTaskName).forceTimeUpdate();
            runningTaskName = null;
        }

        return taskList;
    }

    private void processAllLogLines(List<String[]> lines) {
        int lineNum = 1;
        for (String[] line : lines) {
            processLogLine(line, lineNum);
            lineNum++;
        }
    }

    private void processLogLine(String[] line, int lineNum) {
        String timestamp = line[0];
        String taskName = line[1];
        String[] actionArgs = line[2].split(" ");
        String userAction = actionArgs[0];

        // Parse line from log file and update tasks accordingly
        switch (userAction) {
            case "start" -> {
                createTaskIfNotInList(taskName);
                startTask(taskName, timestamp);
            }
            case "stop" -> stopTask(taskName, timestamp);
            case "describe" -> {
                createTaskIfNotInList(taskName);
                updateDescription(getTask(taskName), actionArgs);
            }
            case "size" -> {
                createTaskIfNotInList(taskName);
                getTask(taskName).size = Size.valueOf(actionArgs[1]);
            }
            case "rename" -> {
                if (taskName.equals(runningTaskName)) {
                    runningTaskName = actionArgs[1];
                }
                if (getTask(taskName) != null) {
                    getTask(taskName).name = actionArgs[1];
                }
            }
            case "delete" -> {
                if (taskName.equals(runningTaskName)) {
                    runningTaskName = null;
                }
                deleteTask(taskName);
            }
            default -> System.err.println("Line " + lineNum + " of " + LOG_FILE + " is malformed");
        }
    }

    private Task getTask(String taskName) {
        for (Task task : taskList) {
            if (task.name.equals(taskName)) {
                return task;
            }
        }
        return null;
    }

    private void createTaskIfNotInList(String taskName) {
        // Check if task already exists
        for (Task task : taskList) {
            if (task.name.equals(taskName))  { return; }
        }

        // Create new task object if not already stored in collection
        Task newTask = new Task(taskName);
        taskList.add(newTask);
    }

    private void deleteTask(String taskName) {
        taskList.removeIf(task -> task.name.equals(taskName));
    }

    private void startTask(String taskName, String timestamp) {
        // Check if a task is already running
        if (runningTaskName != null && getTask(runningTaskName) != null) {
            getTask(runningTaskName).stop(timestamp);
        }

        // Start the appropriate task from the collection
        getTask(taskName).start(timestamp);
        runningTaskName = taskName;
    }

    private void stopTask(String taskName, String timestamp) {
        if (taskName.equals(runningTaskName)) {
            getTask(taskName).stop(timestamp);
            runningTaskName = null;
        }
    }

    private void updateDescription(Task task, String[] actionArgs) {
        int actionArgsLength = actionArgs.length;

        // Handle case where user wants to give the task a size when describing it
        if (UsageChecker.isValidSize(actionArgs[actionArgsLength - 1])) {
            task.size = Size.valueOf(actionArgs[actionArgsLength - 1]);
            actionArgs[actionArgsLength - 1] = "";
        }

        // Extract the action
        String[] descriptionArgs = new String[actionArgs.length - 1];
        System.arraycopy(actionArgs, 1, descriptionArgs, 0, actionArgs.length - 1);

        // Store updated task description
        String newDescription;
        if(actionArgs[actionArgsLength - 1].isEmpty()) {
            // If a size was removed from the description, leave the space
            // that separated the size from the rest of the description
            newDescription = task.description
                    + String.join(" ",  descriptionArgs);
        } else {
            newDescription = task.description
                    + String.join(" ",  descriptionArgs) + " ";
        }

        task.description = newDescription;
    }
}