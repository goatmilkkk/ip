package jay;

/**
 * Base Chatbot application
 */
public class Jay {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

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
     * The main menu for the {@code Yorm} application.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            try {
                String input = ui.scanLine();
                Command command = Parser.parseCommand(input);
                String argument = null;

                switch (command) {
                case BYE:
                    ui.showBye();
                    return;

                case LIST:
                    ui.showTasks(tasks);
                    break;

                case MARK:
                    argument = Parser.parseArgument(input);
                    int markedIndex = Parser.parseTaskNumber(tasks, argument);
                    tasks.get(markedIndex).markAsDone();
                    storage.save(tasks);
                    ui.showMarkedTask(tasks, markedIndex);
                    break;

                case UNMARK:
                    argument = Parser.parseArgument(input);
                    int unmarkedIndex = Parser.parseTaskNumber(tasks, argument);
                    tasks.get(unmarkedIndex).unmarkAsDone();
                    storage.save(tasks);
                    ui.showUnmarkedTask(tasks, unmarkedIndex);
                    break;

                case DELETE:
                    argument = Parser.parseArgument(input);
                    int delIndex = Parser.parseTaskNumber(tasks, argument);
                    Task removedTask = tasks.remove(delIndex);
                    storage.save(tasks);
                    ui.showRemovedTask(tasks, removedTask);
                    break;

                case TODO:
                    argument = Parser.parseArgument(input);
                    tasks.add(Parser.parseTodo(argument));
                    storage.save(tasks);
                    ui.showAddedTask(tasks);
                    break;

                case DEADLINE:
                    argument = Parser.parseArgument(input);
                    tasks.add(Parser.parseDeadline(argument));
                    storage.save(tasks);
                    ui.showAddedTask(tasks);
                    break;

                case EVENT:
                    argument = Parser.parseArgument(input);
                    tasks.add(Parser.parseEvent(argument));
                    storage.save(tasks);
                    ui.showAddedTask(tasks);
                    break;

                case FIND:
                    argument = Parser.parseArgument(input);
                    TaskList matches = tasks.findByKeyword(argument);
                    ui.showFoundTasks(matches);
                    break;

                default:
                    throw new JayException("Error, unknown command");
                }
            } catch (JayException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Jay("data/tasks.txt").run();
    }
}
