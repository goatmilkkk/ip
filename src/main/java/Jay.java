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
                throw new JayInvalidCommandException("Error: invalid command!");
            }
        }
    }

    public static void main(String[] args) throws JayException {
        Storage storage = new Storage();
        ArrayList<Task> tasks = storage.load();
        Scanner scanner = new Scanner(System.in);
        Matcher m;

        System.out.println("\t____________________________________________________________");
        System.out.println("\tHello! I'm Jay");
        System.out.println("\tWhat can I do for you?");
        System.out.println("\t____________________________________________________________\n");

        while (true) {
            String input = scanner.nextLine();
            String[] words = input.split("\\s+", 2);
            String rawCommand = words[0];
            String argument = (words.length > 1) ? words[1] : "";

            try {
                Command command = Command.parse(rawCommand);
                switch (command) {
                    case BYE:
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Bye. Hope to see you again soon!");
                        System.out.println("\t____________________________________________________________\n");
                        return;

                    case LIST:
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println("\t " + (i + 1) + ". " + tasks.get(i));
                        }
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case MARK:
                        if (!argument.matches("\\d+")) {
                            throw new JayParseException("Error: not a task number!");
                        }
                        int markIndex = Integer.parseInt(argument) - 1;
                        if (markIndex < 0 || markIndex >= tasks.size()) {
                            throw new JayParseException("Error: invalid task number!");
                        }
                        tasks.get(markIndex).markAsDone();
                        storage.save(tasks);
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Nice! I've marked this task as done:");
                        System.out.println("\t " + tasks.get(markIndex));
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case UNMARK:
                        if (!argument.matches("\\d+")) {
                            throw new JayParseException("Error: not a task number!");
                        }
                        int unmarkIndex = Integer.parseInt(argument) - 1;
                        if (unmarkIndex < 0 || unmarkIndex >= tasks.size()) {
                            throw new JayParseException("Error: invalid task number!");
                        }
                        tasks.get(unmarkIndex).unmarkAsDone();
                        storage.save(tasks);
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t OK, I've marked this task as not done yet:");
                        System.out.println("\t " + tasks.get(unmarkIndex));
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case DELETE:
                        if (!argument.matches("\\d+")) {
                            throw new JayParseException("Error: not a task number!");
                        }
                        int delIndex = Integer.parseInt(argument) - 1;
                        if (delIndex < 0 || delIndex >= tasks.size()) {
                            throw new JayParseException("Error: invalid task number!");
                        }
                        Task removed = tasks.remove(delIndex);
                        storage.save(tasks);
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Noted. I've removed this task:");
                        System.out.println("\t   " + removed);
                        System.out.println("\t Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case TODO:
                        if (Objects.equals(argument, "")) {
                            throw new JayParseException("Error: empty description for Todo!");
                        }
                        tasks.add(new Todo(argument));
                        storage.save(tasks);
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Got it. I've added this task:");
                        System.out.println("\t   " + tasks.getLast());
                        System.out.println("\t Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case DEADLINE:
                        Pattern deadlinePattern = Pattern.compile("^(?<desc>.+?)\\s*/by\\s+(?<by>.+)$");
                        m = deadlinePattern.matcher(argument);
                        if (!m.matches()) {
                            throw new JayParseException("Error: invalid format for Deadline!");
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
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Got it. I've added this task:");
                        System.out.println("\t   " + tasks.getLast());
                        System.out.println("\t Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case EVENT:
                        Pattern eventPattern = Pattern.compile("^(?<desc>.+?)\\s*/from\\s+(?<from>.+?)\\s*/to\\s+(?<to>.+)$");
                        m = eventPattern.matcher(argument);
                        if (!m.matches()) {
                            throw new JayParseException("Error: invalid format for Event!");
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
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Got it. I've added this task:");
                        System.out.println("\t   " + tasks.getLast());
                        System.out.println("\t Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("\t____________________________________________________________\n");
                        break;
                }
            } catch (JayException e) {
                System.out.println("\t____________________________________________________________");
                System.out.println("\t " + e.toString());
                System.out.println("\t____________________________________________________________\n");
            }
        }
    }
}
