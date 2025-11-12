package app.utils;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Validator input sederhana
 */
public class Validator {
    public static int scanInt(Scanner sc) {
        while (true) {
            try {
                String s = sc.nextLine().trim();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                CLIHelper.printError("Input harus angka. Coba lagi: ");
            }
        }
    }

    public static double scanDouble(Scanner sc) {
        while (true) {
            try {
                String s = sc.nextLine().trim();
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                CLIHelper.printError("Input harus angka (desimal). Coba lagi: ");
            }
        }
    }
}
