import java.io.*;
import java.util.*;

/**
 * Representation of the Yin-Yang puzzle
 */
public class YinYangPuzzle {
    private final int[][] grid;
    private final int size;

    public YinYangPuzzle(int[][] grid) {
        this.grid = grid;
        this.size = grid.length;
    }

    public int getSize() {
        return size;
    }

    public int[][] getGrid() {
        return grid;
    }

    public static YinYangPuzzle loadFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<int[]> gridList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                gridList.add(Arrays.stream(line.split(" "))
                        .mapToInt(Integer::parseInt)
                        .toArray());
            }
            int[][] grid = gridList.toArray(new int[0][]);
            return new YinYangPuzzle(grid);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : grid) {
            for (int cell : row) {
                sb.append(cell).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
