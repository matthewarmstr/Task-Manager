package matthewarmstr.taskmanager;

public class MalformedInputException extends Exception {
    String message;

    public MalformedInputException(String thrownMessage) {
        message = thrownMessage + """
            Commands:
              start <task name>                                 | "Start a task of a specific name"
              stop <task name>                                  | "Stop a task of a specific name"
              describe <task name> <description> [{S|M|L|XL}]   | "Add a description to a task (can be a new, start or stopped task). A size (S, M, L, or XL) can also be optionally assigned to this task."
              summary                                           | "Summarize all tasks"
              summary [<task name> | {S|M|L|XL}]                | "Summarize a specific task, or summarize tasks of a specific size"
              size <task name> {S|M|L|XL}                       | "Assign a size (S, M, L, or XL) to a specific task"
              rename <old task name> <new task name>            | "Rename an existing task"
              delete <task name>                                | "Delete an existing task";
              exit                                              | "Exit task manager\"""";
    }
}
