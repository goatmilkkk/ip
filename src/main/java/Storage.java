import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final Path file;

    public Storage() {
        this.file = Paths.get("data", "duke.txt");
    }

    /** Load tasks from disk. If file/folder missing, return empty list. */
    public ArrayList<Task> load() throws JayException {
        try {
            if (Files.notExists(file)) {
                if (file.getParent() != null) Files.createDirectories(file.getParent());
                return new ArrayList<>();
            }

            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            ArrayList<Task> tasks = new ArrayList<>();

            for (String raw : lines) {
                String line = raw.trim();
                if (line.isEmpty()) continue;

                // format: TYPE | done | desc | [dates...]
                String[] parts = line.split("\\s*\\|\\s*", -1);
                if (parts.length < 3) throw new JayParseException("Error: corrupted save file format.");

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
                        if (parts.length < 4) throw new JayParseException("Error: bad Deadline line.");
                        LocalDateTime by = LocalDateTime.parse(parts[3].trim(), ISO);
                        t = new Deadline(desc, by);
                        break;
                    }
                    case 'E': {
                        if (parts.length < 5) throw new JayParseException("Error: bad Event line.");
                        LocalDateTime from = LocalDateTime.parse(parts[3].trim(), ISO);
                        LocalDateTime to   = LocalDateTime.parse(parts[4].trim(), ISO);
                        t = new Event(desc, from, to);
                        break;
                    }
                    default:
                        throw new JayParseException("Error: unknown task type in storage: " + kind);
                }

                if (done == 1) t.markAsDone();
                tasks.add(t);
            }
            return tasks;

        } catch (IOException e) {
            throw new JayInvalidCommandException("Error: cannot read save file: " + e.getMessage());
        } catch (RuntimeException e) {
            // covers bad integers / bad indexing / bad date parse from corrupt lines
            throw new JayParseException("Error: corrupted save file format.");
        }
    }

    /** Save all tasks to disk, creating ./data/ if missing. */
    public void save(List<Task> tasks) throws JayException {
        try {
            if (file.getParent() != null) Files.createDirectories(file.getParent());

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

    /** Always serialize dates as ISO yyyy-MM-dd (not pretty). */
    private static String serialize(Task t) throws JayException {
        if (t instanceof Todo td) {
            return String.join(" | ",
                    "T",
                    td.isDone() ? "1" : "0",
                    td.getDescription());

        } else if (t instanceof Deadline d) {
            LocalDateTime byDate = d.getBy();
            return String.join(" | ",
                    "D",
                    d.isDone() ? "1" : "0",
                    d.getDescription(),
                    byDate.format(ISO));

        } else if (t instanceof Event e) {
            LocalDateTime fromDateTime = e.from;
            LocalDateTime toDateTime   = e.to;

            return String.join(" | ",
                    "E",
                    e.isDone() ? "1" : "0",
                    e.getDescription(),
                    fromDateTime.format(ISO),
                    toDateTime.format(ISO));
        } else {
            throw new JayParseException("Error: unrecognized task type in data file!");
        }
    }
}