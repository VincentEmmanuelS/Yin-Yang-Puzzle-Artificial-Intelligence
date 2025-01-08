/**
 * @author Vincent Emmanuel Suwardy / 6182201067
 */

import java.util.*;

/**
 * Genetic algorithm solver
 */
class GeneticSolver {
    private final YinYangPuzzle puzzle;

    public GeneticSolver(YinYangPuzzle puzzle) {
        this.puzzle = puzzle;
    }

    public GeneticResult solve() {
        List<Chromosome> population = initializePopulation();
        int generation = 0;

        while (generation < GeneticAlgorithmYinYang.MAX_GENERATIONS) {
            population = evolve(population);
            Chromosome best = getBestChromosome(population);

            if (best.getFitness() == 0) { // Solution found
                return new GeneticResult(best, generation);
            }

            generation++;
        }

        return new GeneticResult(getBestChromosome(population), generation);
    }

    private List<Chromosome> initializePopulation() {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < GeneticAlgorithmYinYang.POPULATION_SIZE; i++) {
            population.add(new Chromosome(puzzle.getSize()));
        }
        return population;
    }

    private List<Chromosome> evolve(List<Chromosome> population) {
        List<Chromosome> newPopulation = new ArrayList<>();
        for (int i = 0; i < GeneticAlgorithmYinYang.POPULATION_SIZE; i++) {
            Chromosome parent1 = selectParent(population);
            Chromosome parent2 = selectParent(population);
            Chromosome offspring = parent1.crossover(parent2);
            offspring.mutate();
            newPopulation.add(offspring);
        }
        return newPopulation;
    }

    private Chromosome selectParent(List<Chromosome> population) {
        Chromosome best = null;
        for (int i = 0; i < 5; i++) {
            Chromosome candidate = population.get(GeneticAlgorithmYinYang.RANDOM.nextInt(GeneticAlgorithmYinYang.POPULATION_SIZE));
            if (best == null || candidate.getFitness() < best.getFitness()) {
                best = candidate;
            }
        }
        return best;
    }

    private Chromosome getBestChromosome(List<Chromosome> population) {
        return population.stream().min(Comparator.comparingInt(Chromosome::getFitness)).orElseThrow();
    }
}
