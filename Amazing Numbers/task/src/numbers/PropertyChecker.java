package numbers;

public class PropertyChecker {

    public boolean even(long number) {
        return number % 2 == 0;
    }

    public boolean odd(long number) {
        return !even(number);
    }

    public boolean buzz(long number) {

        return number % 7 == 0 || number % 10 == 7;
    }

    public boolean duck(long number) {

        String n = String.valueOf(number);

        return n.substring(1).contains("0");
    }

    public boolean palindromic(long number) {

        String n = String.valueOf(number);
        String reversed = new StringBuilder(n).reverse().toString();

        return n.equals(reversed);
    }

    public boolean gapful(long number) {

        String n = String.valueOf(number);

        if (n.length() < 3) {
            return false;
        }

        long concat = Long.parseLong(n.substring(0, 1) + n.substring(n.length() - 1));

        return number % concat == 0;
    }

    public boolean spy(long number) {

        long sum = 0;
        long product = 1;

        while (number > 0) {
            long temp = number % 10;
            sum += temp;
            product *= temp;
            number = number / 10;
        }

        return sum == product;

    }

    public boolean sunny(long number) {

        return square(number + 1);
    }

    public boolean square(long number) {

        double n = Math.sqrt(number);
        return number == (long) n * n;
    }

    public boolean jumping(long number) {

        String n = String.valueOf(number);

        char[] ar = n.toCharArray();

        boolean jumping = true;

        for (int i = 0; i < ar.length; i++) {

            if (i == ar.length - 1) {
                break;
            } else {
                char first = ar[i];
                char second = ar[i + 1];
                if (!(first - second == 1 || first - second == -1)) {
                    jumping = false;
                }
            }

        }
        return jumping;
    }

    public boolean happy(long number) {

        if (number == 1) {
            return true;
        }
        if (number == 4) {
            return false;
        }

        return happy(happySum(number));

    }

    public long happySum(long number) {
        long sum = 0;
        while (number > 0) {
            long rem = number % 10;
            sum = sum + (rem * rem);
            number /= 10;
        }
        return sum;
    }

    public boolean sad(long number) {
        return !happy(number);
    }

    public boolean exists(String name) {

        for (Property prop : Property.values()) {
            if (prop.getValue().toUpperCase().equals(name)) {
                return true;
            }
        }
        return false;
    }

}
