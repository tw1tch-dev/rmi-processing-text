import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RMI_Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            process_txt computeAll = (process_txt) registry.lookup("calculation");

            List<String> words = new ArrayList<>();
            BufferedReader file = new BufferedReader(new FileReader("C:/Users/Administrator/Downloads/threaded project/src/words.txt"));
            String word;
            while ((word = file.readLine()) != null) {
                String[] lines = word.split(" ");
                for (String wordd : lines) {
                    words.add(wordd);
                }
            }
            file.close();
            StringBuffer x = new StringBuffer();

            for(String word1 : words) {
                x.append(word1).append(" ");
            }
            String txt = x.toString();

            // THREADS FOR EACH FUNCTION
            Thread countThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int letterCount = computeAll.Count(txt);
                        System.out.println("Letter count: " + letterCount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            Thread longestThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String longestWord = computeAll.longest(txt);
                        System.out.println("Longest word: " + longestWord);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            Thread shortestThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String shortestWord = computeAll.shortest(txt);
                        System.out.println("Shortest word: " + shortestWord);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            Thread repeatedWordsThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<String> repeatedWords = computeAll.repeatedwords(txt);
                        System.out.println("Repeated words: " + repeatedWords);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            

            Thread wordCountsThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Map<String, Integer> wordCounts = computeAll.Repeat(txt);
                        System.out.println("Repetition of each word: " + wordCounts);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            
            long started = System.currentTimeMillis();

            countThread.start();
            longestThread.start();
            shortestThread.start();
            repeatedWordsThread.start();
            wordCountsThread.start();

            try {
                countThread.join();
                longestThread.join();
                shortestThread.join();
                repeatedWordsThread.join();
                wordCountsThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long finished = System.currentTimeMillis();

            long time = finished - started;

            System.out.println("Program execution time: " + time + " milliseconds");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
