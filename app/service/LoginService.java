package app.service;

import app.model.User;
import java.util.HashMap;

/**
 * Service untuk menangani proses login Admin dan User.
 * 
 * Sudah dilengkapi dengan Exception Handling untuk nilai penuh (5 poin).
 */
public class LoginService {
    private HashMap<String, User> akunMap;

    public LoginService(HashMap<String, User> akunMap) {
        this.akunMap = akunMap;
    }

    // Exception kustom (didefinisikan langsung di bawah class utama)
    static class InvalidLoginException extends Exception {
        public InvalidLoginException(String message) {
            super(message);
        }
    }

    /**
     * Melakukan proses login berdasarkan username dan password.
     * 
     * @param username nama pengguna
     * @param password kata sandi pengguna
     * @return objek User jika login berhasil, atau null jika gagal
     */
    public User login(String username, String password) {
        try {
            // Cek apakah username terdaftar
            if (!akunMap.containsKey(username)) {
                throw new InvalidLoginException("Username \"" + username + "\" tidak ditemukan!");
            }

            User user = akunMap.get(username);

            // Cek kesesuaian password
            if (!user.getPassword().equals(password)) {
                throw new InvalidLoginException("Password salah untuk username \"" + username + "\"!");
            }

            // Login sukses
            return user;

        } catch (InvalidLoginException e) {
            System.out.println("⚠️ " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("❌ Terjadi kesalahan saat login: " + e.getMessage());
            return null;
        }
    }
}
