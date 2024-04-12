package numbers;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        PropertyChecker checker = new PropertyChecker();

        System.out.println(checker.happy(4));

        Scanner scanner = new Scanner(System.in);
        UserInterface ui = new UserInterface(scanner);
        System.out.println("Hello");

        ui.start();

    }

}
