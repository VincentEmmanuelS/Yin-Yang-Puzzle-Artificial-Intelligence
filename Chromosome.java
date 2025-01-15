/**
 * @author Vincent Emmanuel Suwardy / 6182201067
 * @author Andrew Sebastian Aruan / 6182201004
 */

import java.util.Random;

/**
 * Kromosom yang mewakili solusi potensial dalam algoritma genetika
 */
public class Chromosome {
    private final int[][] grid;     // Matriks grid puzzle yang mewakili kromosom
    private final int fitness;      // Nilai fitness untuk kromosom ini

    /**
     * Konstruktor untuk membuat kromosom baru dengan ukuran puzzle tertentu
     * @param size Ukuran grid puzzle
     * @param puzzle Puzzle Yin-Yang yang akan diselesaikan
     */
    public Chromosome(int size, YinYangPuzzle puzzle) {
        grid = new int[size][size];
        // Inisialisasi grid dengan bidak acak (W atau B) hanya pada posisi yang kosong (.)
        Random rand = GlobalVariable.RANDOM;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (puzzle.getGrid()[i][j] == 0) {              // Jika sel kosong (.), isi dengan acak W atau B
                    grid[i][j] = rand.nextInt(2) + 1;     // 1 untuk W, 2 untuk B
                } 
                else {
                    grid[i][j] = puzzle.getGrid()[i][j];        // Mempertahankan nilai W (1) atau B (2)
                }
            }
        }
        this.fitness = calculateFitness(puzzle);            // Hitung fitness dari kromosom ini
    }

    /**
     * Menghitung nilai fitness dari kromosom ini
     * @param puzzle Puzzle Yin-Yang yang akan diselesaikan
     * @return Fitness dari kromosom ini
     */
    public int calculateFitness(YinYangPuzzle puzzle) {
        int fitness = 100; // Nilai default fitness dimulai dari 100
    
        // Menghitung penalti untuk kotak 2x2 yang terbentuk
        int boxPenalty = 0;
        for (int i = 0; i < puzzle.getSize() - 1; i++) {
            for (int j = 0; j < puzzle.getSize() - 1; j++) {
                // Cek apakah ada kotak 2x2 yang warna pada posisi yang berbeda
                if (grid[i][j] != grid[i][j + 1] || grid[i][j] != grid[i + 1][j] || grid[i][j] != grid[i + 1][j + 1]) {
                    boxPenalty++;
                }
            }
        }
        fitness -= boxPenalty * 3; // Penalti 2 untuk setiap kotak 2x2 yang terbentuk
    
        // Menghitung penalti untuk pemisahan warna menjadi beberapa wilayah
        // Penanganan untuk wilayah yang terpisah berdasarkan warna
        int whiteRegions = countRegions(1); // Menghitung jumlah wilayah putih
        int blackRegions = countRegions(2); // Menghitung jumlah wilayah hitam
    
        // Penalti untuk jumlah wilayah yang lebih dari 1
        fitness -= (whiteRegions - 1) * 2;
        fitness -= (blackRegions - 1) * 2;
    
        return fitness;
    }
    
    /**
     * Menghitung jumlah wilayah yang terpisah berdasarkan warna
     * @param color Warna yang dihitung (1 untuk putih, 2 untuk hitam)
     * @return Jumlah wilayah yang terpisah
     */
    private int countRegions(int color) {
        boolean[][] visited = new boolean[grid.length][grid.length];
        int regionCount = 0;
    
        // Menelusuri grid untuk menghitung jumlah wilayah yang terpisah
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == color && !visited[i][j]) {
                    // Temukan wilayah baru dengan pencarian DFS atau BFS
                    exploreRegion(i, j, color, visited);
                    regionCount++;
                }
            }
        }
    
        return regionCount;
    }
    
    /**
     * Menjelajahi wilayah yang terhubung menggunakan DFS
     * @param i Baris saat ini
     * @param j Kolom saat ini
     * @param color Warna wilayah
     * @param visited Matriks yang menandakan apakah suatu sel telah dikunjungi
     */
    private void exploreRegion(int i, int j, int color, boolean[][] visited) {
        // Jika keluar dari grid atau sudah dikunjungi atau bukan warna yang dicari
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[i].length || visited[i][j] || grid[i][j] != color) {
            return;
        }
    
        visited[i][j] = true; // Tandai sel ini sudah dikunjungi
    
        // Jelajahi sel-sel yang terhubung (atas, bawah, kiri, kanan)
        exploreRegion(i - 1, j, color, visited); // Atas
        exploreRegion(i + 1, j, color, visited); // Bawah
        exploreRegion(i, j - 1, color, visited); // Kiri
        exploreRegion(i, j + 1, color, visited); // Kanan
    }

    /**
     * Mengembalikan nilai fitness dari kromosom ini
     * @return Fitness dari kromosom ini
     */
    public int getFitness() {
        return fitness; // Mengembalikan fitness
    }

    /**
     * Melakukan crossover antara dua kromosom untuk menghasilkan keturunan
     * @param other Kromosom orang tua kedua
     * @return Kromosom keturunan
     */
    // public Chromosome crossover(Chromosome other) {
    //     int size = grid.length;
    //     Random rand = GlobalVariable.RANDOM;
    //     int crossoverPoint = rand.nextInt(size);
    
    //     // Membuat grid untuk kromosom keturunan
    //     int[][] newGrid = new int[size][size];
    
    //     // Menyalin bagian dari orang tua pertama
    //     for (int i = 0; i < crossoverPoint; i++) {
    //         for (int j = 0; j < size; j++) {
    //             if (this.grid[i][j] == 0) {         // Hanya salin jika sel kosong
    //                 newGrid[i][j] = this.grid[i][j];
    //             } 
    //             else {
    //                 newGrid[i][j] = other.grid[i][j];
    //             }
    //         }
    //     }
    
    //     // Menyalin bagian dari orang tua kedua
    //     for (int i = crossoverPoint; i < size; i++) {
    //         for (int j = 0; j < size; j++) {
    //             if (this.grid[i][j] == 0) {         // Hanya salin jika sel kosong
    //                 newGrid[i][j] = this.grid[i][j];
    //             } 
    //             else {
    //                 newGrid[i][j] = other.grid[i][j];
    //             }
    //         }
    //     }

    /**
     * Melakukan crossover antara dua kromosom untuk menghasilkan keturunan
     * @param other Kromosom orang tua kedua
     * @return Kromosom keturunan
     */
    public Chromosome crossover(Chromosome other) {
        Random rand = GlobalVariable.RANDOM;

        // Memeriksa apakah crossover harus dilakukan berdasarkan CROSSOVER_RATE
        if (rand.nextDouble() >= GlobalVariable.CROSSOVER_RATE) {
            // Jika tidak melakukan crossover, salin salah satu orang tua secara langsung
            return new Chromosome(this.grid.length, new YinYangPuzzle(this.grid));
        }

        int size = grid.length;
        int crossoverPoint = rand.nextInt(size);

        // Membuat grid untuk kromosom keturunan
        int[][] newGrid = new int[size][size];

        // Menyalin bagian dari orang tua pertama
        for (int i = 0; i < crossoverPoint; i++) {
            for (int j = 0; j < size; j++) {
                if (this.grid[i][j] == 0) {         // Hanya salin jika sel kosong
                    newGrid[i][j] = this.grid[i][j];
                } 
                else {
                    newGrid[i][j] = other.grid[i][j];
                }
            }
        }

        // Menyalin bagian dari orang tua kedua
        for (int i = crossoverPoint; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.grid[i][j] == 0) {         // Hanya salin jika sel kosong
                    newGrid[i][j] = this.grid[i][j];
                } 
                else {
                    newGrid[i][j] = other.grid[i][j];
                }
            }
        }

        return new Chromosome(size, new YinYangPuzzle(newGrid));
    }

    /**
     * Melakukan mutasi pada kromosom ini
     */
    public void mutate() {
        Random rand = GlobalVariable.RANDOM;

        if (rand.nextDouble() < GlobalVariable.MUTATION_RATE) {
            int i = rand.nextInt(grid.length);
            int j = rand.nextInt(grid.length);

            // Hanya lakukan mutasi pada sel kosong
            if (grid[i][j] == 0) {
                grid[i][j] = rand.nextInt(2) + 1; // 1 untuk W, 2 untuk B
            }
        }
    }

    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : grid) {
            for (int cell : row) {
                switch (cell) {
                    case 0:
                        sb.append(". ");
                        break;
                    case 1:
                        sb.append("W ");
                        break;
                    case 2:
                        sb.append("B ");
                        break;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}