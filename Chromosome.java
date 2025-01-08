/**
 * @author Vincent Emmanuel Suwardy / 6182201067
 */
import java.util.*;

/**
 * Representation of a solution candidate (chromosome)
 */
public class Chromosome {
    private final int[][] solution;
    private final int fitness;

    public Chromosome(int size) {
        this.solution = generateRandomSolution(size);
        this.fitness = calculateFitness();
    }

    private int[][] generateRandomSolution(int size) {
        int[][] solution = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                solution[i][j] = GeneticAlgorithmYinYang.RANDOM.nextInt(2); // 0 or 1
            }
        }
        return solution;
    }

    public Chromosome(int[][] solution) {
        this.solution = solution;
        this.fitness = calculateFitness();
    }

    public Chromosome crossover(Chromosome other) {
        int size = solution.length;
        int[][] offspringSolution = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                offspringSolution[i][j] = GeneticAlgorithmYinYang.RANDOM.nextDouble() < 0.5 ? solution[i][j] : other.solution[i][j];
            }
        }
        return new Chromosome(offspringSolution);
    }

    public void mutate() {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[i].length; j++) {
                if (GeneticAlgorithmYinYang.RANDOM.nextDouble() < GeneticAlgorithmYinYang.MUTATION_RATE) {
                    solution[i][j] = 1 - solution[i][j]; // Flip 0 to 1 or 1 to 0
                }
            }
        }
    }

    // Tambahkan evaluasi konektivitas dan larangan grup 2x2 ke dalam fitness function
    private int calculateFitness() {
        int fitness = 0;

        // Evaluasi keseimbangan baris dan kolom (dari fungsi yang ada)
        fitness += evaluateBalance();

        // Evaluasi bahwa tidak ada kelompok berturut-turut yang terlalu besar
        fitness += evaluateGroupBalance();

        // Tambahkan evaluasi konektivitas
        fitness += evaluateConnectivity();

        // Tambahkan penalti untuk subgrup 2x2 yang tidak valid
        fitness += evaluate2x2Groups();

        return fitness;
    }

    // Mengevaluasi konektivitas antar grup putih dan hitam
    private int evaluateConnectivity() {
        int connectivityScore = 0;

        boolean[][] visited = new boolean[solution.length][solution[0].length];

        // Periksa konektivitas grup putih (0)
        int whiteConnectedComponents = countConnectedComponents(0, visited);
        if (whiteConnectedComponents == 1) {
            connectivityScore += 10; // Bonus jika semua putih terhubung
        } else {
            connectivityScore -= whiteConnectedComponents*10; // Penalti untuk setiap grup terpisah
        }

        // Reset visited dan periksa konektivitas grup hitam (1)
        visited = new boolean[solution.length][solution[0].length];
        int blackConnectedComponents = countConnectedComponents(1, visited);
        if (blackConnectedComponents == 1) {
            connectivityScore += 10; // Bonus jika semua hitam terhubung
        } else {
            connectivityScore -= blackConnectedComponents; // Penalti untuk setiap grup terpisah
        }

        return connectivityScore;
    }

    // Hitung jumlah komponen terhubung untuk nilai tertentu (0 atau 1)
    private int countConnectedComponents(int value, boolean[][] visited) {
        int components = 0;
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[i].length; j++) {
                if (!visited[i][j] && solution[i][j] == value) {
                    components++;
                    dfs(i, j, value, visited);
                }
            }
        }
        return components;
    }

    // Depth-First Search untuk menandai komponen terhubung
    private void dfs(int x, int y, int value, boolean[][] visited) {
        if (x < 0 || x >= solution.length || y < 0 || y >= solution[0].length || visited[x][y] || solution[x][y] != value) {
            return;
        }

        visited[x][y] = true;

        // Periksa empat arah (atas, bawah, kiri, kanan)
        dfs(x - 1, y, value, visited);
        dfs(x + 1, y, value, visited);
        dfs(x, y - 1, value, visited);
        dfs(x, y + 1, value, visited);
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
                    penalty -= 5; // Penalti untuk subgrup 2x2 yang tidak valid
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
                } else {
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
                } else {
                    consecutive = 1;
                }
            }
        }

        return groupScore;
    }

    // Menyediakan solusi (grid)
    public int[][] getSolution() {
        return solution;
    }

    public int getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : solution) {
            for (int cell : row) {
                sb.append(cell).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
