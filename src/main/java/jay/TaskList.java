package jay;

import java.util.ArrayList;

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
}
