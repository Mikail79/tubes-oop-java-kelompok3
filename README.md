# 🎬 Sistem Pemesanan Tiket Bioskop (Java Swing)

> Tugas Besar Mata Kuliah Pemrograman Berorientasi Objek (PBO) - Kelompok 3

Aplikasi desktop sederhana berbasis Java Swing untuk simulasi pemesanan tiket bioskop. Proyek ini menerapkan konsep **Object-Oriented Programming (OOP)** seperti Inheritance, Polymorphism, Encapsulation, dan Exception Handling.

## 👥 Anggota Kelompok 3
| NIM | Nama | Peran |
| :--- | :--- | :--- |
| **[NIM MIKAIL]** | **Muhammad Mikail Alfasya** | **Ketua** - GUI Design & Main Integration |
| [NIM FATHIA] | Fathia Artha Utami | Backend Logic (OOP Core) |
| [NIM AFLIANA] | Afliana Amanda | Error Handling & Testing |

## ✨ Fitur Utama
1.  **Pemilihan Film:** Menggunakan `JComboBox` untuk memilih film yang sedang tayang.
2.  **Visualisasi Kursi:** Layout kursi menggunakan `GridLayout` interaktif (Hijau = Kosong, Merah = Dipilih, Abu-abu = Terjual).
3.  **Perhitungan Otomatis:** Menghitung total harga tiket menggunakan logika OOP (`Tiket` & `TiketBioskop`).
4.  **Sistem Pembayaran:** Validasi uang pembayaran dan perhitungan kembalian.
5.  **Exception Handling:**
    * Validasi jika belum memilih kursi.
    * Validasi jika uang pembayaran kurang (`PembayaranGagalException`).
    * Validasi input angka (`NumberFormatException`).

## 🛠️ Struktur Proyek (OOP)
* **Abstract Class (`Tiket.java`):** Blueprint dasar untuk tiket dengan enkapsulasi data film.
* **Inheritance (`TiketBioskop.java`):** Subclass yang mengimplementasikan logika perhitungan harga spesifik.
* **GUI (`AplikasiBioskop.java`):** Interface pengguna berbasis JFrame dan Event Listener.
* **Custom Exception (`PembayaranGagalException.java`):** Menangani error spesifik transaksi.

## 🚀 Cara Menjalankan
Pastikan Java Development Kit (JDK) sudah terinstall.

1.  **Clone Repository:**
    ```bash
    git clone [https://github.com/](https://github.com/)[USERNAME_MIKAIL]/tubes-oop-java-kelompok3.git
    ```
2.  **Masuk ke Folder:**
    ```bash
    cd tubes-oop-java-kelompok3
    ```
3.  **Compile Kode:**
    ```bash
    javac src/*.java
    ```
4.  **Jalankan Aplikasi:**
    ```bash
    java src.AplikasiBioskop
    ```

---
**Politeknik STMI Jakarta - 2025**
