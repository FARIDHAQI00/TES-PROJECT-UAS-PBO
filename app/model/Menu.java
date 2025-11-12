package app.model;

/**
 * Model Menu (makanan/minuman)
 */
public class Menu {
    private String id;
    private String nama;
    private String kategori;
    private double harga;

    public Menu(String id, String nama, String kategori, double harga) {
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        this.harga = harga;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public String getKategori() { return kategori; }
    public double getHarga() { return harga; }

    public void setId(String id) { this.id = id; }
    public void setNama(String nama) { this.nama = nama; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public void setHarga(double harga) { this.harga = harga; }

    @Override
    public String toString() {
        return id + "|" + nama + "|" + kategori + "|" + harga;
    }
}
