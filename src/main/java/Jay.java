import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.*;
import java.util.ArrayList;

public class Jay {

    public enum Command {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT;

        static Command parse(String raw) throws JayException {
            try {
                return Command.valueOf(raw.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new JayException("Error: invalid command!");
            }
        }
    }

    public static void main(String[] args) throws JayException {
        Storage storage = new Storage();
        ArrayList<Task> tasks = storage.load();
        Scanner scanner = new Scanner(System.in);
        Matcher m;
        Ui ui = new Ui();

        ui.showWelcome();

        while (true) {
            String input = scanner.nextLine();
            String[] words = input.split("\\s+", 2);
            String rawCommand = words[0];
            String argument = (words.length > 1) ? words[1] : "";

            try {
                Command command = Command.parse(rawCommand);
                switch (command) {
                    case BYE:
                        ui.showBye();
                        return;

                    case LIST:
                        ui.showTasks(tasks);
                        break;

                    case MARK:
                        if (!argument.matches("\\d+")) {
                            throw new JayException("Error: not a task number!");
                        }
                        int markedIndex = Integer.parseInt(argument) - 1;
                        if (markedIndex < 0 || markedIndex >= tasks.size()) {
                            throw new JayException("Error: invalid task number!");
                        }
                        tasks.get(markedIndex).markAsDone();
                        storage.save(tasks);
                        ui.showMarkedTask(tasks, markedIndex);
                        break;

                    case UNMARK:
                        if (!argument.matches("\\d+")) {
                            throw new JayException("Error: not a task number!");
                        }
                        int unmarkedIndex = Integer.parseInt(argument) - 1;
                        if (unmarkedIndex < 0 || unmarkedIndex >= tasks.size()) {
                            throw new JayException("Error: invalid task number!");
                        }
                        tasks.get(unmarkedIndex).unmarkAsDone();
                        storage.save(tasks);
                        ui.showUnmarkedTask(tasks, unmarkedIndex);
                        break;

                    case DELETE:
                        if (!argument.matches("\\d+")) {
                            throw new JayException("Error: not a task number!");
                        }
                        int delIndex = Integer.parseInt(argument) - 1;
                        if (delIndex < 0 || delIndex >= tasks.size()) {
                            throw new JayException("Error: invalid task number!");
                        }
                        Task removedTask = tasks.remove(delIndex);
                        storage.save(tasks);
                        ui.showRemovedTask(tasks, removedTask);
                        break;

                    case TODO:
                        if (Objects.equals(argument, "")) {
                            throw new JayException("Error: empty description for Todo!");
                        }
                        tasks.add(new Todo(argument));
                        storage.save(tasks);
                        ui.showAddedTask(tasks);
                        break;

                    case DEADLINE:
                        Pattern deadlinePattern = Pattern.compile("^(?<desc>.+?)\\s*/by\\s+(?<by>.+)$");
                        m = deadlinePattern.matcher(argument);
                        if (!m.matches()) {
                            throw new JayException("Error: invalid format for Deadline!");
                        }
                        LocalDateTime by = LocalDateTime.parse(
                            m.group("by"),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")
                        );
                        tasks.add(new Deadline(
                            m.group("desc").trim(),
                            by
                        ));
                        storage.save(tasks);
                        ui.showAddedTask(tasks);
                        break;

                    case EVENT:
                        Pattern eventPattern = Pattern.compile("^(?<desc>.+?)\\s*/from\\s+(?<from>.+?)\\s*/to\\s+(?<to>.+)$");
                        m = eventPattern.matcher(argument);
                        if (!m.matches()) {
                            throw new JayException("Error: invalid format for Event!");
                        }

                        LocalDateTime from = LocalDateTime.parse(
                            m.group("from"),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")
                        );
                        LocalDateTime to = LocalDateTime.parse(
                            m.group("to"),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")
                        );
                        tasks.add(new Event(
                            m.group("desc").trim(),
                            from,
                            to
                        ));
                        storage.save(tasks);
                        ui.showAddedTask(tasks);
                        break;
                }
            } catch (JayException e) {
                ui.showError(e.toString());
            }
        }
    }
}
