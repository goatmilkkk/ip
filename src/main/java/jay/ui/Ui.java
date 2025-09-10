package jay.ui;

import jay.tasklist.TaskList;
import jay.tasks.Task;

/**
 * Handles user interaction by generating messages instead of printing.
 */
public class Ui {

    /**
     * Returns the welcome message.
     *
     * @return The welcome message string.
     */
    public static String showWelcome() {
        return "Hello! I'm Jay\nWhat can I do for you?";
    }

    /**
     * Returns the goodbye message.
     *
     * @return The goodbye message string.
     */
    public static String showBye() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Returns all tasks in the list as a formatted string.
     *
     * @param tasks The list of tasks.
     * @return A formatted string of all tasks.
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
     *
     * @param tasks       The list of tasks.
     * @param markedIndex The index of the task marked as done.
     * @return The confirmation message string.
     */
    public String showMarkedTask(TaskList tasks, int markedIndex) {
        return "Nice! I've marked this task as done:\n  " + tasks.get(markedIndex);
    }

    /**
     * Returns a message when a task is marked as not done.
     *
     * @param tasks         The list of tasks.
     * @param unmarkedIndex The index of the task marked as not done.
     * @return The confirmation message string.
     */
    public String showUnmarkedTask(TaskList tasks, int unmarkedIndex) {
        return "OK, I've marked this task as not done yet:\n  " + tasks.get(unmarkedIndex);
    }

    /**
     * Returns a message when a task is removed.
     *
     * @param tasks       The list of tasks after removal.
     * @param removedTask The task that was removed.
     * @return The confirmation message string.
     */
    public String showRemovedTask(TaskList tasks, Task removedTask) {
        return "Noted. I've removed this task:\n  " + removedTask
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Returns a message when a task is added.
     *
     * @param tasks The list of tasks after addition.
     * @return The confirmation message string.
     */
    public String showAddedTask(TaskList tasks) {
        return "Got it. I've added this task:\n  " + tasks.get(tasks.size() - 1)
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Returns an error message.
     *
     * @param message The error message text.
     * @return The formatted error message string.
     */
    public String showError(String message) {
        return "Error: " + message;
    }

    /**
     * Returns the list of tasks matching a keyword.
     *
     * @param matches The list of matching tasks.
     * @return A formatted string of matching tasks, or a message if none are found.
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
