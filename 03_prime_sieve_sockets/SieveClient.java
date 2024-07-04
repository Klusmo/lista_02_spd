import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class SieveClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 512;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            // Send start and end range to the server
            int start = 1; // Example start range
            int end = 1000; // Example end range
            output.writeInt(start);
            output.writeInt(end);
            output.flush();

            // Receive and print the list of prime numbers from the server
            List<Integer> primes = List.class.cast(input.readObject());
            System.out.println("Prime numbers between " + start + " and " + end + ": " + primes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}