import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RMI_GUI extends JFrame {
    private JTextArea outputTxt;

    public RMI_GUI() {
        setTitle("RMI Client GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        outputTxt = new JTextArea();
        outputTxt.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTxt);
        add(scrollPane, BorderLayout.CENTER);

        JButton analyzeButton = new JButton("Calculate");
        analyzeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateBtn();
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearWindow();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(analyzeButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(500, 400));

        pack();
        setLocationRelativeTo(null);
    }

    private void calculateBtn() {
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
                    int letterCount = 0;
                    try {
                        letterCount = computeAll.Count(txt);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    outputTxt.append("Letter count: " + letterCount + "\n");
                }
            });
            
            Thread longestThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String longestWord = computeAll.longest(txt);
                        printTxt("Longest word: " + longestWord);
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
                        printTxt("Shortest word: " + shortestWord);
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
                        printTxt("Repeated words: " + repeatedWords);
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
                        printTxt("Repetition of each word: " + wordCounts);
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

            printTxt("Program execution time: " + time + " milliseconds");
            printTxt("____________________________________");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearWindow() {
        outputTxt.setText("");
    }

    private synchronized void printTxt(String text) {
        outputTxt.append(text + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RMI_GUI gui = new RMI_GUI();
                gui.setVisible(true);
            }
        });
    }
}
