package jay.tasks;

/**
 * Represents a simple Todo task.
 */
public class Todo extends Task {

    /**
     * Creates a new Todo task.
     *
     * @param description The task description.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
