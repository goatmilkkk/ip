package jay.tasks;

import java.time.LocalDateTime;

import jay.parser.Parser;

/**
 * Represents an event task with a start and end datetime.
 */
public class Event extends Task {
    protected final LocalDateTime from;
    protected final LocalDateTime to;

    /**
     * Creates a new event task.
     *
     * @param description The task description.
     * @param from        The start datetime.
     * @param to          The end datetime.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public LocalDateTime getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return "[E]"
                + super.toString()
                + " (from: "
                + Parser.formatDateTime(getFrom())
                + " to: "
                + Parser.formatDateTime(getTo())
                + ")";
    }
}
