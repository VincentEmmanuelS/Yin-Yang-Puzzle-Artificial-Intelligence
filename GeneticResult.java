/**
 * Container for the result of the genetic algorithm
 */
public class GeneticResult {
    private final Chromosome solution;
    private final int generations;

    public GeneticResult(Chromosome solution, int generations) {
        this.solution = solution;
        this.generations = generations;
    }

    public Chromosome getSolution() {
        return solution;
    }

    public int getGenerations() {
        return generations;
    }

    public int getFitness() {
        return solution.getFitness();
    }
}
