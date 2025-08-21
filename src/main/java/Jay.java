import java.util.Scanner;

public class Jay {
    public static void main(String[] args) {
        System.out.println("Hello! I'm Jay");
        System.out.println("What can I do for you?");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            System.out.println(input);
        }
        System.out.println("Bye. Hope to see you again soon!");
    }
}
