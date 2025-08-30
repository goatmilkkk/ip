package jay;

import java.time.LocalDateTime;

/**
 * Represents a task with a description and a deadline.
 */
public class Deadline extends Task {
    protected final LocalDateTime by;

    /**
     * Creates a new deadline task.
     *
     * @param description The task description.
     * @param by The deadline datetime.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public LocalDateTime getBy() {
        return this.by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + Parser.formatDateTime(getBy()) + ")";
    }
}
