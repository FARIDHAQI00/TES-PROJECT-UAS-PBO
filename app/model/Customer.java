package app.model;

import app.service.KasirService;
import app.utils.CLIHelper;
import app.utils.Validator;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Customer (kasir/pelanggan) - bisa pesan dan lihat riwayat
 */
public class Customer extends User {

    private KasirService kasirService;
    private app.service.FileService fs;

    public Customer(String username, String password,
                    ArrayList<Menu> daftarMenu, ArrayList<Transaksi> daftarTransaksi,
                    app.service.FileService fs) {
        super(username, password, "user", daftarMenu, daftarTransaksi, fs);
        this.fs = fs;
        this.kasirService = new KasirService(daftarMenu, daftarTransaksi, fs, this.username);
    }

    @Override
    public void tampilkanMenu() {
        System.out.println("=== USER MENU ===");
        System.out.println("1. Lihat Daftar Menu");
        System.out.println("2. Pesan Menu");
        System.out.println("3. Cari Menu");
        System.out.println("4. Sort Menu (Harga)");
        System.out.println("5. Lihat Riwayat Transaksi Saya");
        System.out.println("6. Logout");
    }

    @Override
    public boolean handleOption(int opt, Scanner sc) {
        switch (opt) {
            case 1: CLIHelper.printTableMenu(daftarMenu); break;
            case 2: kasirService.startOrder(sc); break;
            case 3:
                System.out.print("Masukkan keyword nama: ");
                String kw = sc.nextLine().trim();
                CLIHelper.printTableMenu(KasirService.searchMenu(daftarMenu, kw));
                break;
            case 4:
                System.out.println("1. Ascending\n2. Descending");
                System.out.print("Pilih: ");
                int o = Validator.scanInt(sc);
                kasirService.sortMenuByPrice(daftarMenu, o == 1);
                CLIHelper.printSuccess("Done. Lihat daftar menu.");
                CLIHelper.printTableMenu(daftarMenu);
                break;
            case 5:
                kasirService.printMyTransactions(username);
                break;
            case 6:
                CLIHelper.printInfo("Logout...");
                return true;
            default: CLIHelper.printError("Pilihan tidak valid.");
        }
        return false;
    }
}
