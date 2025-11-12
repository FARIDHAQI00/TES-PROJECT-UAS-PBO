package app.service;

import java.util.Scanner;

/**
 * Interface CRUD dasar
 */
public interface OperasiData {
    void tambahData(Scanner sc);
    void editData(Scanner sc);
    void hapusData(Scanner sc);
}
