package jay.tasklist;

import java.util.ArrayList;
import java.util.stream.Collectors;

import jay.tasks.Task;

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
        if (keyword == null || keyword.isBlank()) {
            return new TaskList();
        }
        String needle = keyword.toLowerCase();
        return this.stream()
                .filter(t -> t.getDescription().toLowerCase().contains(needle))
                .collect(Collectors.toCollection(TaskList::new));
    }
}
