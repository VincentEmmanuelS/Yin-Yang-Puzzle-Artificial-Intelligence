/**
 * @author Vincent Emmanuel Suwardy / 6182201067
 */

import java.io.*;
import java.util.*;

/**
 * Algoritma Genetik untuk Menyelesaikan Puzzle Yin-Yang
 */
public class GeneticAlgorithmYinYang {

    // Seed untuk keperluan reproduksibilitas (agar hasilnya konsisten)
    private static final long SEED = new Random().nextLong(1000); // Ganti dengan nilai seed yang diinginkan

    // Penghasil angka acak dengan seed tetap
    public static final Random RANDOM = new Random(SEED);

    // Parameter untuk algoritma genetik
    public static final int POPULATION_SIZE = 100;      // Ukuran populasi
    public static final int MAX_GENERATIONS = 1000;     // Maksimal jumlah generasi
    public static final double MUTATION_RATE = 0.1;     // Tingkat mutasi
    public static final double CROSSOVER_RATE = 0.7;    // Tingkat crossover (pertukaran gen antar individu)

    public static void main(String[] args) throws IOException {
        // Menampilkan seed yang digunakan untuk keperluan reproduksibilitas
        System.out.println("Menggunakan seed: " + SEED);
    
        // File input dan output untuk puzzle
        String inputFile = "puzzle_input.txt";      // Nama file input untuk puzzle
        String outputFile = "puzzle_solution.txt";  // Nama file output untuk solusi
    
        // Memuat puzzle dari file input
        YinYangPuzzle puzzle = YinYangPuzzle.loadFromFile(inputFile);
    
        // Menyelesaikan puzzle menggunakan algoritma genetik
        GeneticSolver solver = new GeneticSolver(puzzle);
        GeneticResult result = solver.solve();
    
        // Menyimpan solusi dan laporan ke file output
        saveResultToFile(outputFile, result);
    }
    
    /**
     * Menyimpan solusi dan laporan ke dalam file output.
     * @param filePath Path ke file output
     * @param result Hasil solusi dan laporan
     * @throws IOException Jika terjadi kesalahan saat menulis file
     */
    private static void saveResultToFile(String filePath, GeneticResult result) throws IOException {
        // Menulis solusi dan laporan ke dalam file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Solusi:\n");
            writer.write(result.getSolution().toString()); // Menampilkan grid dalam format yang diperlukan
            writer.write("\n\nLaporan:\n");
            writer.write("Seed: " + SEED + "\n"); // Menyertakan seed yang digunakan dalam laporan
            writer.write("Generasi: " + result.getGenerations() + "\n"); // Menyertakan jumlah generasi
            writer.write("Fitness Akhir: " + result.getFitness() + "\n"); // Menyertakan nilai fitness akhir
        }
    }    
}
