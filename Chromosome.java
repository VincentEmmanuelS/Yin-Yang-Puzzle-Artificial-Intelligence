/**
 * @author Vincent Emmanuel Suwardy / 6182201067
 */

/**
 * Representasi dari kandidat solusi (kromosom)
 */
public class Chromosome {
    private final int[][] solution; // Matriks solusi (grid yang mewakili kandidat solusi)
    private int fitness; // Nilai fitness dari solusi
    private final YinYangPuzzle puzzle; // Referensi ke puzzle untuk memeriksa posisi awal

    /**
     * Konstruktor untuk membuat kromosom dengan solusi acak
     * @param size Ukuran puzzle (panjang sisi)
     * @param puzzle Objek YinYangPuzzle yang mewakili puzzle yang ingin diselesaikan
     */
    public Chromosome(int size, YinYangPuzzle puzzle) {
        this.puzzle = puzzle;
        this.solution = generateRandomSolution(size); // Menghasilkan solusi acak
        this.fitness = calculateFitness(); // Menghitung nilai fitness untuk solusi ini
    }

    /**
     * Menghasilkan solusi acak berdasarkan puzzle yang diberikan
     * @param size Ukuran puzzle
     * @return Matriks solusi acak
     */
    private int[][] generateRandomSolution(int size) {
        int[][] solution = new int[size][size];
        int[][] puzzleGrid = puzzle.getGrid(); // Grid puzzle untuk memeriksa posisi tetap
        
        // Menghasilkan solusi acak tetapi mempertahankan posisi 'W' dan 'B' dari puzzle
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (puzzleGrid[i][j] == 0) {  // Jika posisi kosong
                    // Menetapkan secara acak 'W' (1) atau 'B' (2) pada sel kosong
                    solution[i][j] = GeneticAlgorithmYinYang.RANDOM.nextInt(2) + 1;  // 1 untuk W, 2 untuk B
                } 
                else {
                    solution[i][j] = puzzleGrid[i][j];  // Mempertahankan posisi 'W' dan 'B'
                }
            }
        }
        return solution;
    }

    /**
     * Konstruktor untuk membuat kromosom dengan solusi yang sudah ada
     * @param solution Matriks solusi yang diberikan
     * @param puzzle Objek YinYangPuzzle yang mewakili puzzle yang ingin diselesaikan
     */
    public Chromosome(int[][] solution, YinYangPuzzle puzzle) {
        this.solution = solution;
        this.puzzle = puzzle;
        this.fitness = calculateFitness(); // Menghitung nilai fitness untuk solusi ini
    }

    /**
     * Melakukan persilangan (crossover) antara dua kromosom untuk menghasilkan keturunan
     * @param other Kromosom lainnya yang akan dipersilangkan
     * @return Kromosom hasil persilangan
     */
    public Chromosome crossover(Chromosome other) {
        int size = solution.length;
        int[][] offspringSolution = new int[size][size];
        int[][] puzzleGrid = puzzle.getGrid(); // Untuk memeriksa apakah posisi tetap
    
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                // Mempertahankan posisi 'W' dan 'B', hanya melakukan crossover untuk sel kosong
                if (puzzleGrid[i][j] == 0) {
                    offspringSolution[i][j] = GeneticAlgorithmYinYang.RANDOM.nextDouble() < 0.5 ? solution[i][j] : other.solution[i][j];
                } 
                else {
                    offspringSolution[i][j] = puzzleGrid[i][j]; // Mempertahankan posisi 'W' dan 'B'
                }
            }
        }
        return new Chromosome(offspringSolution, puzzle); // Mengembalikan keturunan sebagai kromosom baru
    }
    
    /**
     * Melakukan mutasi pada kromosom ini dengan kemungkinan tertentu
     */
    public void mutate() {
        int size = solution.length;
        int[][] puzzleGrid = puzzle.getGrid(); // Untuk memeriksa apakah posisi tetap
    
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (puzzleGrid[i][j] == 0) { // Hanya melakukan mutasi pada sel kosong
                    if (GeneticAlgorithmYinYang.RANDOM.nextDouble() < GeneticAlgorithmYinYang.MUTATION_RATE) {
                        // Membalikkan sel antara 1 (W) dan 2 (B)
                        int originalValue = solution[i][j];
                        solution[i][j] = 3 - solution[i][j]; // Membalik antara 1 dan 2 (W dan B)
                        
                        int newFitness = calculateFitness();
                        if (newFitness < fitness) {
                            // Jika fitness menurun, kembalikan perubahan
                            solution[i][j] = originalValue;
                        } 
                        else {
                            fitness = newFitness; // Perbarui fitness jika mutasi menghasilkan nilai fitness yang lebih baik
                        }
                    }
                }
            }
        }
    }

    /**
     * Menghitung nilai fitness dari solusi ini
     * @return Nilai fitness
     */
    private int calculateFitness() {
        int fitness = 0;

        // Evaluasi keseimbangan baris dan kolom
        fitness += evaluateBalance();

        // Evaluasi kelompok yang terlalu besar
        fitness += evaluateGroupBalance();

        // Evaluasi konektivitas antar grup (putih dan hitam)
        fitness += evaluateConnectivity();

        // Penalti untuk subgrup 2x2 yang tidak valid
        fitness += evaluate2x2Groups();

        return fitness; // Mengembalikan nilai fitness total
    }

        /**
     * Mengevaluasi konektivitas antar grup (putih dan hitam)
     * @return Nilai konektivitas
     */
    private int evaluateConnectivity() {
        int connectivityScore = 0;
    
        boolean[][] visited = new boolean[solution.length][solution[0].length];
    
        // Periksa konektivitas grup putih (0)
        int whiteConnectedComponents = countConnectedComponents(0, visited);
        if (whiteConnectedComponents == 1) {
            connectivityScore += 50; // Bonus besar jika semua putih terhubung
        } 
        else {
            connectivityScore -= whiteConnectedComponents * 20; // Penalti untuk setiap grup terpisah
        }
    
        // Reset visited dan periksa konektivitas grup hitam (1)
        visited = new boolean[solution.length][solution[0].length];
        int blackConnectedComponents = countConnectedComponents(1, visited);
        if (blackConnectedComponents == 1) {
            connectivityScore += 50; // Bonus besar jika semua hitam terhubung
        } 
        else {
            connectivityScore -= blackConnectedComponents * 20; // Penalti untuk setiap grup terpisah
        }
    
        return connectivityScore;
    }

    /**
     * Menghitung jumlah komponen terhubung untuk nilai tertentu (0 atau 1)
     * @param value Nilai yang dihitung (0 untuk putih, 1 untuk hitam)
     * @param visited Matriks boolean yang menandai apakah sel telah dikunjungi
     * @return Jumlah komponen terhubung
     */
    private int countConnectedComponents(int value, boolean[][] visited) {
        int components = 0;
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[i].length; j++) {
                if (!visited[i][j] && solution[i][j] == value) {
                    components++;
                    dfs(i, j, value, visited); // Melakukan pencarian kedalaman untuk menandai komponen terhubung
                }
            }
        }
        return components;
    }

    /**
     * Melakukan pencarian kedalaman (DFS) untuk menandai komponen terhubung
     * @param x Koordinat x
     * @param y Koordinat y
     * @param value Nilai yang dicari
     * @param visited Matriks yang menandai posisi yang sudah dikunjungi
     */
    private void dfs(int x, int y, int value, boolean[][] visited) {
        if (x < 0 || x >= solution.length || y < 0 || y >= solution[0].length || visited[x][y] || solution[x][y] != value) {
            return; // Kembali jika di luar batas atau sudah dikunjungi
        }
    
        visited[x][y] = true; // Tandai posisi ini sudah dikunjungi
    
        // Cek sel-sel terhubung (atas, bawah, kiri, kanan)
        dfs(x + 1, y, value, visited);
        dfs(x - 1, y, value, visited);
        dfs(x, y + 1, value, visited);
        dfs(x, y - 1, value, visited);
    }

    // Evaluasi penalti untuk subgrup 2x2 yang tidak valid
    private int evaluate2x2Groups() {
        int penalty = 0;
    
        for (int i = 0; i < solution.length - 1; i++) {
            for (int j = 0; j < solution[i].length - 1; j++) {
                // Cek jika subgrup 2x2 memiliki nilai yang sama
                if (solution[i][j] == solution[i + 1][j] &&
                    solution[i][j] == solution[i][j + 1] &&
                    solution[i][j] == solution[i + 1][j + 1]) {
                    penalty -= 20; // Penalti yang lebih besar
                }
            }
        }
    
        return penalty;
    }

    // Mengevaluasi keseimbangan antara 0 dan 1 pada setiap baris dan kolom
    private int evaluateBalance() {
        int balanceScore = 0;

        // Cek keseimbangan baris
        for (int i = 0; i < solution.length; i++) {
            int countZero = 0;
            int countOne = 0;
            for (int j = 0; j < solution[i].length; j++) {
                if (solution[i][j] == 0) countZero++;
                if (solution[i][j] == 1) countOne++;
            }
            // Menilai seberapa seimbang baris
            if (Math.abs(countZero - countOne) <= 1) {
                balanceScore++; // Menilai baris seimbang
            }
        }

        // Cek keseimbangan kolom
        for (int j = 0; j < solution[0].length; j++) {
            int countZero = 0;
            int countOne = 0;

            for (int i = 0; i < solution.length; i++) {
                if (solution[i][j] == 0) countZero++;
                if (solution[i][j] == 1) countOne++;
            }

            // Menilai seberapa seimbang kolom
            if (Math.abs(countZero - countOne) <= 1) {
                balanceScore++; // Menilai kolom seimbang
            }
        }

        return balanceScore;
    }

    // Mengevaluasi apakah ada kelompok yang terlalu besar
    private int evaluateGroupBalance() {
        int groupScore = 0;

        // Evaluasi kelompok berturut-turut pada baris dan kolom
        // Jika terdapat kelompok lebih dari 3 angka berturut-turut yang sama, maka nilai fitness dikurangi
        for (int i = 0; i < solution.length; i++) {
            int consecutive = 1;
            for (int j = 1; j < solution[i].length; j++) {
                if (solution[i][j] == solution[i][j - 1]) {
                    consecutive++;
                    if (consecutive > 3) groupScore--;
                } 
                else {
                    consecutive = 1;
                }
            }
        }

        for (int j = 0; j < solution[0].length; j++) {
            int consecutive = 1;

            for (int i = 1; i < solution.length; i++) {
                if (solution[i][j] == solution[i - 1][j]) {
                    consecutive++;
                    if (consecutive > 3) groupScore--;
                } 
                else {
                    consecutive = 1;
                }
            }
        }

        return groupScore;
    }

    // Getter untuk solusi (grid)
    public int[][] getSolution() {
        return solution;
    }

    // Getter untuk nilai fitness
    public int getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : solution) {
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
