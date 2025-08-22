import java.util.Objects;
import java.util.Scanner;
import java.util.regex.*;

public class Jay {
    public static void main(String[] args) throws JayException {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int count = 0;
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
                        for (int i = 0; i < count; i++) {
                            System.out.println("\t " + (i + 1) + ". " + tasks[i]);
                        }
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case "mark":
                        int markIndex = Integer.parseInt(argument) - 1;
                        tasks[markIndex].markAsDone();
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Nice! I've marked this task as done:");
                        System.out.println("\t " + tasks[markIndex]);
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case "unmark":
                        int unmarkIndex = Integer.parseInt(argument) - 1;
                        tasks[unmarkIndex].unmarkAsDone();
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t OK, I've marked this task as not done yet:");
                        System.out.println("\t " + tasks[unmarkIndex]);
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case "todo":
                        if (Objects.equals(argument, "")) {
                            throw new JayParseArgumentException("Error: empty description for Todo!");
                        }
                        tasks[count++] = new Todo(argument);
                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Got it. I've added this task:");
                        System.out.println("\t   " + tasks[count - 1]);
                        System.out.println("\t Now you have " + count + " tasks in the list.");
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case "deadline":
                        Pattern deadlinePattern = Pattern.compile("^(?<desc>.+?)\\s*/by\\s+(?<by>.+)$");
                        m = deadlinePattern.matcher(argument);
                        if (!m.matches()) {
                            throw new JayParseArgumentException("Error: invalid format for Deadline!");
                        }

                        tasks[count++] = new Deadline(
                                m.group("desc").trim(),
                                m.group("by").trim()
                        );

                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Got it. I've added this task:");
                        System.out.println("\t   " + tasks[count - 1]);
                        System.out.println("\t Now you have " + count + " tasks in the list.");
                        System.out.println("\t____________________________________________________________\n");
                        break;

                    case "event":
                        Pattern eventPattern = Pattern.compile("^(?<desc>.+?)\\s*/from\\s+(?<from>.+?)\\s*/to\\s+(?<to>.+)$");
                        m = eventPattern.matcher(argument);
                        if (!m.matches()) {
                            throw new JayParseArgumentException("Error: invalid format for Event!");
                        }

                        tasks[count++] = new Event(
                                m.group("desc").trim(),
                                m.group("from").trim(),
                                m.group("to").trim()
                        );

                        System.out.println("\t____________________________________________________________");
                        System.out.println("\t Got it. I've added this task:");
                        System.out.println("\t   " + tasks[count - 1]);
                        System.out.println("\t Now you have " + count + " tasks in the list.");
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