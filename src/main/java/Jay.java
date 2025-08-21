import java.util.Scanner;

public class Jay {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int count = 0;

        System.out.println("Hello! I'm Jay");
        System.out.println("What can I do for you?\n");

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            } else if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else if (input.startsWith("mark")) {
                int taskNumber = Integer.parseInt(input.replace("mark", "").trim());
                System.out.println("Nice! I've marked this task as done:");
                tasks[taskNumber - 1].markAsDone();
                System.out.println(tasks[taskNumber - 1]);
            } else if (input.startsWith("unmark")) {
                int taskNumber = Integer.parseInt(input.replace("unmark", "").trim());
                System.out.println("OK, I've marked this task as not done yet:");
                tasks[taskNumber - 1].unmarkAsDone();
                System.out.println(tasks[taskNumber - 1]);
            } else {
                tasks[count++] = new Task(input);
                System.out.println("added: " + input);
            }
            System.out.println();
        }
    }
}
