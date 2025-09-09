package jay.ui;

import jay.command.Command;
import jay.exception.JayException;
import jay.parser.Parser;
import jay.storage.Storage;
import jay.tasklist.TaskList;
import jay.tasks.Task;

/**
 * Base Chatbot application
 */
public class Jay {


    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Constructs a new {@code Jay} application that stores data
     * in the default file path {@code data/tasks.txt}.
     */
    public Jay() {
        this("data/tasks.txt");
    }

    /**
     * Creates the {@code Jay} application.
     *
     * @param filePath The filepath to read the saved data from.
     */
    public Jay(String filePath) {
        assert filePath != null && !filePath.isBlank();
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (JayException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList();
        }
    }

    /**
     * Processes the user input and executes the corresponding command.
     *
     * @param input The full input string entered by the user
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.parseCommand(input);
            String argument = null;

            switch (command) {
            case BYE:
                return ui.showBye();

            case LIST:
                return ui.showTasks(tasks);

            case MARK:
                argument = Parser.parseArgument(input);
                int markedIndex = Parser.parseTaskNumber(tasks, argument);
                tasks.get(markedIndex).markAsDone();
                storage.save(tasks);
                return ui.showMarkedTask(tasks, markedIndex);

            case UNMARK:
                argument = Parser.parseArgument(input);
                int unmarkedIndex = Parser.parseTaskNumber(tasks, argument);
                tasks.get(unmarkedIndex).unmarkAsDone();
                storage.save(tasks);
                return ui.showUnmarkedTask(tasks, unmarkedIndex);

            case DELETE:
                argument = Parser.parseArgument(input);
                int delIndex = Parser.parseTaskNumber(tasks, argument);
                Task removedTask = tasks.remove(delIndex);
                storage.save(tasks);
                return ui.showRemovedTask(tasks, removedTask);

            case TODO:
                argument = Parser.parseArgument(input);
                tasks.add(Parser.parseTodo(argument));
                storage.save(tasks);
                return ui.showAddedTask(tasks);

            case DEADLINE:
                argument = Parser.parseArgument(input);
                tasks.add(Parser.parseDeadline(argument));
                storage.save(tasks);
                return ui.showAddedTask(tasks);

            case EVENT:
                argument = Parser.parseArgument(input);
                tasks.add(Parser.parseEvent(argument));
                storage.save(tasks);
                return ui.showAddedTask(tasks);

            case FIND:
                argument = Parser.parseArgument(input);
                TaskList matches = tasks.findByKeyword(argument);
                return ui.showFoundTasks(matches);

            default:
                throw new JayException("unknown command");
            }
        } catch (JayException e) {
            return ui.showError(e.getMessage());
        }
    }
}
