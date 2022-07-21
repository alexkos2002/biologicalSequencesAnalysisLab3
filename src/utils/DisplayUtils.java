package utils;

public class DisplayUtils {

    public static void printMatrix (int[][] matrix, int n, int m) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.printf("%12d ", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printStringMatrix (String[][] matrix, int n, int m) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.printf("%12s", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

}
