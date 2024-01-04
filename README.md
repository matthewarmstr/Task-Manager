# Task Manager Application
A simple task tracker written in Java using Maven and IntelliJ.

## About
Users can organize their completed and ongoing tasks in a straightforward command line-driven program. Tasks can be started and stopped at any given time to keep track of how long they spend on each activity. Individual tasks can be assigned a user-defined name, description, and size (based on traditional T-shirt sizes, such as S for “small” and L for “large”). Any task can be given a longer description, renamed, or resized at any time. 
## Saving User Inputs
The task manager maintains a log in the form of a text file to save the exact time when the user interacts with the stored tasks, making the task manager keep track of changes to each task attribute across multiple execution runs. This also allows the task manager to display precise running times for tasks that were previously stopped or are still ongoing when the program is executed. The log file is updated every time the user inputs a non-summary action to either create a new task, update the characteristics of an existing task, or remove one of the stored tasks entirely. These commands include: 
~~~
start <task name>                                | "Start a task of a specific name"
stop <task name>                                 | "Stop a task of a specific name"
describe <task name> <description> [{S|M|L|XL}]  | "Add a description to a task (can be a new or existing task).
                                                    A size (S, M, L, or XL) can be optionally assigned to this task."
size <task name> {S|M|L|XL}                      | "Assign a size (S, M, L, or XL) to a specific task"
rename <old task name> <new task name>           | "Rename an existing task"
delete <task name>                               | "Delete an existing task"
~~~
*Note: for simplicity, task names cannot include spaces. Task descriptions can have spaces, and thus multiple words. When a size is included in the 'describe' command, it must be separated from the description by a space.*

## Summarizing Logged Tasks
A summary can easily be printed for all existing tasks, which shows the description, size, and running time for each named task. Once a summary command is received, the task tracker builds a collection of tasks according to the user’s syntactically correct commands in the log. A series of logic checks are completed when reading the log file to ensure that the task behavior is recorded accurately (ex: if the user starts one task and immediately starts another task, the task builder stops the first task before starting the second task; another case is that a task can be sized and/or described before being started). Instead of printing the summary of every task, the user also has the option of viewing the summary of tasks with a given size, or a task with a specific name. These three different summary commands include:
~~~
summary                             | "Summarize all tasks"
summary <task name>                 | "Summarize a specific task"
summary {S|M|L|XL}                  | "Summarize all tasks of a specific size"
~~~

When there is more than one task included in the summary, the user can choose to have these tasks displayed in either ascending or descending order by their name (alphabetically), size (S, M, etc.), or time elapsed. These commands include:
~~~
name up                             | "Sort by task name alphabetically in ascending order" (A --> Z, a --> z)
name down                           | "Sort by task name alphabetically in descending order" (z --> a, Z --> A)
size up                             | "Sort by task size in ascending order" (S --> XL)
size down                           | "Sort by task size in descending order" (XL --> S)
time up                             | "Sort by time elapsed on each task in ascending order" (lowest times printed first)
time down                           | "Sort by time elapsed on each task in descending order" (highest times printed first)
~~~

Finally, when the summary includes more than one task with the same size, the minimum, maximum, total, and average elapsed times of the summarized tasks within each size category are computed and displayed.
