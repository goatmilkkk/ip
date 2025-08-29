import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    protected final LocalDateTime from;
    protected final LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to   = to;
    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public LocalDateTime getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + getFrom().format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"))
                + " to: " + getTo().format(DateTimeFormatter.ofPattern("MMM dd yyyy h:mma")) + ")";
    }
}