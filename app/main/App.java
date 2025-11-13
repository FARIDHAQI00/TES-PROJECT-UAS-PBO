package app.main;

import app.model.*;
import app.service.*;
import app.utils.CLIHelper;
import app.utils.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        CLIHelper.clearScreen();
        CLIHelper.printBanner();

        ArrayList<Menu> daftarMenu = new ArrayList<>();
        ArrayList<Transaksi> daftarTransaksi = new ArrayList<>();
        HashMap<String, User> akunMap = new HashMap<>();

        FileService fileService = new FileService("app/data/menu.txt", "app/data/transaksi.txt");

        fileService.loadMenus(daftarMenu);
        fileService.loadTransaksi(daftarTransaksi);

        // load user dari file
        akunMap.putAll(fileService.loadUsers(daftarMenu, daftarTransaksi));

        // default akun (jika mau manual)
        Admin adminDefault = new Admin("admin", "admin123", daftarMenu, daftarTransaksi, fileService);
        akunMap.put(adminDefault.getUsername(), adminDefault);

        LoginService loginService = new LoginService(akunMap);
        Scanner sc = new Scanner(System.in);

        while (true) {
            CLIHelper.printLine();
            System.out.println("1. Login");
            System.out.println("2. Register (User)");
            System.out.println("3. Keluar");
            System.out.print("Pilih: ");
            int pilihan = Validator.scanInt(sc);

            if (pilihan == 1) {
                CLIHelper.clearScreen();
                System.out.println("=== LOGIN ===");
                System.out.print("Username: ");
                String u = sc.nextLine().trim();
                System.out.print("Password: ");
                String p = sc.nextLine().trim();

                User user = loginService.login(u, p);
                if (user == null) {
                    CLIHelper.printError("Login gagal — cek username/password");
                    continue;
                }
                CLIHelper.printSuccess("Berhasil login sebagai " + user.getRole().toUpperCase());
                boolean logout = false;
                while (!logout) {
                    CLIHelper.printLine();
                    user.tampilkanMenu();
                    System.out.print("Pilih: ");
                    int opt = Validator.scanInt(sc);
                    logout = user.handleOption(opt, sc);
                }
            } else if (pilihan == 2) {
                CLIHelper.clearScreen();
                System.out.println("=== REGISTER (User) ===");
                System.out.print("Pilih username: ");
                String nu = sc.nextLine().trim();
                if (akunMap.containsKey(nu)) {
                    CLIHelper.printError("Username sudah ada!");
                    continue;
                }
                System.out.print("Pilih password: ");
                String np = sc.nextLine().trim();

                Customer newUser = new Customer(nu, np, daftarMenu, daftarTransaksi, fileService);
                akunMap.put(nu, newUser);
                fileService.appendUser(newUser); // simpan langsung ke file
                CLIHelper.printSuccess("Registrasi berhasil. Silakan login.");
            } else if (pilihan == 3) {
                CLIHelper.printInfo("Bye! Simpan data...");
                fileService.saveMenus(daftarMenu);
                fileService.saveTransaksi(daftarTransaksi);
                fileService.saveUsers(akunMap);
                break;
            } else {
                CLIHelper.printError("Pilihan tidak valid.");
            }
        }
        sc.close();
    }
}
