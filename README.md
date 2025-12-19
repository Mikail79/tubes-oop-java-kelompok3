# 🎬 CINEMAXX PREMIER – Booking System

> **Tugas Besar Pemrograman Berorientasi Objek (OOP)**  
> Sistem pemesanan tiket bioskop berbasis desktop dengan antarmuka grafis (GUI) **Modern & Premium** menggunakan **Java Swing**.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Java_Swing-GUI-blue?style=for-the-badge)

---

## 📖 Deskripsi Proyek

**CINEMAXX PREMIER** adalah aplikasi desktop yang mensimulasikan pengalaman pemesanan tiket bioskop kelas premium.  
Aplikasi ini dibangun dengan pendekatan **Object-Oriented Programming (OOP)**, dengan pemisahan logika data, tampilan, dan kontrol ke dalam class yang modular.

Tema desain yang digunakan adalah **“Dark Luxury”**, dengan palet warna **Navy Gelap** dan **Aksen Emas**, serta dilengkapi **carousel film interaktif** layaknya aplikasi streaming modern.

---

## ✨ Fitur Utama

### 🎨 1. User Interface (UI) Premium
- **Modern Design**: Gradasi warna kustom, tombol rounded, dan layout responsif.
- **Hero Banner**: Banner film besar di halaman utama.
- **Horizontal Movie Carousel**: Daftar film dapat di-scroll ke samping seperti Netflix / Disney+.

### 💺 2. Sistem Pemilihan Kursi (Seat Selection)
- **Visualisasi Nyata**: Kursi digambar manual menggunakan `Graphics2D` lengkap dengan sandaran.
- **Smart Logic Warna Kursi**:
  - 🟢 / 🔵 **Hijau / Biru Pucat** → Kursi tersedia  
  - 🟡 **Emas** → Kursi sedang dipilih  
  - 🔴 **Merah** → Kursi sudah terjual
- **Persistensi Data**: Status kursi tiap film tersimpan dan tidak saling tertimpa.

### 💳 3. Transaksi & Validasi
- **Real-time Calculation**: Total harga dihitung otomatis.
- **Payment Simulation**: Validasi uang pembayaran (uang kurang tidak bisa lanjut).
- **E-Receipt**: Struk digital berisi detail film, kursi (A1, A2, dst), total, dan kembalian.

---

## 📂 Struktur Folder Proyek

Pastikan struktur folder seperti berikut agar resource gambar dapat dimuat dengan benar:

```text
tubes-oop-java-kelompok3/
│
├── resources/               <-- (WAJIB: file gambar .jpg/.png)
│   ├── avatar.jpg
│   ├── spiderman.jpg
│   ├── oppenheimer.jpg
│   ├── barbie.png
│   ├── mission.jpg
│   ├── batman.jpg
│   ├── interstellar.jpg
│   └── inception.jpg
│
├── src/                     <-- Source Code Java
│   ├── AplikasiBioskop.java (Main Class)
│   ├── LoginPanel.java
│   ├── LandingPanel.java
│   ├── SeatPanel.java
│   ├── PaymentPanel.java
│   └── ReceiptPanel.java
│
└── README.md
````

---

## 🚀 Cara Menjalankan Aplikasi

### 🧰 Prasyarat

* Java Development Kit (**JDK 8** atau lebih baru)
* IDE Java (IntelliJ IDEA, NetBeans, atau VS Code)

### ▶️ Langkah Menjalankan

1. Clone atau download repositori ini.
2. Pastikan folder `resources` berisi gambar poster sesuai nama file di kode.
3. Buka project di IDE.
4. Jalankan file utama:

   ```
   src/AplikasiBioskop.java
   ```

### 🖥️ Menjalankan via Terminal (Command Line)

```bash
# Compile semua file Java
javac src/*.java

# Jalankan program
java -cp src AplikasiBioskop
```

---

## 🛠️ Penjelasan Modul (Class)

| Nama Class          | Fungsi                                                              |
| ------------------- | ------------------------------------------------------------------- |
| **AplikasiBioskop** | Main JFrame, mengatur `CardLayout`, navigasi panel, dan data global |
| **LoginPanel**      | Halaman login dengan validasi username dan desain floating card     |
| **LandingPanel**    | Halaman utama berisi Hero Banner & Carousel Film                    |
| **SeatPanel**       | Logika pemilihan kursi dan validasi kursi terjual                   |
| **PaymentPanel**    | Ringkasan pesanan dan proses pembayaran                             |
| **ReceiptPanel**    | Menampilkan struk digital transaksi                                 |

---

## 📸 Screenshots

> *(Tambahkan screenshot aplikasi di sini)*

* **Login Page** – Tampilan login elegan
* **Movie Selection** – Carousel film interaktif
* **Seat Booking** – Denah kursi bioskop
* **Receipt** – Struk digital pembayaran

---

## 👥 Anggota Kelompok 3

Proyek ini disusun untuk memenuhi tugas mata kuliah **Pemrograman Berorientasi Objek**.

* **Muhammad Mikail Alfasya** – Lead Developer & UI/UX
* **Fathia Artha Utami** – Backend Logic
* **Afliana Amanda** – Documentation & Testing

---

## 💡 Tips Tambahan

1. File `README.md` ini akan otomatis tampil sebagai halaman utama repositori GitHub.
2. Bagian **Screenshots** sangat disarankan untuk memperkuat presentasi proyek.
3. Gunakan gambar hasil screenshot aplikasi dan simpan di folder `resources` atau `docs`.

---

© 2025 **Kelompok 3** – Politeknik STMI Jakarta

```

Kalau mau, aku juga bisa:
- Menyesuaikan README ke **bahasa Inggris**
- Menambahkan **diagram OOP (UML)**
- Merapikan README agar **siap submit ke dosen / GitHub publik**
```
