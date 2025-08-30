package jay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    static Command parseCommand(String input) throws JayException {
        try {
            String[] words = input.split("\\s+", 2);
            return Command.valueOf(words[0].toUpperCase());
        } catch (Exception e) {
            throw new JayException("Error, invalid command!");
        }
    }

    public static String parseArgument(String input) throws JayException {
        try {
            String[] words = input.split("\\s+", 2);
            return words[1];
        } catch (Exception e) {
            throw new JayException("Error, invalid argument!");
        }

    }

    public static int parseTaskNumber(ArrayList<Task> tasks, String argument) throws JayException {
        if (!argument.matches("\\d+")) {
            throw new JayException("Error, not a task number!");
        }
        int taskNumber = Integer.parseInt(argument) - 1;
        if (taskNumber < 0 || taskNumber >= tasks.size()) {
            throw new JayException("Error, invalid task number!");
        }
        return taskNumber;
    }

    private static LocalDateTime parseDateTimeString(String raw) throws JayException {
        try {
            return LocalDateTime.parse(raw, DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new JayException("Error, invalid datetime");
        }
    }

    public static String formatDateTime(LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ofPattern("MMM dd yyyy h:mma"));
    }

    public static Todo parseTodo(String argument) throws JayException {
        if (Objects.equals(argument, "")) {
            throw new JayException("Error, empty description for Todo!");
        }
        return new Todo(argument);
    }

    public static Deadline parseDeadline(String argument) throws JayException {
        Pattern deadlinePattern = Pattern.compile("^(?<desc>.+?)\\s*/by\\s+(?<by>.+)$");
        Matcher m = deadlinePattern.matcher(argument);
        if (!m.matches()) {
            throw new JayException("Error, invalid format for Deadline!");
        }
        LocalDateTime by = parseDateTimeString(m.group("by"));
        return new Deadline(m.group("desc").trim(), by);
    }

    public static Event parseEvent(String argument) throws JayException {
        Pattern eventPattern = Pattern.compile("^(?<desc>.+?)\\s*/from\\s+(?<from>.+?)\\s*/to\\s+(?<to>.+)$");
        Matcher m = eventPattern.matcher(argument);
        if (!m.matches()) {
            throw new JayException("Error, invalid format for Event!");
        }
        LocalDateTime from = parseDateTimeString(m.group("from"));
        LocalDateTime to = parseDateTimeString(m.group("to"));
        return new Event(m.group("desc").trim(), from, to);
    }
}