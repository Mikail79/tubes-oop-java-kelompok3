package src;

public abstract class Tiket {
    private String judulFilm; // Encapsulation
    protected int hargaDasar;

    public Tiket(String judulFilm, int hargaDasar) {
        this.judulFilm = judulFilm;
        this.hargaDasar = hargaDasar;
    }

    public String getJudulFilm() {
        return judulFilm;
    }

    // Abstract method: Penerapan Polymorphism
    public abstract int hitungTotalHarga(int jumlahTiket);
}