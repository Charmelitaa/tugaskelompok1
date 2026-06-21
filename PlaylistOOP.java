/*
 * Identitas Kelompok 4:
 * Nama : M. Saifulloh Huda
 * NIM  : 2902771754
 * Nama : Mohamad Akmal Pramana
 * NIM  : 2902799802
 * Nama : Sepistiani Charmelita Noya
 * NIM  : 2902791264
 *
 * Nama File : PlaylistOOP.java
 * Deskripsi : Program manajemen playlist musik sederhana menggunakan konsep OOP.
 */

import java.util.Scanner;

// ==========================================
// CLASS LAGU (Penerapan Enkapsulasi)
// ==========================================
class Lagu {
    // Atribut dibuat private agar tidak bisa diakses langsung dari luar class
    private String judul;
    private String artis;
    private double durasi;

    // Constructor
    public Lagu(String judul, String artis, double durasi) {
        this.judul = judul;
        this.artis = artis;
        this.durasi = durasi;
    }

    // Getter dan Setter (Jalur akses atribut private)
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getArtis() { return artis; }
    public void setArtis(String artis) { this.artis = artis; }

    public double getDurasi() { return durasi; }
    public void setDurasi(double durasi) { this.durasi = durasi; }

    // Method untuk menampilkan informasi lengkap lagu
    public void tampilkanInfo() {
        System.out.println("Judul  : " + judul);
        System.out.println("Artis  : " + artis);
        System.out.printf("Durasi : %.2f menit\n", durasi);
    }
}

// ==========================================
// CLASS USER (Parent Class / Inheritance)
// ==========================================
class User {
    private String nama;
    
    // Array dan counter dibuat protected static agar bisa diwarisi dan dipakai bersama oleh Admin & Member
    protected static Lagu[] daftarLagu = new Lagu[100];
    protected static int jumlahLagu = 0;

    public User(String nama) {
        this.nama = nama;
    }

    public String getNama() { return nama; }

    // Method Polymorphism (akan di-override di child class)
    public void tampilkanAkses() {
        System.out.println("User memiliki akses umum ke sistem playlist.");
    }

    // Logika: Method untuk menampilkan daftar lagu (Dipakai bersama oleh Admin dan Member)
    protected void tampilkanDaftarLagu() {
        System.out.println("\n--- Daftar Lagu ---");
        if (jumlahLagu == 0) {
            System.out.println("Belum ada lagu yang tersimpan.");
        } else {
            // Logika: Iterasi array dari indeks 0 sampai jumlahLagu
            for (int i = 0; i < jumlahLagu; i++) {
                System.out.println((i + 1) + ". " + daftarLagu[i].getJudul() + " - " + daftarLagu[i].getArtis());
            }
        }
    }
}

// ==========================================
// CLASS ADMIN (Child Class - Inheritance)
// ==========================================
class Admin extends User {
    public Admin(String nama) {
        super(nama);
    }

    // Polymorphism: Override method tampilkanAkses khusus untuk Admin
    @Override
    public void tampilkanAkses() {
        System.out.println("-> Admin dapat menambahkan lagu dan melihat daftar lagu.");
    }

    // Logika: Menambahkan objek Lagu baru ke dalam array
    public void tambahLagu(Scanner input) {
        // Cek batas kapasitas array
        if (jumlahLagu >= daftarLagu.length) {
            System.out.println("Playlist sudah penuh. Tidak dapat menambahkan lagu baru.");
            return;
        }

        System.out.println("\n--- Tambah Lagu Baru ---");
        System.out.print("Masukkan judul lagu : ");
        String judul = input.nextLine();
        System.out.print("Masukkan nama artis : ");
        String artis = input.nextLine();
        System.out.print("Masukkan durasi lagu (menit): ");
        double durasi = input.nextDouble();
        input.nextLine(); // Membersihkan buffer newline

        // Logika: Simpan objek baru di indeks ke-jumlahLagu, lalu increment counter
        daftarLagu[jumlahLagu] = new Lagu(judul, artis, durasi);
        jumlahLagu++;
        System.out.println("Lagu berhasil ditambahkan ke playlist.");
    }
}

// ==========================================
// CLASS MEMBER (Child Class - Inheritance)
// ==========================================
class Member extends User {
    public Member(String nama) {
        super(nama);
    }

    // Polymorphism: Override method tampilkanAkses khusus untuk Member
    @Override
    public void tampilkanAkses() {
        System.out.println("-> Member dapat menelusuri, melihat detail, mencari lagu, dan analisis durasi.");
    }

    // Logika: Menampilkan detail lagu berdasarkan nomor urut (indeks array)
    public void lihatDetailLagu(Scanner input) {
        tampilkanDaftarLagu();
        if (jumlahLagu == 0) return;

        System.out.print("\nMasukkan nomor lagu yang ingin dilihat: ");
        int nomor = input.nextInt();
        input.nextLine();

        // Logika: Validasi nomor agar tidak terjadi ArrayIndexOutOfBounds
        if (nomor >= 1 && nomor <= jumlahLagu) {
            System.out.println("\n--- Detail Lagu ---");
            daftarLagu[nomor - 1].tampilkanInfo(); // Array dimulai dari 0
        } else {
            System.out.println("Nomor lagu tidak valid.");
        }
    }

