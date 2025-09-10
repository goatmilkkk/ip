package jay.command;

import java.util.HashMap;
import java.util.Map;

import jay.exception.JayException;

/**
 * Represents the set of valid commands accepted by the Chatbot.
 */
public enum Command {
    BYE("bye", "b"),
    LIST("list", "ls", "l", "li"),
    MARK("mark", "m"),
    UNMARK("unmark", "um"),
    DELETE("delete", "del", "d"),
    TODO("todo", "t"),
    DEADLINE("deadline", "dl"),
    EVENT("event", "e"),
    FIND("find", "f");

    private static final Map<String, Command> lookup = new HashMap<>();
    private final String[] aliases;

    static {
        for (Command cmd : Command.values()) {
            for (String alias : cmd.aliases) {
                lookup.put(alias.toUpperCase(), cmd);
            }
        }
    }

    Command(String... aliases) {
        this.aliases = aliases;
    }

    /**
     * Resolve a string into a Command.
     *
     * @param word The input string (command keyword or alias).
     * @return The corresponding Command.
     * @throws JayException if no matching command is found.
     */
    public static Command fromString(String word) throws JayException {
        Command cmd = lookup.get(word.toUpperCase());
        if (cmd == null) {
            throw new JayException("invalid command!");
        }
        return cmd;
    }
}
