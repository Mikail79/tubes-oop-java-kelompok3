package src;

public class TiketBioskop extends Tiket {
    
    public TiketBioskop(String judulFilm, int harga) {
        super(judulFilm, harga);
    }

    @Override
    public int hitungTotalHarga(int jumlahTiket) {
        // Logika bisnis sederhana
        return this.hargaDasar * jumlahTiket;
    }
}