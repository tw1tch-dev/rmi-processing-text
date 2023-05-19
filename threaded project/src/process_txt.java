import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface process_txt extends Remote {
    int Count(String txt) throws RemoteException;
    List<String> repeatedwords(String txt) throws RemoteException;
    String longest(String txt) throws RemoteException;
    String shortest(String txt) throws RemoteException;
    Map<String, Integer> Repeat(String txt) throws RemoteException;
}
