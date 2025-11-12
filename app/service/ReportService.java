package app.service;

import app.model.Transaksi;

import java.util.ArrayList;

/**
 * Service untuk membuat laporan pendapatan
 */
public class ReportService {
    private ArrayList<Transaksi> daftarTransaksi;

    public ReportService(ArrayList<Transaksi> daftarTransaksi) {
        this.daftarTransaksi = daftarTransaksi;
    }

    public void printReportAll() {
        double total = 0;
        int count = 0;
        for (Transaksi t : daftarTransaksi) {
            total += t.getTotal();
            count++;
        }
        System.out.println("=== LAPORAN TRANSAKSI ===");
        System.out.println("Jumlah transaksi: " + count);
        System.out.printf("Total pendapatan: Rp%.0f\n", total);
    }
}
