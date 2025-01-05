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

    // private int calculateFitness() {
    //     // Placeholder fitness function
    //     return GeneticAlgorithmYinYang.RANDOM.nextInt(100); // TODO: Implement actual fitness evaluation
    // }

    private int calculateFitness() {
        int fitness = 0;

        // Periksa keseimbangan pada baris dan kolom
        fitness += evaluateBalance();

        // Periksa bahwa tidak ada kelompok berturut-turut yang terlalu besar
        fitness += evaluateGroupBalance();

        return fitness;
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
