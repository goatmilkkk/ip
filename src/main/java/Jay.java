import java.util.Scanner;

public class Jay {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
        int count = 0;

        System.out.println("Hello! I'm Jay");
        System.out.println("What can I do for you?\n");

        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            } else if (input.equals("list")) {
                for (int i = 0; i < count; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                System.out.println("");
            } else {
                tasks[count++] = input;
                System.out.println("added: " + input + "\n");
            }
        }
    }
}
