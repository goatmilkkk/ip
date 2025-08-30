package jay;

import java.util.ArrayList;
import java.util.Objects;

public class TaskList extends ArrayList<Task> {
    public TaskList() {
        super();
    }

    public TaskList(ArrayList<Task> tasks) {
        super(tasks);
    }

    public TaskList findByKeyword(String keyword) {
        TaskList matches = new TaskList();
        if (Objects.equals(keyword, "")) {
            return matches;
        }
        String needle = keyword.toLowerCase();
        for (Task t : this) {
            if (t.getDescription().toLowerCase().contains(needle)) {
                matches.add(t);
            }
        }
        return matches;
    }
}