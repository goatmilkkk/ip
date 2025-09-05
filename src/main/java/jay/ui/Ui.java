package jay.ui;

import jay.tasklist.TaskList;
import jay.tasks.Task;

/**
 * Handles user interaction by generating messages instead of printing.
 */
public class Ui {

    /**
     * Returns the welcome message.
     */
    public static String showWelcome() {
        return "Hello! I'm Jay\nWhat can I do for you?";
    }

    /**
     * Returns the goodbye message.
     */
    public String showBye() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Returns all tasks in the list as a formatted string.
     *
     * @param tasks The list of tasks.
     */
    public String showTasks(TaskList tasks) {
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Returns a message when a task is marked as done.
     */
    public String showMarkedTask(TaskList tasks, int markedIndex) {
        return "Nice! I've marked this task as done:\n  " + tasks.get(markedIndex);
    }

    /**
     * Returns a message when a task is marked as not done.
     */
    public String showUnmarkedTask(TaskList tasks, int unmarkedIndex) {
        return "OK, I've marked this task as not done yet:\n  " + tasks.get(unmarkedIndex);
    }

    /**
     * Returns a message when a task is removed.
     */
    public String showRemovedTask(TaskList tasks, Task removedTask) {
        return "Noted. I've removed this task:\n  " + removedTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Returns a message when a task is added.
     */
    public String showAddedTask(TaskList tasks) {
        return "Got it. I've added this task:\n  " + tasks.get(tasks.size() - 1)
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Returns an error message.
     */
    public String showError(String message) {
        return "Error: " + message;
    }

    /**
     * Returns the list of tasks matching a keyword.
     */
    public String showFoundTasks(TaskList matches) {
        if (matches.isEmpty()) {
            return "No matching tasks found.";
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append((i + 1)).append(". ").append(matches.get(i)).append("\n");
        }
        return sb.toString().trim();
    }
}