    // Logika: Mencari lagu dengan mencocokkan string judul (case-insensitive)
    public void cariLagu(Scanner input) {
        System.out.print("\nMasukkan judul lagu yang dicari: ");
        String keyword = input.nextLine().toLowerCase();
        boolean ditemukan = false;

        System.out.println("\n--- Hasil Pencarian ---");
        // Logika: Looping array, ubah judul ke lowercase agar pencarian tidak sensitif huruf besar/kecil
        for (int i = 0; i < jumlahLagu; i++) {
            if (daftarLagu[i].getJudul().toLowerCase().contains(keyword)) {
                daftarLagu[i].tampilkanInfo();
                System.out.println("------------------------");
                ditemukan = true;
            }
        }

        if (!ditemukan) {
            System.out.println("Lagu dengan judul tersebut tidak ditemukan.");
        }
    }

    // Logika: Menghitung rata-rata durasi dari seluruh lagu di dalam array
    public void hitungRataRataDurasi() {
        if (jumlahLagu == 0) {
            System.out.println("Playlist kosong, tidak ada yang bisa dihitung.");
            return;
        }

        double totalDurasi = 0;
        // Logika: Akumulasi total durasi dengan menjumlahkan setiap elemen array
        for (int i = 0; i < jumlahLagu; i++) {
            totalDurasi += daftarLagu[i].getDurasi();
        }

        // Logika: Bagi total durasi dengan jumlah lagu untuk mendapatkan rata-rata
        double rataRata = totalDurasi / jumlahLagu;

        System.out.printf("\n--- Analisis Playlist ---\n");
        System.out.printf("Total lagu         : %d lagu\n", jumlahLagu);
        System.out.printf("Total durasi       : %.2f menit\n", totalDurasi);
        System.out.printf("Rata-rata durasi   : %.2f menit\n", rataRata);
    }
}

// ==========================================
// CLASS UTAMA (PlaylistOOP)
// ==========================================
public class PlaylistOOP {
    public static void main(String[] args) {
        int pilihan;
        Scanner input = new Scanner(System.in);

        // Inisialisasi objek (Polymorphism: Objek child disimpan di referensi parent/child)
        Admin admin = new Admin("Admin Playlist");
        Member member = new Member("Member Playlist");

        // Data awal (Dummy data)
        daftarLaguAwal();

        do {
            System.out.println("\n==================================");
            System.out.println(" SISTEM MANAJEMEN PLAYLIST MUSIK");
            System.out.println("==================================");
            System.out.println("1. Login sebagai Admin");
            System.out.println("2. Login sebagai Member");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");
            pilihan = input.nextInt();
            input.nextLine();

            switch (pilihan) {
                case 1:
                    System.out.println("\nLogin sebagai: " + admin.getNama());
                    admin.tampilkanAkses(); // Memanggil method polymorphism
                    menuAdmin(admin, input);
                    break;

                case 2:
                    System.out.println("\nLogin sebagai: " + member.getNama());
                    member.tampilkanAkses(); // Memanggil method polymorphism
                    menuMember(member, input);
                    break;

                case 0:
                    System.out.println("Terima kasih telah menggunakan program playlist musik.");
                    break;

                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        } while (pilihan != 0);
        
        input.close();
    }

    // Method untuk mengisi data awal ke dalam array
    public static void daftarLaguAwal() {
        User.daftarLagu[0] = new Lagu("Evaluasi", "Hindia", 3.15);
        User.daftarLagu[1] = new Lagu("Sampai Jadi Debu", "Banda Neira", 4.10);
        User.daftarLagu[2] = new Lagu("Monokrom", "Tulus", 3.34);
        User.jumlahLagu = 3;
    }

    // Menu khusus Admin
    public static void menuAdmin(Admin admin, Scanner input) {
        int pilihan;
        do {
            System.out.println("\n========= MENU ADMIN =========");
            System.out.println("1. Tambah Lagu");
            System.out.println("2. Lihat Daftar Lagu");
            System.out.println("0. Kembali");
            System.out.print("Pilih menu: ");
            pilihan = input.nextInt();
            input.nextLine();

            switch (pilihan) {
                case 1: admin.tambahLagu(input); break;
                case 2: admin.tampilkanDaftarLagu(); break;
                case 0: System.out.println("Kembali ke menu utama."); break;
                default: System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 0);
    }

    // Menu khusus Member
    public static void menuMember(Member member, Scanner input) {
        int pilihan;
        do {
            System.out.println("\n========= MENU MEMBER =========");
            System.out.println("1. Lihat Daftar Lagu");
            System.out.println("2. Lihat Detail Lagu");
            System.out.println("3. Cari Lagu Berdasarkan Judul");
            System.out.println("4. Hitung Rata-rata Durasi Lagu"); // FITUR TAMBAHAN
            System.out.println("0. Kembali");
            System.out.print("Pilih menu: ");
            pilihan = input.nextInt();
            input.nextLine();

            switch (pilihan) {
                case 1: member.tampilkanDaftarLagu(); break;
                case 2: member.lihatDetailLagu(input); break;
                case 3: member.cariLagu(input); break;
                case 4: member.hitungRataRataDurasi(); break;
                case 0: System.out.println("Kembali ke menu utama."); break;
                default: System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 0);
    }
}