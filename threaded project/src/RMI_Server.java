import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMI_Server {
    public static void main(String[] args) {
        try {
            process_txt computeAll = new functions_RMI();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("calculation", computeAll);

            System.out.println("Server has started running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}