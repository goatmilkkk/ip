package jay;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a list of tasks.
 */
public class TaskList extends ArrayList<Task> {
    /**
     * Creates an empty task list.
     */
    public TaskList() {
        super();
    }

    /**
     * Creates a task list initialized with the given tasks.
     *
     * @param tasks The list of tasks to initialize with.
     */
    public TaskList(ArrayList<Task> tasks) {
        super(tasks);
    }

    /**
     * Finds all tasks in the list whose description contains the given keyword.
     * The search is case-insensitive. If the keyword is an empty string,
     * an empty task list will be returned.
     *
     * @param keyword The keyword to search for in each task's description.
     * @return A new {@code TaskList} containing the tasks that matched.
     */
    public TaskList findByKeyword(String keyword) {
        TaskList matches = new TaskList();
        if (Objects.equals(keyword, "")) {
            return matches;
        }
        String needle = keyword.toLowerCase();
        for (Task t : this) {
            if (t.getDescription().toLowerCase().contains(needle)) {
                matches.add(t);
            }
        }
        return matches;
    }
}
