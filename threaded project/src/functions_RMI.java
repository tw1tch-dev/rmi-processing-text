import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class functions_RMI extends UnicastRemoteObject implements process_txt {
    protected functions_RMI() throws RemoteException {
        super();
    }
    
    public int Count(String txt) throws RemoteException {
        return txt.length();
    }

    public String longest(String txt) throws RemoteException {
        String[] words = txt.split(" ");
        String longest = "";

        for (String word : words) {
            if (word.length() > longest.length()) {
                longest = word;
            }
        }

        return longest;
    }

    public String shortest(String txt) throws RemoteException {
        String[] words = txt.split(" ");
        String shortest = "honorificabilitudinitatibus";

        for (String word : words) {
            if (word.length() < shortest.length()) {
                shortest = word;
            }
        }

        return shortest;
    }

    public List<String> repeatedwords(String txt) throws RemoteException {
        String[] words = txt.split(" ");
        Set<String> unique = new HashSet<>();
        List<String> repeatedwords = new ArrayList<>();

        for (String word : words) {
            if(!unique.add(word)) {
                if(!repeatedwords.contains(word)) {
                    repeatedwords.add(word);
                }
            }
        }

        return repeatedwords;
    }
    
    public Map<String, Integer> Repeat(String txt) throws RemoteException {
        String[] words = txt.split(" ");
        Map<String, Integer> wordCounts = new HashMap<>();

        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }

        return wordCounts;
    }
}