import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MatrixMultiplicationClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 8888);
            MatrixMultiplicationInterface service = (MatrixMultiplicationInterface)
            registry.lookup("MatrixMultiplicationService");

            Scanner scanner = new Scanner(System.in);
            
            int rowsA = getMatrixDimensionFromUser(scanner, "Matrix A", "linhas");
            int colsA = getMatrixDimensionFromUser(scanner, "Matrix A", "colunas");
            int rowsB = getMatrixDimensionFromUser(scanner, "Matrix B", "linhas");
            int colsB = getMatrixDimensionFromUser(scanner, "Matrix B", "colunas");
            if (colsA != rowsB) {
                throw new IllegalArgumentException("As matrizes não são compatíveis para multiplicação");
            }
            int[][] matrixA = getMatrixFromUser(scanner, "Matrix A", rowsA, colsA);
            int[][] matrixB = getMatrixFromUser(scanner, "Matrix B", rowsB, colsB);
            int[][] result = service.multiplyMatrices(matrixA, matrixB);
            System.out.println("Resultado da multiplicação:");
            printMatrix(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getMatrixDimensionFromUser(Scanner scanner, String name, String dimension) {
        System.out.print("Digite o número de " + dimension + " da " + name + ": ");
        return scanner.nextInt();
    }

    private static int[][] getMatrixFromUser(Scanner scanner, String name, int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        System.out.println("Digite os elementos da " + name + ":");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print("Digite o elemento [" + i + "][" + j + "]: ");
                matrix[i][j] = scanner.nextInt();
            }
        }
        
        return matrix;
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }
}
