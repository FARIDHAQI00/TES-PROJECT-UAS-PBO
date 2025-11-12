package app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Model Transaksi
 */
public class Transaksi {
    private String id;
    private String username; // siapa yang melakukan transaksi
    private ArrayList<OrderItem> items;
    private double total;
    private String tanggal;
    private String metodeBayar; // Tunai / QRIS

    public Transaksi(String username, ArrayList<OrderItem> items, double total, String metodeBayar) {
        this.id = "T-" + UUID.randomUUID().toString().substring(0,8);
        this.username = username;
        this.items = items;
        this.total = total;
        this.metodeBayar = metodeBayar;
        this.tanggal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public ArrayList<OrderItem> getItems() { return items; }
    public double getTotal() { return total; }
    public String getTanggal() { return tanggal; }
    public String getMetodeBayar() { return metodeBayar; }

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("|").append(username).append("|").append(tanggal).append("|").append(total).append("|").append(metodeBayar).append("|");
        for (OrderItem oi : items) {
            sb.append(oi.getMenuId()).append(",").append(oi.getNama()).append(",").append(oi.getQty()).append(",").append(oi.getSubtotal()).append(";");
        }
        return sb.toString();
    }

    public String prettyString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaksi ID: ").append(id).append("\n");
        sb.append("User: ").append(username).append(" | Tanggal: ").append(tanggal).append(" | Metode: ").append(metodeBayar).append("\n");
        sb.append("Items:\n");
        for (OrderItem oi : items) {
            sb.append(String.format(" - %s x%d = Rp%.0f\n", oi.getNama(), oi.getQty(), oi.getSubtotal()));
        }
        sb.append(String.format("TOTAL: Rp%.0f\n", total));
        return sb.toString();
    }

    // Nested helper class for order item
    public static class OrderItem {
        private String menuId;
        private String nama;
        private int qty;
        private double subtotal;

        public OrderItem(String menuId, String nama, int qty, double subtotal) {
            this.menuId = menuId;
            this.nama = nama;
            this.qty = qty;
            this.subtotal = subtotal;
        }

        public String getMenuId() { return menuId; }
        public String getNama() { return nama; }
        public int getQty() { return qty; }
        public double getSubtotal() { return subtotal; }
    }
}
