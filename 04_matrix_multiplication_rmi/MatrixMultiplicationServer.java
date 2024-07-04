import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MatrixMultiplicationServer {
    public static void main(String[] args) {
        try {
            MatrixMultiplicationService service = new MatrixMultiplicationService();
            Registry registry = LocateRegistry.createRegistry(8888);
            registry.bind("MatrixMultiplicationService", service);
            System.out.println("Matrix Multiplication Service is running");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
