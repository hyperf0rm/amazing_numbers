package numbers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;
    private PropertyChecker checker;

    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
        this.checker = new PropertyChecker();
    }

    public void start() {

        System.out.println("Welcome to Amazing Numbers!\n");

        printInstructions();

        processRequest(scanner);

        System.out.println("Goodbye!");

    }

    public void processRequest(Scanner scanner) {

        while (true) {
            System.out.print("Enter a request: > ");
            String input = scanner.nextLine().trim();

            if (input.equals("")) {
                printInstructions();
            }

            if (input.equals("0")) {
                break;
            }

            String[] parts = input.split(" ");

            if (parts.length == 1) {
                oneNaturalNumber(parts);
            } else if (parts.length == 2) {
                twoNaturalNumbers(parts);
            } else if (parts.length == 3) {
                twoNaturalNumbersSingleProperty(parts);
            } else {
                twoNaturalNumbersMultipleProperties(parts);
            }
        }
    }

    private void oneNaturalNumber(String[] input) {
        try {
            long number = Long.parseLong(input[0]);
            if (number < 0) {
                System.out.println("\nThe first parameter should be a natural number or zero.\n");
            } else {
                printPropertiesTable(number);
            }
        } catch (NumberFormatException e) {
            System.out.println("\nThe first parameter should be a natural number or zero.\n");
        }
    }

    private void twoNaturalNumbers(String[] input) {
        try {
            long number = Long.parseLong(input[0]);
            long consecutive = Long.parseLong(input[1]);
            if (areNumbersNatural(number, consecutive)) {
                printMultipleNumbers(number, consecutive);
            }
        } catch (NumberFormatException e) {
            System.out.println("\nThe second parameter should be a natural number.\n");
        }
    }

    private void printMultipleNumbers(long number, long c) {

        System.out.println();

        for (long i = number; i < number + c; i++) {
            propertiesLine(i);
        }

        System.out.println();
    }

    private void twoNaturalNumbersSingleProperty(String[] input) {
        StringBuilder sb = new StringBuilder();

        try {
            long number = Long.parseLong(input[0]);
            long consecutive = Long.parseLong(input[1]);
            sb.append(input[2].toUpperCase());
            if (areNumbersNatural(number, consecutive)) {
                Property property = Property.valueOf(sb.toString());
                printMultipleNumbersBySingleProperty(number, consecutive, property);
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                System.out.println("\nFirst and second parameters should be natural numbers.\n");
            }
            if (e instanceof IllegalArgumentException) {
                System.out.println("The property [" + sb + "] is wrong.\n" +
                        "Available properties: " + Arrays.toString(Property.values()) + "\n");
            }
        }
    }

    private void printMultipleNumbersBySingleProperty(long number, long n, Property property) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String methodName = property.getValue();
        Method method = checker.getClass().getMethod(methodName, long.class);

        System.out.println();

        for (long i = number; n != 0 ; i++) {

            boolean filtered = (Boolean) method.invoke(checker, i);

            if (filtered) {
                propertiesLine(i);
                n--;
            }
        }
        System.out.println();
    }

    private void twoNaturalNumbersMultipleProperties(String[] input) {

        List<StringBuilder> names = userPropertiesList(input).get(0);
        List<StringBuilder> excluded = userPropertiesList(input).get(1);

        for (StringBuilder excl : excluded) {
            excl = excl.deleteCharAt(0);
        }

        if (!propertiesExist(names)) {
            return;
        }

        List<Property> props = propertiesFromInput(names);

        if (!checkMutuallyExclusiveProperties(props)) {
            return;
        }

        try {

            long number = Long.parseLong(input[0]);
            long consecutive = Long.parseLong(input[1]);
            if (areNumbersNatural(number, consecutive)) {
                complexFiltering(number, consecutive, props);
            }
        } catch (NumberFormatException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            System.out.println("\nThe second parameter should be a natural number.\n");
        }
    }

    private List<List<StringBuilder>> userPropertiesList(String[] input) {

        List<StringBuilder> userProps = new ArrayList<>();
        List<StringBuilder> excludeProps = new ArrayList<>();

        for (int i = 2; i < input.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(input[i].toUpperCase());
            if (sb.toString().startsWith("-")) {
                excludeProps.add(sb);
                continue;
            }
            userProps.add(sb);
        }

        List<List<StringBuilder>> props = new ArrayList<List<StringBuilder>>(2);
        props.add(userProps);
        props.add(excludeProps);
        return props;
    }

    private boolean propertiesExist(List<StringBuilder> userProps) {
        List<StringBuilder> wrongProps = new ArrayList<>();

        for (StringBuilder prop : userProps) {
            if (!checker.exists(prop.toString())) {
                wrongProps.add(prop);
            }
        }

        if (wrongProps.size() == 1) {
            String wrong = wrongProps.get(0).toString();
            System.out.println("\nThe property [" + wrong + "] is wrong.\n" +
                    "Available properties: " + Arrays.toString(Property.values()) + "\n");
            return false;
        }
        if (wrongProps.size() > 1) {
            String wrong = wrongProps.get(0).toString();
            for (int i = 1; i < wrongProps.size(); i++) {
                wrong += ", " + wrongProps.get(i);
            }
            System.out.println("The properties [" + wrong + "] are wrong.\n" +
                    "Available properties: " + Arrays.toString(Property.values()) + "\n");
            return false;
        }

        return true;
    }

    private List<Property> propertiesFromInput(List<StringBuilder> userProperties) {

        List<Property> props = new ArrayList<>();
        for (StringBuilder name : userProperties) {
            Property prop = Property.valueOf(name.toString());
            props.add(prop);
        }
        return props;
    }

    private boolean checkMutuallyExclusiveProperties(List<Property> props) {
        for (Property prop : props) {
            for (Property propCheck : props) {
                if (!prop.equals(propCheck) && prop.mutuallyExclusive(propCheck)) {
                    System.out.println("\nThe request contains mutually exclusive properties: [" + prop.getValue().toUpperCase() + ", " + propCheck.getValue().toUpperCase() + "]\n" +
                            "There are no numbers with these properties.\n");
                    return false;
                }
            }
        }
        return true;
    }

    private void complexFiltering(long number, long n, List<Property> props) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        List<Method> methods = new ArrayList<>();

        for (Property prop : props) {
            String mName = prop.getValue();
            Method method = checker.getClass().getMethod(mName, long.class);
            methods.add(method);
        }

        for (long i = number; n != 0; i++) {

            List<Boolean> filters = new ArrayList<>();

            for (Method method : methods) {
                boolean filter = (Boolean) method.invoke(checker, i);
                filters.add(filter);
            }

            if (allTrue(filters)) {
                propertiesLine(i);
                n--;
            }
        }
    }

    private boolean allTrue(List<Boolean> list) {
        for (Boolean bool : list) {
            if (!bool) {
                return false;
            }
        }
        return true;
    }

    private boolean areNumbersNatural(long number, long consecutive) {
        if (number < 1) {
            System.out.println("\nThe first parameter should be a natural number or zero.\n");
            return false;
        } else if (consecutive < 1) {
            System.out.println("\nThe second parameter should be a natural number.\n");
            return false;
        }
        return true;
    }

    private void printInstructions() {
        System.out.println("Supported requests:\n" +
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the second parameter shows how many consecutive numbers are to be printed;\n" +
                "- two natural numbers and a property to search for;\n" +
                "- two natural numbers and two properties to search for;\n" +
                "- a property preceded by minus must not be present in numbers;\n" +
                "- separate the parameters with one space;\n" +
                "- enter 0 to exit.\n");
    }

    private void printPropertiesTable(long number) {
        System.out.printf("\nProperties of %d" +
                        "\n\t\tgapful: " + checker.gapful(number) +
                        "\n\t\teven: " + checker.even(number) +
                        "\n\t\todd: " + checker.odd(number) +
                        "\n\t\tbuzz: " + checker.buzz(number) +
                        "\n\t\tduck: " + checker.duck(number) +
                        "\n\t\tpalindromic: " + checker.palindromic(number) +
                        "\n\t\tsquare: " + checker.square(number) +
                        "\n\t\tsunny: " + checker.sunny(number) +
                        "\n\t\tspy: " + checker.spy(number) +
                        "\n\t\thappy: " + checker.happy(number) +
                        "\n\t\tsad: " + checker.sad(number) +
                        "\n\t\tjumping: " + checker.jumping(number) + "\n\n",
                number);
    }

    private void propertiesLine(long number) {
        String buzz = "";
        String duck = "";
        String palindromic = "";
        String gapful = "";
        String spy = "";
        String square = "";
        String sunny = "";
        String jumping = "";
        String happy = "";
        String even = "";

        if (checker.buzz(number)) {
            buzz = "buzz, ";
        }

        if (checker.duck(number)) {
            duck = "duck, ";
        }

        if (checker.palindromic(number)) {
            palindromic = "palindromic, ";
        }

        if (checker.gapful(number)) {
            gapful = "gapful, ";
        }

        if (checker.spy(number)) {
            spy = "spy, ";
        }

        if (checker.square(number)) {
            square = "square, ";
        }

        if (checker.sunny(number)) {
            sunny = "sunny, ";
        }

        if (checker.jumping(number)) {
            jumping = "jumping, ";
        }

        if (checker.happy(number)) {
            happy = "happy, ";
        } else {
            happy = "sad, ";
        }

        if (checker.even(number)) {
            even = "even";
        } else {
            even = "odd";
        }

        System.out.println(number + " is " + buzz + duck + palindromic + gapful + spy + square + sunny + jumping + happy + even);
    }
}