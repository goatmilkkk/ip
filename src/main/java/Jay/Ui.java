package Jay;

import java.util.ArrayList;

public class Ui {
    public void showWelcome() {
        System.out.println("\t____________________________________________________________");
        System.out.println("\tHello! I'm Jay");
        System.out.println("\tWhat can I do for you?");
        System.out.println("\t____________________________________________________________\n");
    }

    public void showBye() {
        System.out.println("\t____________________________________________________________");
        System.out.println("\t Bye. Hope to see you again soon!");
        System.out.println("\t____________________________________________________________\n");
    }

    public void showTasks(ArrayList<Task> tasks) {
        System.out.println("\t____________________________________________________________");
        System.out.println("\t Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("\t " + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println("\t____________________________________________________________\n");
    }

    public void showMarkedTask(ArrayList<Task> tasks, int markedIndex) {
        System.out.println("\t____________________________________________________________");
        System.out.println("\t Nice! I've marked this task as done:");
        System.out.println("\t " + tasks.get(markedIndex));
        System.out.println("\t____________________________________________________________\n");
    }

    public void showUnmarkedTask(ArrayList<Task> tasks, int unmarkedIndex) {
        System.out.println("\t____________________________________________________________");
        System.out.println("\t OK, I've marked this task as not done yet:");
        System.out.println("\t " + tasks.get(unmarkedIndex));
        System.out.println("\t____________________________________________________________\n");
    }

    public void showRemovedTask(ArrayList<Task> tasks, Task removedTask) {
        System.out.println("\t____________________________________________________________");
        System.out.println("\t Noted. I've removed this task:");
        System.out.println("\t   " + removedTask);
        System.out.println("\t Now you have " + tasks.size() + " tasks in the list.");
        System.out.println("\t____________________________________________________________\n");
    }

    public void showAddedTask(ArrayList<Task> tasks) {
        System.out.println("\t____________________________________________________________");
        System.out.println("\t Got it. I've added this task:");
        System.out.println("\t   " + tasks.get(tasks.size() - 1));
        System.out.println("\t Now you have " + tasks.size() + " tasks in the list.");
        System.out.println("\t____________________________________________________________\n");
    }

    public void showError(String message) {
        System.out.println("\t____________________________________________________________");
        System.out.println("\t " + message);
        System.out.println("\t____________________________________________________________\n");
    }
}
