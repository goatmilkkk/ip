package jay;

/**
 * Represents a simple todo task.
 */
public class Todo extends Task {
    protected String by; // not used, but kept for compatibility

    /**
     * Creates a new todo task.
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
