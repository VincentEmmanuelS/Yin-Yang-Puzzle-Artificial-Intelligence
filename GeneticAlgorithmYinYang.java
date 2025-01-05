import java.io.*;
import java.util.*;

/**
 * Genetic Algorithm for Solving Yin-Yang Puzzle
 */
public class GeneticAlgorithmYinYang {

    // Seed for reproducibility
    private static final long SEED = new Random().nextLong(); // Replace with desired seed value

    // Random number generator with a fixed seed
    public static final Random RANDOM = new Random(SEED);

    // Parameters for the genetic algorithm
    public static final int POPULATION_SIZE = 100;
    public static final int MAX_GENERATIONS = 1000;
    public static final double MUTATION_RATE = 0.1;
    public static final double CROSSOVER_RATE = 0.8;


    public static void main(String[] args) throws IOException {
        // Print the seed used for reproducibility
        System.out.println("Using seed: " + SEED);

        // Input and output files
        String inputFile = "puzzle_input.txt";
        String outputFile = "puzzle_solution.txt";

        // Load puzzle from input file
        YinYangPuzzle puzzle = YinYangPuzzle.loadFromFile(inputFile);

        // Solve the puzzle using a genetic algorithm
        GeneticSolver solver = new GeneticSolver(puzzle);
        GeneticResult result = solver.solve();

        // Save the solution and report to output file
        saveResultToFile(outputFile, result);
    }

    private static void saveResultToFile(String filePath, GeneticResult result) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Solution:\n");
            writer.write(result.getSolution().toString());
            writer.write("\n\nReport:\n");
            writer.write("Seed: " + SEED+"\n");
            writer.write("Generations: " + result.getGenerations() + "\n");
            writer.write("Final Fitness: " + result.getFitness() + "\n");
        }
    }
}
