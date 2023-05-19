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

public class RMI_GUI extends JFrame { // a class that is a frame window that can be used in GUI apps
    private JTextArea outputTxt;

    // used the javax.swing package, it is for creating and managing GUI stuff life windows, buttons.. etc
    public RMI_GUI() {
        // set window title and exit operation
        setTitle("RMI Client GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // set layout

        // create jtextarea object and set it uneditable
        outputTxt = new JTextArea();
        outputTxt.setEditable(false);

        // create jscrollpane object and add jtextarea to it
        JScrollPane scrollPane = new JScrollPane(outputTxt);
        add(scrollPane, BorderLayout.CENTER);

        // create the button for calculation
        JButton calcButton = new JButton("Calculate");
        calcButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateBtn();
            }
        });

        // create button to clear window
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearWindow();
            }
        });

        // create panel to have the buttons inside it
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calcButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH); // set panel location

        // adjusting the size of window
        setPreferredSize(new Dimension(500, 400));
        pack();

        // center the window in the screen
        setLocationRelativeTo(null);
    }

    private void calculateBtn() {
        try {
            // gets the reference of RMI registry at port 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            // searches for a remote object under name "calculation"
            process_txt computeAll = (process_txt) registry.lookup("calculation");

            List<String> words = new ArrayList<>();
            // reading the file
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

            // concatenate the words into one string
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

            // wait for each thread to finish to calculate the finish time
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

            long time = finished - started; // total time

            printTxt("Program execution time: " + time + " milliseconds");
            printTxt("____________________________________");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // clears the windows of the GUI
    private void clearWindow() {
        outputTxt.setText("");
    }

    // prints the text into the GUI window
    private synchronized void printTxt(String text) {
        outputTxt.append(text + "\n");
    }

    public static void main(String[] args) {
        // executing GUI setup on event thread, ensures the setup is executed
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // create instance of the GUI and set it to be visible
                RMI_GUI gui = new RMI_GUI();
                gui.setVisible(true);
            }
        });
    }
}
