package app.model;

import java.io.Serializable;
import java.util.ArrayList;
import app.service.FileService;

public class User implements Serializable {
    protected String username;
    protected String password;
    protected String role;
    protected ArrayList<Menu> daftarMenu;
    protected ArrayList<Transaksi> daftarTransaksi;
    protected FileService fs;

    public User(String username, String password, String role,
                ArrayList<Menu> daftarMenu, ArrayList<Transaksi> daftarTransaksi,
                FileService fs) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.daftarMenu = daftarMenu;
        this.daftarTransaksi = daftarTransaksi;
        this.fs = fs;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public void tampilkanMenu() {}
    public boolean handleOption(int opt, java.util.Scanner sc) { return false; }
}
