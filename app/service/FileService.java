package app.service;

import app.model.Menu;
import app.model.Transaksi;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

/**
 * Service untuk baca/tulis file (menu & transaksi)
 */
public class FileService {
    private final String menuPath;
    private final String transaksiPath;

    public FileService(String menuPath, String transaksiPath) {
        this.menuPath = menuPath;
        this.transaksiPath = transaksiPath;
        initFiles();
    }

    private void initFiles() {
        try {
            Path menu = Paths.get(menuPath);
            Path transaksi = Paths.get(transaksiPath);

            // buat folder kalau belum ada
            if (!Files.exists(menu.getParent())) {
                Files.createDirectories(menu.getParent());
            }

            // buat file kosong kalau belum ada
            if (!Files.exists(menu)) Files.createFile(menu);
            if (!Files.exists(transaksi)) Files.createFile(transaksi);

        } catch (IOException e) {
            System.err.println("Gagal inisialisasi file: " + e.getMessage());
        }
    }

    public void loadMenus(ArrayList<Menu> daftarMenu) {
        daftarMenu.clear(); // biar gak dobel kalau dipanggil ulang
        try (BufferedReader br = Files.newBufferedReader(Paths.get(menuPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // format: id|nama|kategori|harga
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    String id = parts[0];
                    String nama = parts[1];
                    String kategori = parts[2];
                    double harga = Double.parseDouble(parts[3]);
                    daftarMenu.add(new Menu(id, nama, kategori, harga));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loadMenus: " + e.getMessage());
        }
    }

    public void saveMenus(ArrayList<Menu> daftarMenu) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(menuPath))) {
            for (Menu m : daftarMenu) {
                bw.write(m.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saveMenus: " + e.getMessage());
        }
    }

    public void loadTransaksi(ArrayList<Transaksi> daftarTransaksi) {
        daftarTransaksi.clear();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(transaksiPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Transaksi t = reconstructTransaksiFromString(line);
                if (t != null) daftarTransaksi.add(t);
            }
        } catch (IOException e) {
            System.err.println("Error loadTransaksi: " + e.getMessage());
        }
    }

    private Transaksi reconstructTransaksiFromString(String line) {
        try {
            String[] parts = line.split("\\|", 6);
            if (parts.length < 5) return null;

            String username = parts[1];
            double total = Double.parseDouble(parts[3]);
            String metode = parts[4];
            String itemsStr = parts.length >= 6 ? parts[5] : "";

            ArrayList<Transaksi.OrderItem> items = new ArrayList<>();
            if (!itemsStr.isEmpty()) {
                for (String item : itemsStr.split(";")) {
                    if (item.trim().isEmpty()) continue;
                    String[] t = item.split(",");
                    items.add(new Transaksi.OrderItem(
                            t[0], t[1],
                            Integer.parseInt(t[2]),
                            Double.parseDouble(t[3])
                    ));
                }
            }
            return new Transaksi(username, items, total, metode);
        } catch (Exception e) {
            System.err.println("Gagal reconstruct transaksi: " + e.getMessage());
            return null;
        }
    }

    public void saveTransaksi(ArrayList<Transaksi> daftarTransaksi) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(transaksiPath))) {
            for (Transaksi t : daftarTransaksi) {
                bw.write(t.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saveTransaksi: " + e.getMessage());
        }
    }
}
