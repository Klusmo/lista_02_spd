import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MatrixMultiplicationService extends UnicastRemoteObject implements MatrixMultiplicationInterface {

    protected MatrixMultiplicationService() throws RemoteException {
        super();
    }

    @Override
    public int[][] multiplyMatrices(int[][] firstMatrix, int[][] secondMatrix) throws RemoteException {
        int r1 = firstMatrix.length; // rows in first matrix
        int c1 = firstMatrix[0].length; // columns in first matrix (rows in second matrix)
        int c2 = secondMatrix[0].length; // columns in second matrix 
        int[][] product = new int[r1][c2];

        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                for (int k = 0; k < c1; k++) {
                    product[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }

        return product;
    }
}
