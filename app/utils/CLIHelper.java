package app.utils;

import app.model.Menu;

import java.util.ArrayList;

/**
 * Helper untuk tampilan CLI (warna sederhana & utilitas)
 */
public class CLIHelper {
    // ANSI colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    public static void printBanner() {
        System.out.println(BLUE + "==== Selamat datang di RestiCash+ ====" + RESET);
    }

    public static void clearScreen() {
        // not portable but works in many terminals
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printLine() {
        System.out.println("----------------------------------------");
    }

    public static void printError(String s) {
        System.out.println(RED + "[ERROR] " + s + RESET);
    }

    public static void printSuccess(String s) {
        System.out.println(GREEN + "[SUCCESS] " + s + RESET);
    }

    public static void printInfo(String s) {
        System.out.println(YELLOW + "[INFO] " + s + RESET);
    }

    public static void printTableMenu(ArrayList<Menu> menus) {
        if (menus == null || menus.isEmpty()) {
            printInfo("Daftar menu kosong.");
            return;
        }
        System.out.printf("\n%-6s %-25s %-10s %-8s\n", "ID", "NAMA", "KATEGORI", "HARGA");
        System.out.println("-----------------------------------------------------------");
        for (Menu m : menus) {
            System.out.printf("%-6s %-25s %-10s Rp%.0f\n", m.getId(), m.getNama(), m.getKategori(), m.getHarga());
        }
    }
}
