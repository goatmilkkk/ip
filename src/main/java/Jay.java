import java.util.Objects;
import java.util.Scanner;
import java.util.regex.*;
import java.util.ArrayList;

public class Jay {
    public static void main(String[] args) throws JayException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        Matcher m;

        System.out.println("\t____________________________________________________________");
        System.out.println("\tHello! I'm Jay");
        System.out.println("\tWhat can I do for you?");
        System.out.println("\t____________________________________________________________\n");

        while (true) {
            String input = scanner.nextLine();
            String[] words = input.split("\\s+", 2);
            String command = words[0];
            String argument = (words.length > 1) ? words[1] : "";

            try {
                switch (command) {
                    case "bye":
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Bye. Hope to see you again soon!");
                        System.out.println("\t____________________________________________________________\n");
                        return;

                    case "list":
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println("\t " + (i + 1) + ". " + tasks.get(i));
                        }
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case "mark":
                        if (!argument.matches("\\d+")) {
                            throw new JayParseArgumentException("Error: not a task number!");
                        }
                        int markIndex = Integer.parseInt(argument) - 1;
                        if (markIndex < 0 || markIndex >= tasks.size()) {
                            throw new JayParseArgumentException("Error: invalid task number!");
                        }
                        tasks.get(markIndex).markAsDone();
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Nice! I've marked this task as done:");
                        System.out.println("\t " + tasks.get(markIndex));
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case "unmark":
                        if (!argument.matches("\\d+")) {
                            throw new JayParseArgumentException("Error: not a task number!");
                        }
                        int unmarkIndex = Integer.parseInt(argument) - 1;
                        if (unmarkIndex < 0 || unmarkIndex >= tasks.size()) {
                            throw new JayParseArgumentException("Error: invalid task number!");
                        }
                        tasks.get(unmarkIndex).unmarkAsDone();
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t OK, I've marked this task as not done yet:");
                        System.out.println("\t " + tasks.get(unmarkIndex));
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case "delete":
                        if (!argument.matches("\\d+")) {
                            throw new JayParseArgumentException("Error: not a task number!");
                        }
                        int delIndex = Integer.parseInt(argument) - 1;
                        if (delIndex < 0 || delIndex >= tasks.size()) {
                            throw new JayParseArgumentException("Error: invalid task number!");
                        }
                        Task removed = tasks.remove(delIndex);

                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Noted. I've removed this task:");
                        System.out.println("\t   " + removed);
                        System.out.println("\t Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case "todo":
                        if (Objects.equals(argument, "")) {
                            throw new JayParseArgumentException("Error: empty description for Todo!");
                        }
                        tasks.add(new Todo(argument));
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Got it. I've added this task:");
                        System.out.println("\t   " + tasks.getLast());
                        System.out.println("\t Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case "deadline":
                        Pattern deadlinePattern = Pattern.compile("^(?<desc>.+?)\\s*/by\\s+(?<by>.+)$");
                        m = deadlinePattern.matcher(argument);
                        if (!m.matches()) {
                            throw new JayParseArgumentException("Error: invalid format for Deadline!");
                        }
                        tasks.add(new Deadline(
                                m.group("desc").trim(),
                                m.group("by").trim()
                        ));
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Got it. I've added this task:");
                        System.out.println("\t   " + tasks.getLast());
                        System.out.println("\t Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case "event":
                        Pattern eventPattern = Pattern.compile("^(?<desc>.+?)\\s*/from\\s+(?<from>.+?)\\s*/to\\s+(?<to>.+)$");
                        m = eventPattern.matcher(argument);
                        if (!m.matches()) {
                            throw new JayParseArgumentException("Error: invalid format for Event!");
                        }
                        tasks.add(new Event(
                                m.group("desc").trim(),
                                m.group("from").trim(),
                                m.group("to").trim()
                        ));
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Got it. I've added this task:");
                        System.out.println("\t   " + tasks.getLast());
                        System.out.println("\t Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    default:
                        throw new JayInvalidCommandException("Error: invalid command!");
                }
            } catch (JayException e) {
                System.out.println("\t____________________________________________________________");
                System.out.println("\t " + e.toString());
                System.out.println("\t____________________________________________________________\n");
            }
        }
    }
}
