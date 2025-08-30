package jay;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles parsing of user commands and task arguments.
 */
public class Parser {
    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Parses the command word from user input.
     *
     * @param input The full user input string.
     * @return The parsed {@code Command}.
     * @throws JayException If the command is invalid.
     */
    static Command parseCommand(String input) throws JayException {
        try {
            String[] words = input.split("\\s+", 2);
            return Command.valueOf(words[0].toUpperCase());
        } catch (Exception e) {
            throw new JayException("Error, invalid command!");
        }
    }

    /**
     * Extracts the argument part of the user input.
     *
     * @param input The full user input string.
     * @return The argument string.
     * @throws JayException If no argument is found.
     */
    public static String parseArgument(String input) throws JayException {
        try {
            String[] words = input.split("\\s+", 2);
            return words[1];
        } catch (Exception e) {
            throw new JayException("Error, invalid argument!");
        }
    }

    /**
     * Parses and validates a task number from input.
     *
     * @param tasks    The current list of tasks.
     * @param argument The raw argument string.
     * @return The zero-based task index.
     * @throws JayException If the number is invalid or out of range.
     */
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

    /**
     * Parses a datetime string into a {@code LocalDateTime}.
     *
     * @param raw The raw datetime string.
     * @return The parsed {@code LocalDateTime}.
     * @throws JayException If the format is invalid.
     */
    private static LocalDateTime parseDateTimeString(String raw) throws JayException {
        try {
            return LocalDateTime.parse(raw, DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new JayException("Error, invalid datetime");
        }
    }

    /**
     * Formats a {@code LocalDateTime} for display.
     *
     * @param dt The datetime to format.
     * @return The formatted string.
     */
    public static String formatDateTime(LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ofPattern("MMM dd yyyy h:mma"));
    }

    /**
     * Parses a Todo task from the given argument.
     *
     * @param argument The task description.
     * @return The {@code Todo} task.
     * @throws JayException If the description is empty.
     */
    public static Todo parseTodo(String argument) throws JayException {
        if (Objects.equals(argument, "")) {
            throw new JayException("Error, empty description for Todo!");
        }
        return new Todo(argument);
    }

    /**
     * Parses a Deadline task from the given argument.
     *
     * @param argument The raw input string.
     * @return The {@code Deadline} task.
     * @throws JayException If the format is invalid.
     */
    public static Deadline parseDeadline(String argument) throws JayException {
        Pattern deadlinePattern = Pattern.compile("^(?<desc>.+?)\\s*/by\\s+(?<by>.+)$");
        Matcher m = deadlinePattern.matcher(argument);
        if (!m.matches()) {
            throw new JayException("Error, invalid format for Deadline!");
        }
        LocalDateTime by = parseDateTimeString(m.group("by"));
        return new Deadline(m.group("desc").trim(), by);
    }

    /**
     * Parses an Event task from the given argument.
     *
     * @param argument The raw input string.
     * @return The {@code Event} task.
     * @throws JayException If the format is invalid.
     */
    public static Event parseEvent(String argument) throws JayException {
        Pattern eventPattern =
                Pattern.compile("^(?<desc>.+?)\\s*/from\\s+(?<from>.+?)\\s*/to\\s+(?<to>.+)$");
        Matcher m = eventPattern.matcher(argument);
        if (!m.matches()) {
            throw new JayException("Error, invalid format for Event!");
        }
        LocalDateTime from = parseDateTimeString(m.group("from"));
        LocalDateTime to = parseDateTimeString(m.group("to"));
        return new Event(m.group("desc").trim(), from, to);
    }
}
