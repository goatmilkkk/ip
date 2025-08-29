import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path file;

    public Storage() {
        this.file = Paths.get("data", "duke.txt");
    }

    /** Load tasks from disk. If file/folder missing, return empty list. */
    public ArrayList<Task> load() throws JayException {
        try {
            if (Files.notExists(file)) {
                // ensure parent dir exists for first run
                if (file.getParent() != null) {
                    Files.createDirectories(file.getParent());
                }
                return new ArrayList<>();
            }

            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            ArrayList<Task> tasks = new ArrayList<>();
            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty()) continue;

                // split by " | " but keep empty trailing parts if any
                String[] parts = line.split("\\s*\\|\\s*", -1);
                char kind = parts[0].trim().charAt(0);
                int done = Integer.parseInt(parts[1].trim());

                Task t;
                switch (kind) {
                    case 'T': {
                        String desc = parts[2];
                        t = new Todo(desc);
                        break;
                    }
                    case 'D': {
                        String desc = parts[2];
                        String by = parts[3];
                        t = new Deadline(desc, by);
                        break;
                    }
                    case 'E': {
                        String desc = parts[2];
                        String from = parts[3];
                        String to = parts[4];
                        t = new Event(desc, from, to);
                        break;
                    }
                    default:
                        throw new JayParseException("Error: unknown task type in storage: " + kind);
                }
                if (done == 1) {
                    t.markAsDone();
                }
                tasks.add(t);
            }
            return tasks;
        } catch (IOException e) {
            throw new JayInvalidCommandException("Error: cannot read save file: " + e.getMessage());
        } catch (RuntimeException e) {
            // covers bad integers / bad indexing from corrupt lines
            throw new JayParseException("Error: corrupted save file format.");
        }
    }

    /** Save all tasks to disk, creating ./data/ if missing. */
    public void save(List<Task> tasks) throws JayException {
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            try (BufferedWriter w = Files.newBufferedWriter(
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
            throw new JayInvalidCommandException("Error: cannot write save file: " + e.getMessage());
        }
    }

    private static String serialize(Task t) throws JayException {
        if (t instanceof Todo td) {
            return String.join(" | ",
                    "T",
                    td.isDone() ? "1" : "0",
                    td.getDescription());
        } else if (t instanceof Deadline d) {
            return String.join(" | ",
                    "D",
                    d.isDone() ? "1" : "0",
                    d.getDescription(),
                    d.getBy());
        } else if (t instanceof Event e) {
            return String.join(" | ",
                    "E",
                    e.isDone() ? "1" : "0",
                    e.getDescription(),
                    e.getFrom(),
                    e.getTo());
        } else {
            throw new JayParseException("Error: unrecognized task type in data file!");
        }
    }
}