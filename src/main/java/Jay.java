import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.*;
import java.util.ArrayList;

public class Jay {

    public static void main(String[] args) throws JayException {
        Storage storage = new Storage();
        ArrayList<Task> tasks = storage.load();
        Scanner scanner = new Scanner(System.in);
        Ui ui = new Ui();

        ui.showWelcome();
        while (true) {
            String input = scanner.nextLine();
            String[] parts = Parser.parseInput(input);
            String rawCommand = parts[0];
            String argument = parts[1];

            try {
                Parser.Command command = Parser.Command.parse(rawCommand);
                switch (command) {
                    case BYE:
                        ui.showBye();
                        return;

                    case LIST:
                        ui.showTasks(tasks);
                        break;

                    case MARK:
                        int markedIndex = Parser.parseTaskNumber(tasks, argument);
                        tasks.get(markedIndex).markAsDone();
                        storage.save(tasks);
                        ui.showMarkedTask(tasks, markedIndex);
                        break;

                    case UNMARK:
                        int unmarkedIndex = Parser.parseTaskNumber(tasks, argument);
                        tasks.get(unmarkedIndex).unmarkAsDone();
                        storage.save(tasks);
                        ui.showUnmarkedTask(tasks, unmarkedIndex);
                        break;

                    case DELETE:
                        int delIndex = Parser.parseTaskNumber(tasks, argument);
                        Task removedTask = tasks.remove(delIndex);
                        storage.save(tasks);
                        ui.showRemovedTask(tasks, removedTask);
                        break;

                    case TODO:
                        tasks.add(Parser.parseTodo(argument));
                        storage.save(tasks);
                        ui.showAddedTask(tasks);
                        break;

                    case DEADLINE:
                        tasks.add(Parser.parseDeadline(argument));
                        storage.save(tasks);
                        ui.showAddedTask(tasks);
                        break;

                    case EVENT:
                        tasks.add(Parser.parseEvent(argument));
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
