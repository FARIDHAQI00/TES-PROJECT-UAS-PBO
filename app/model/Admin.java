package app.model;

import app.service.OperasiData;
import app.service.ReportService;
import app.service.FileService;
import app.utils.CLIHelper;
import app.utils.Validator;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Admin class - memiliki hak CRUD dan laporan
 */
public class Admin extends User implements OperasiData {

    private ReportService reportService;
    private FileService fs;

    public Admin(String username, String password,
                 ArrayList<Menu> daftarMenu, ArrayList<Transaksi> daftarTransaksi,
                 FileService fs) {
        super(username, password, "admin", daftarMenu, daftarTransaksi, fs);
        this.fs = fs;
        this.reportService = new ReportService(daftarTransaksi);
    }

    @Override
    public void tampilkanMenu() {
        System.out.println("=== ADMIN MENU ===");
        System.out.println("1. Tambah Menu");
        System.out.println("2. Edit Menu");
        System.out.println("3. Hapus Menu");
        System.out.println("4. Lihat Daftar Menu");
        System.out.println("5. Lihat Semua Transaksi");
        System.out.println("6. Laporan Pendapatan");
        System.out.println("7. Simpan Data ke File");
        System.out.println("8. Logout");
    }

    @Override
    public boolean handleOption(int opt, Scanner sc) {
        switch (opt) {
            case 1 -> tambahData(sc);
            case 2 -> editData(sc);
            case 3 -> hapusData(sc);
            case 4 -> lihatMenu();
            case 5 -> lihatTransaksi();
            case 6 -> reportService.printReportAll();
            case 7 -> {
                fs.saveMenus(daftarMenu);
                fs.saveTransaksi(daftarTransaksi);
                CLIHelper.printSuccess("Data disimpan ke file.");
            }
            case 8 -> {
                CLIHelper.printInfo("Logout...");
                return true;
            }
            default -> CLIHelper.printError("Pilihan tidak valid.");
        }
        return false;
    }

    // ================================================================
    // ========== Implementasi dari Interface OperasiData ============
    // ================================================================

    @Override
    public void tambahData(Scanner sc) {
        System.out.println("--- Tambah Menu Baru ---");
        System.out.print("ID (ex: M001): ");
        String id = sc.nextLine().trim();

        System.out.print("Nama Menu: ");
        String nama = sc.nextLine().trim();

        System.out.print("Kategori (makanan/minuman): ");
        String kategori = sc.nextLine().trim();

        System.out.print("Harga: ");
        double harga = Validator.scanDouble(sc);

        Menu m = new Menu(id, nama, kategori, harga);
        daftarMenu.add(m);
        CLIHelper.printSuccess("Menu berhasil ditambahkan!");
    }

    @Override
    public void editData(Scanner sc) {
        System.out.println("--- Edit Menu ---");
        System.out.print("Masukkan ID Menu: ");
        String id = sc.nextLine().trim();

        Menu found = null;
        for (Menu m : daftarMenu) {
            if (m.getId().equalsIgnoreCase(id)) {
                found = m;
                break;
            }
        }

        if (found == null) {
            CLIHelper.printError("Menu tidak ditemukan!");
            return;
        }

        System.out.print("Nama baru (" + found.getNama() + "): ");
        String nama = sc.nextLine().trim();
        if (!nama.isEmpty()) found.setNama(nama);

        System.out.print("Kategori baru (" + found.getKategori() + "): ");
        String kategori = sc.nextLine().trim();
        if (!kategori.isEmpty()) found.setKategori(kategori);

        System.out.print("Harga baru (" + found.getHarga() + "): ");
        String hargaInput = sc.nextLine().trim();
        if (!hargaInput.isEmpty()) {
            try {
                double harga = Double.parseDouble(hargaInput);
                found.setHarga(harga);
            } catch (NumberFormatException e) {
                CLIHelper.printError("Harga tidak valid, perubahan diabaikan.");
            }
        }

        CLIHelper.printSuccess("Menu berhasil diperbarui!");
    }

    @Override
    public void hapusData(Scanner sc) {
        System.out.println("--- Hapus Menu ---");
        System.out.print("Masukkan ID Menu: ");
        String id = sc.nextLine().trim();

        Menu target = null;
        for (Menu m : daftarMenu) {
            if (m.getId().equalsIgnoreCase(id)) {
                target = m;
                break;
            }
        }

        if (target == null) {
            CLIHelper.printError("Menu tidak ditemukan!");
            return;
        }

        daftarMenu.remove(target);
        CLIHelper.printSuccess("Menu berhasil dihapus!");
    }

    // ================================================================
    // =============== Method Pendukung (Tambahan) ====================
    // ================================================================

    private void lihatMenu() {
        if (daftarMenu.isEmpty()) {
            CLIHelper.printInfo("Belum ada menu yang terdaftar.");
            return;
        }
        CLIHelper.printTableMenu(daftarMenu);
    }

    private void lihatTransaksi() {
        if (daftarTransaksi.isEmpty()) {
            CLIHelper.printInfo("Belum ada transaksi.");
            return;
        }

        for (Transaksi t : daftarTransaksi) {
            System.out.println(t.prettyString());
            CLIHelper.printLine();
        }
    }
}
