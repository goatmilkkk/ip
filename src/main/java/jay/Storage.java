package jay;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading of tasks from a file.
 */
public class Storage {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final Path file;

    /**
     * Creates a storage that saves to the default file path {@code data/tasks.txt}.
     */
    public Storage() {
        this.file = Paths.get("data", "tasks.txt");
    }

    /**
     * Creates a storage with a custom file path.
     *
     * @param file The file path to use.
     */
    public Storage(String file) {
        this.file = Paths.get(file);
    }

    /**
     * Loads tasks from the save file. If the file does not exist,
     * a new empty list is returned.
     *
     * @return A list of tasks from the file.
     * @throws JayException If the file is corrupted or cannot be read.
     */
    public ArrayList<Task> load() throws JayException {
        try {
            if (Files.notExists(file)) {
                if (file.getParent() != null) {
                    Files.createDirectories(file.getParent());
                }
                return new ArrayList<>();
            }

            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            ArrayList<Task> tasks = new ArrayList<>();

            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // format: TYPE | done | desc | [dates...]
                String[] parts = line.split("\\s*\\|\\s*", -1);
                if (parts.length < 3) {
                    throw new JayException("Error, corrupted save file format.");
                }

                char kind = parts[0].trim().isEmpty() ? '?' : parts[0].trim().charAt(0);
                int done = Integer.parseInt(parts[1].trim());
                String desc = parts[2];

                Task t;
                switch (kind) {
                case 'T': {
                    t = new Todo(desc);
                    break;
                }
                case 'D': {
                    if (parts.length < 4) {
                        throw new JayException("Error, bad Deadline line.");
                    }
                    LocalDateTime by = LocalDateTime.parse(parts[3].trim(), ISO);
                    t = new Deadline(desc, by);
                    break;
                }
                case 'E': {
                    if (parts.length < 5) {
                        throw new JayException("Error, bad Event line.");
                    }
                    LocalDateTime from = LocalDateTime.parse(parts[3].trim(), ISO);
                    LocalDateTime to = LocalDateTime.parse(parts[4].trim(), ISO);
                    t = new Event(desc, from, to);
                    break;
                }

                default: {
                    throw new JayException("Error, unknown task type in storage: " + kind);
                }
                }

                if (done == 1) {
                    t.markAsDone();
                }
                tasks.add(t);
            }
            return tasks;

        } catch (IOException e) {
            throw new JayException("Error, cannot read save file: " + e.getMessage());
        } catch (RuntimeException e) {
            throw new JayException("Error, corrupted save file format.");
        }
    }

    /**
     * Saves the given list of tasks to the file.
     *
     * @param tasks The list of tasks to save.
     * @throws JayException If the file cannot be written to.
     */
    public void save(List<Task> tasks) throws JayException {
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }

            try (BufferedWriter w =
                         Files.newBufferedWriter(
                                 file,
                                 StandardCharsets.UTF_8,
                                 StandardOpenOption.CREATE,
                                 StandardOpenOption.TRUNCATE_EXISTING,
                                 StandardOpenOption.WRITE)) {

                for (Task t : tasks) {
                    w.write(serialize(t));
                    w.newLine();
                }
            }
        } catch (IOException e) {
            throw new JayException("Error, cannot write save file: " + e.getMessage());
        }
    }

    /**
     * Converts a task into a saveable string representation.
     *
     * @param t The task to serialize.
     * @return The serialized task string.
     * @throws JayException If the task type is not recognized.
     */
    private static String serialize(Task t) throws JayException {
        if (t instanceof Todo td) {
            return String.join(" | ", "T", td.isDone() ? "1" : "0", td.getDescription());

        } else if (t instanceof Deadline d) {
            LocalDateTime byDate = d.getBy();
            return String.join(
                    " | ", "D", d.isDone() ? "1" : "0", d.getDescription(), byDate.format(ISO));

        } else if (t instanceof Event e) {
            LocalDateTime fromDateTime = e.from;
            LocalDateTime toDateTime = e.to;

            return String.join(
                    " | ",
                    "E",
                    e.isDone() ? "1" : "0",
                    e.getDescription(),
                    fromDateTime.format(ISO),
                    toDateTime.format(ISO));
        } else {
            throw new JayException("Error, unrecognized task type in data file!");
        }
    }
}
