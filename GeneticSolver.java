/**
 * @author Vincent Emmanuel Suwardy / 6182201067
 */

import java.util.*;

 /**
  * Solver algoritma genetika untuk puzzle Yin-Yang
  */
public class GeneticSolver {
    private final YinYangPuzzle puzzle; // Menyimpan puzzle Yin-Yang yang akan diselesaikan

    /**
     * Konstruktor untuk menginisialisasi solver dengan puzzle Yin-Yang
    * @param puzzle Puzzle Yin-Yang yang akan diselesaikan
    */
    public GeneticSolver(YinYangPuzzle puzzle) {
        this.puzzle = puzzle;
    }

    /**
     * Metode untuk menyelesaikan puzzle menggunakan algoritma genetika
    * @return Hasil dari algoritma genetika, berupa solusi dan jumlah generasi
    */
    public GeneticResult solve() {
        List<Chromosome> population = initializePopulation(); // Inisialisasi populasi awal
        int generation = 0;

        // Proses evolusi sampai jumlah generasi mencapai maksimum
        while (generation < GlobalVariable.MAX_GENERATIONS) {
            population = evolve(population); // Evolusi populasi
            Chromosome best = getBestChromosome(population); // Ambil kromosom terbaik dari populasi

            // Jika fitness terbaik adalah 0, solusi telah ditemukan
            if (best.getFitness() == 0) {
                return new GeneticResult(best, generation); // Mengembalikan hasil dengan solusi yang ditemukan
            }

            generation++; // Meningkatkan jumlah generasi
        }

        // Jika solusi tidak ditemukan dalam batas generasi, kembalikan solusi terbaik yang ditemukan
        return new GeneticResult(getBestChromosome(population), generation);
    }

    /**
     * Inisialisasi populasi pertama untuk algoritma genetika
    * @return Daftar kromosom yang mewakili populasi awal
    */
    private List<Chromosome> initializePopulation() {
        List<Chromosome> population = new ArrayList<>();

        // Membuat kromosom baru untuk populasi
        while (population.size() < GlobalVariable.POPULATION_SIZE) {
            population.add(new Chromosome(puzzle.getSize(), puzzle)); // Membuat kromosom dengan ukuran puzzle dan puzzle itu sendiri
        }
        return population;
    }

    /**
     * Proses evolusi untuk mengubah populasi menjadi generasi berikutnya
    * @param population Populasi saat ini
    * @return Populasi baru setelah evolusi
    */
    private List<Chromosome> evolve(List<Chromosome> population) {
        List<Chromosome> newPopulation = new ArrayList<>();

        // Proses crossover dan mutasi untuk setiap individu
        while (newPopulation.size() < population.size()) {
            Chromosome parent1 = selectParent(population);
            Chromosome parent2 = selectParent(population);

            // Jika hanya ada satu individu, crossover dengan dirinya sendiri
            if (population.size() == 1) {
                parent2 = parent1;
            }

            Chromosome offspring = parent1.crossover(parent2); // Lakukan crossover
            offspring.mutate(); // Lakukan mutasi
            newPopulation.add(offspring); // Tambahkan keturunan ke populasi baru
        }
        return newPopulation; // Kembalikan populasi baru
    }

    /**
     * Memilih orang tua dari populasi menggunakan seleksi turnamen
    * @param population Populasi yang ada
    * @return Orang tua yang dipilih
    */
    private Chromosome selectParent(List<Chromosome> population) {
        Chromosome best = null;

        // Melakukan seleksi turnamen dengan 5 kandidat acak
        for (int i = 0; i < 5; i++) {
            Chromosome candidate = population.get(GlobalVariable.RANDOM.nextInt(population.size()));
            // Memilih kandidat dengan fitness terbaik
            if (best == null || candidate.getFitness() < best.getFitness()) {
                best = candidate;
            }
        }
        return best; // Mengembalikan orang tua dengan fitness terbaik
    }

    /**
     * Mengambil kromosom terbaik dalam populasi berdasarkan nilai fitness
    * @param population Populasi yang ada
    * @return Kromosom dengan fitness terbaik (terendah)
    */
    private Chromosome getBestChromosome(List<Chromosome> population) {
        return population.stream().min(Comparator.comparingInt(Chromosome::getFitness)).orElseThrow();
    }
}
 