package app.service;

import app.model.Menu;
import app.model.Transaksi;
import app.utils.CLIHelper;
import app.utils.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Service untuk menangani order dan checkout
 */
public class KasirService {
    private ArrayList<Menu> daftarMenu;
    private ArrayList<Transaksi> daftarTransaksi;
    private FileService fs;
    private String username; // user yang sedang transaksi

    public KasirService(ArrayList<Menu> daftarMenu, ArrayList<Transaksi> daftarTransaksi, FileService fs, String username) {
        this.daftarMenu = daftarMenu;
        this.daftarTransaksi = daftarTransaksi;
        this.fs = fs;
        this.username = username;
    }

    /**
     * Start proses order
     */
    public void startOrder(Scanner sc) {
        ArrayList<Transaksi.OrderItem> cart = new ArrayList<>();
        boolean adding = true;
        while (adding) {
            CLIHelper.printTableMenu(daftarMenu);
            System.out.print("\nPilih ID Menu (atau ketik 'done' untuk checkout): ");
            String id = sc.nextLine().trim();
            if (id.equalsIgnoreCase("done")) break;
            Menu chosen = null;
            for (Menu m : daftarMenu) if (m.getId().equalsIgnoreCase(id)) chosen = m;
            if (chosen == null) {
                CLIHelper.printError("Menu ID tidak ditemukan.");
                continue;
            }
            System.out.print("Jumlah: ");
            int qty = Validator.scanInt(sc);
            double subtotal = qty * chosen.getHarga();
            cart.add(new Transaksi.OrderItem(chosen.getId(), chosen.getNama(), qty, subtotal));
            System.out.print("Tambah item lagi? (y/n): ");
            String yc = sc.nextLine().trim();
            if (!yc.equalsIgnoreCase("y")) adding = false;
        }
        if (cart.isEmpty()) {
            CLIHelper.printInfo("Tidak ada item, batal transaksi.");
            return;
        }
        double total = 0;
        for (Transaksi.OrderItem oi : cart) total += oi.getSubtotal();
        // pajak sederhana 10% (opsional)
        double pajak = Math.round(total * 0.10);
        double grandTotal = total + pajak;
        System.out.printf("Subtotal: Rp%.0f | Pajak 10%%: Rp%.0f | GRAND TOTAL: Rp%.0f\n\n", total, pajak, grandTotal);
        System.out.println("Metode Bayar: 1. Tunai 2. QRIS");
        System.out.print("Pilih: ");
        int m = Validator.scanInt(sc);
        String metode = (m == 2) ? "QRIS" : "Tunai";
        double bayar = 0;
        if (metode.equals("Tunai")) {
            System.out.print("Bayar (Rp): ");
            bayar = Validator.scanDouble(sc);
            if (bayar < grandTotal) {
                CLIHelper.printError("Uang tidak cukup. Transaksi dibatalkan.\n");
                return;
            }
        } else {
            // simulasi QRIS (langsung sukses)
            System.out.println("QRIS terverifikasi (simulasi).\n");
            bayar = grandTotal;
        }
        double kembalian = bayar - grandTotal;
        Transaksi trx = new Transaksi(username, cart, grandTotal, metode);
        daftarTransaksi.add(trx);
        fs.saveTransaksi(daftarTransaksi); // auto save tiap transaksi
        System.out.println("\n=== INVOICE ===");
        System.out.println(trx.prettyString());
        System.out.printf("Bayar: Rp%.0f | Kembali: Rp%.0f\n", bayar, kembalian);
        CLIHelper.printSuccess("Transaksi berhasil disimpan. ID: " + trx.getId());
    }

    public static ArrayList<Menu> searchMenu(ArrayList<Menu> menus, String keyword) {
        ArrayList<Menu> res = new ArrayList<>();
        for (Menu m : menus) {
            if (m.getNama().toLowerCase().contains(keyword.toLowerCase())) res.add(m);
        }
        return res;
    }

    public void printMyTransactions(String username) {
        boolean ada = false;
        for (Transaksi t : daftarTransaksi) {
            if (t.getUsername().equals(username)) {
                System.out.println(t.prettyString());
                CLIHelper.printLine();
                ada = true;
            }
        }
        if (!ada) CLIHelper.printInfo("Belum ada transaksi oleh " + username);
    }

    public void sortMenuByPrice(ArrayList<Menu> menus, boolean asc) {
        if (asc) {
            Collections.sort(menus, Comparator.comparingDouble(Menu::getHarga));
        } else {
            Collections.sort(menus, Comparator.comparingDouble(Menu::getHarga).reversed());
        }
    }
}
