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
     * @param input The full input string entered by the user.
     * @return The chatbot's response as a string.
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.parseCommand(input);

            switch (command) {
            case BYE:
                return ui.showBye();
            case LIST:
                return ui.showTasks(tasks);
            case MARK:
                return handleMark(input);
            case UNMARK:
                return handleUnmark(input);
            case DELETE:
                return handleDelete(input);
            case TODO:
                return handleAddTodo(input);
            case DEADLINE:
                return handleAddDeadline(input);
            case EVENT:
                return handleAddEvent(input);
            case FIND:
                return handleFind(input);
            default:
                throw new JayException("unknown command");
            }
        } catch (JayException e) {
            return ui.showError(e.getMessage());
        }
    }

    /**
     * Handles marking a task as done.
     *
     * @param input The full input string entered by the user.
     * @return Response message confirming the task has been marked.
     * @throws JayException If the task number is invalid or parsing fails.
     */
    private String handleMark(String input) throws JayException {
        String argument = Parser.parseArgument(input);
        int markedIndex = Parser.parseTaskNumber(tasks, argument);
        tasks.get(markedIndex).markAsDone();
        storage.save(tasks);
        return ui.showMarkedTask(tasks, markedIndex);
    }

    /**
     * Handles unmarking a task (marking it as not done).
     *
     * @param input The full input string entered by the user.
     * @return Response message confirming the task has been unmarked.
     * @throws JayException If the task number is invalid or parsing fails.
     */
    private String handleUnmark(String input) throws JayException {
        String argument = Parser.parseArgument(input);
        int unmarkedIndex = Parser.parseTaskNumber(tasks, argument);
        tasks.get(unmarkedIndex).unmarkAsDone();
        storage.save(tasks);
        return ui.showUnmarkedTask(tasks, unmarkedIndex);
    }

    /**
     * Handles deleting a task from the list.
     *
     * @param input The full input string entered by the user.
     * @return Response message confirming the task has been removed.
     * @throws JayException If the task number is invalid or parsing fails.
     */
    private String handleDelete(String input) throws JayException {
        String argument = Parser.parseArgument(input);
        int delIndex = Parser.parseTaskNumber(tasks, argument);
        Task removedTask = tasks.remove(delIndex);
        storage.save(tasks);
        return ui.showRemovedTask(tasks, removedTask);
    }

    /**
     * Handles adding a new {@code Todo} task.
     *
     * @param input The full input string entered by the user.
     * @return Response message confirming the task has been added.
     * @throws JayException If parsing fails or the argument is invalid.
     */
    private String handleAddTodo(String input) throws JayException {
        String argument = Parser.parseArgument(input);
        tasks.add(Parser.parseTodo(argument));
        storage.save(tasks);
        return ui.showAddedTask(tasks);
    }

    /**
     * Handles adding a new {@code Deadline} task.
     *
     * @param input The full input string entered by the user.
     * @return Response message confirming the task has been added.
     * @throws JayException If parsing fails or the argument is invalid.
     */
    private String handleAddDeadline(String input) throws JayException {
        String argument = Parser.parseArgument(input);
        tasks.add(Parser.parseDeadline(argument));
        storage.save(tasks);
        return ui.showAddedTask(tasks);
    }

    /**
     * Handles adding a new {@code Event} task.
     *
     * @param input The full input string entered by the user.
     * @return Response message confirming the task has been added.
     * @throws JayException If parsing fails or the argument is invalid.
     */
    private String handleAddEvent(String input) throws JayException {
        String argument = Parser.parseArgument(input);
        tasks.add(Parser.parseEvent(argument));
        storage.save(tasks);
        return ui.showAddedTask(tasks);
    }

    /**
     * Handles finding tasks that match a given keyword.
     *
     * @param input The full input string entered by the user.
     * @return Response message containing the list of matching tasks.
     * @throws JayException If parsing fails or the argument is invalid.
     */
    private String handleFind(String input) throws JayException {
        String argument = Parser.parseArgument(input);
        TaskList matches = tasks.findByKeyword(argument);
        return ui.showFoundTasks(matches);
    }
}
