package view;

import java.util.Scanner;

public class InputView {
    private static final Scanner scanner = new Scanner(System.in);

    public String readLine() {
        return scanner.nextLine();
    }

    public String readOption(String... options) {
        String option;
        while (!isInOptions(option = readLine())) {}
        return option;
    }

    private boolean isInOptions(String input, String... options) {
        for (String opt : options) {
            if (input.equals(opt)) return true;
        }
        return false;
    }
}
