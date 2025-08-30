package jay;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void emptyConstructor_createsEmptyList() {
        TaskList tasks = new TaskList();
        assertTrue(tasks.isEmpty(), "New TaskList should be empty");
        assertEquals(0, tasks.size());
    }

    @Test
    public void addAndGetTask_worksCorrectly() {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("write tests");
        tasks.add(todo);

        assertEquals(1, tasks.size());
        assertSame(todo, tasks.get(0));
    }

    @Test
    public void removeTask_worksCorrectly() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("a"));
        tasks.add(new Todo("b"));

        Task removed = tasks.remove(0);

        assertEquals("[T][ ] a", removed.toString());
        assertEquals(1, tasks.size());
        assertEquals("[T][ ] b", tasks.get(0).toString());
    }

    @Test
    public void markAndUnmarkTask_persistsInList() {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("finish homework");
        tasks.add(todo);

        todo.markAsDone();
        assertTrue(tasks.get(0).isDone());

        todo.unmarkAsDone();
        assertFalse(tasks.get(0).isDone());
    }
}
