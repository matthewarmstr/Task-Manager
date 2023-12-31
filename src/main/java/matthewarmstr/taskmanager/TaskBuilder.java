package matthewarmstr.taskmanager;

import java.io.IOException;
import java.util.ArrayList;

public class TaskBuilder {
    LogReader logReader = LogReader.getInstance();

    private static final String LOG_FILE = "./logs/TM.log";

    private static TaskBuilder instance;

    private ArrayList<Task> taskList;
    private String runningTask = null;

    public static TaskBuilder getInstance() {
        if (instance == null) {
            instance = new TaskBuilder();
        }
        return instance;
    }

    public ArrayList<Task> buildTasks() {
        taskList = new ArrayList<Task>();
        ArrayList<String[]> listOfLogLines = new ArrayList<String[]>();

        // Read lines from log file
        try {
            listOfLogLines = logReader.readLog();
        } catch (IOException e) { }

        // Convert user's input (log lines) into tasks
        processAllLogLines(listOfLogLines);

        // If task is still running after all of log has been read, forcefully stop it
        // to allow start time to be compared with current time during execution
        if (runningTask != null) {
            stopTask(runningTask, java.time.LocalDateTime.now() + "");
        }

        return taskList;
    }

    private Task getTask(String taskName) {
        for (Task task : taskList) {
            if (task.name.equals(taskName)) {
                return task;
            }
        }
        return null;
    }

    private void createTask(String taskName) {
        for (Task task : taskList) {
            if(task.name.equals(taskName))  { return; }
        }
        Task newTask = new Task(taskName);
        taskList.add(newTask);
    }

    private void deleteTask(String taskName) {
        for (int index = 0; index < taskList.size(); index++) {
            String nameAtIndex = taskList.get(index).name;
            if(nameAtIndex.equals(taskName)) {
                taskList.remove(index);
            }
        }
    }

    private void startTask(String taskName, String timestamp) {
        if (runningTask != null) {
            getTask(runningTask).stop(timestamp);
        }
        getTask(taskName).start(timestamp);
        runningTask = taskName;
    }

    private void stopTask(String taskName, String timestamp) {
        if (taskName.equals(runningTask)) {
            getTask(taskName).stop(timestamp);
            runningTask = null;
        }
    }

    private void processLogLine(String[] lineArgs, int lineNum) {
        String timestamp = lineArgs[0];
        String taskName = lineArgs[1];
        String[] actionArgs = lineArgs[2].split(" ");
        String userAction = actionArgs[0];

        // Parse line from log file and update tasks accordingly
        if (userAction.equals("start")) {
            createTask(taskName);
            startTask(taskName, timestamp);
        } else if (userAction.equals("stop")) {
            stopTask(taskName, timestamp);
        } else if (userAction.equals("describe")) {
            createTask(taskName);
            updateDescription(getTask(taskName), actionArgs);
        } else if (userAction.equals("size")) {
            createTask(taskName);
            getTask(taskName).size = actionArgs[1];
        } else if (userAction.equals("rename")) {
            if (taskName.equals(runningTask)) {
                runningTask = actionArgs[1];
            }
            if (getTask(taskName) != null) {
                getTask(taskName).name = actionArgs[1];
            }
        } else if (userAction.equals("delete")) {
            if (taskName.equals(runningTask)) { runningTask = null; }
            deleteTask(taskName);
        } else {
            System.out.println("Line " + Integer.toString(lineNum) + " of " + LOG_FILE + " is malformed");
        }
    }

    private void processAllLogLines(ArrayList<String[]> lines) {
        int lineNum = 1;
        for (String[] line : lines) {
            processLogLine(line, lineNum);
            lineNum++;
        }
    }

    private void updateDescription(Task task, String[] actionArgs) {
        int argslength = actionArgs.length;

        // Handle the case where the last token is a valid size
        if (UsageChecker.isValidSize(actionArgs[argslength - 1])) {
            task.size = actionArgs[argslength - 1];
            actionArgs[argslength - 1] = "";
        }

        // Extract the action
        String[] descriptionArgs = new String[actionArgs.length - 1];
        for (int i = 1; i < actionArgs.length; i++) {
            descriptionArgs[i-1] = actionArgs[i];
        }

        // Store updated task description
        String newDescription;
        if(actionArgs[argslength - 1].equals("")) {
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