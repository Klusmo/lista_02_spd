import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ParallelSieveServer {
    private static final int PORT = 512;
    private static final int NUM_THREADS = 4;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor aguardando conexões...");
        for (int i = 0; i < NUM_THREADS; i++) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado: " +
                    clientSocket.getInetAddress().getHostAddress());
            Thread clientThread = new Thread(() -> {
                try {
                    ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
                    ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                    int start = input.readInt();
                    int end = input.readInt();
                    List<Integer> primes = new ArrayList<>();
                    for (int num = start; num <= end; num++) {
                        if (isPrime(num)) {
                            primes.add(num);
                        }
                    }
                    output.writeObject(primes);
                    output.close();
                    input.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            clientThread.start();
        }
        serverSocket.close();
    }

    private static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}