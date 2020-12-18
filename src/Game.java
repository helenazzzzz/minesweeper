import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Game implements Runnable {

    BufferedReader br;

    public void run() {
        /**
         * Creates frame
         */
        JFrame frame = new JFrame("Minesweeper");

        /**
         * Creates and adds title up top
         */
        JLabel gameTitle = new JLabel("Minesweeper");
        gameTitle.setHorizontalAlignment(JLabel.CENTER);
        gameTitle.setFont(new Font("arial", Font.PLAIN, 36));
        frame.add(gameTitle, BorderLayout.PAGE_START);

        /**
         * Creates and adds time and mine label
         */
        JLabel timeLabel = new JLabel("");
        JLabel mineLabel = new JLabel("");

        timeLabel.setVisible(false);
        mineLabel.setVisible(false);

        frame.add(timeLabel);
        frame.add(mineLabel);

        GameBoard game = new GameBoard(gameTitle, timeLabel, mineLabel);
        /**
         * Creates dimensions and mine sliders
         */
        JPanel sliderLabels = new JPanel();
        sliderLabels.setPreferredSize(new Dimension(1280, 720));

        JLabel widthSliderLabel = new JLabel("width: 10");
        JLabel heightSliderLabel = new JLabel("height: 10");
        JLabel mineSliderLabel = new JLabel("# of mines: 10");

        widthSliderLabel.setFont(new Font("arial", Font.PLAIN, 24));
        heightSliderLabel.setFont(new Font("arial", Font.PLAIN, 24));
        mineSliderLabel.setFont(new Font("arial", Font.PLAIN, 24));

        JSlider widthSlider = new JSlider(5, 40, 10);
        JSlider heightSlider = new JSlider(5, 20, 10);
        JSlider mineSlider = new JSlider(1, 99, 10);

        JPanel widthPanel = new JPanel();
        widthPanel.add(widthSliderLabel);
        widthPanel.add(widthSlider);

        JPanel heightPanel = new JPanel();
        heightPanel.add(heightSliderLabel);
        heightPanel.add(heightSlider);

        JPanel minePanel = new JPanel();
        minePanel.add(mineSliderLabel);
        minePanel.add(mineSlider);

        sliderLabels.add(widthPanel);
        sliderLabels.add(heightPanel);
        sliderLabels.add(minePanel);

        JButton start = new JButton("Start");
        start.setFont(new Font("arial", Font.PLAIN, 36));
        sliderLabels.add(start);

        JLabel instructions = new JLabel();
        ImageIcon instructionsIcon = new ImageIcon("files/instructions.png");
        instructions.setIcon(instructionsIcon);
        sliderLabels.add(instructions);

        JLabel leaderboardLabel = new JLabel();
        sliderLabels.add(leaderboardLabel);
        leaderboardLabel.setFont(new Font("arial", Font.PLAIN, 24));
        leaderboardLabel.setVisible(false);

        frame.add(sliderLabels);

        /**
         * Creates bottom panel with buttons
         */
        JPanel bottomPanel = new JPanel();

        JButton back = new JButton("Back");
        back.setVisible(false);

        JButton highscores = new JButton("Highscores");

        JPanel difficulty = new JPanel();
        JButton easy = new JButton("Easy");
        JButton intermediate = new JButton("Intermediate");
        JButton hard = new JButton("Hard");

        difficulty.add(easy);
        difficulty.add(intermediate);
        difficulty.add(hard);

        bottomPanel.add(back, BorderLayout.LINE_START);
        bottomPanel.add(difficulty);
        bottomPanel.add(highscores, BorderLayout.LINE_END);

        frame.add(bottomPanel, BorderLayout.PAGE_END);

        easy.setFont(new Font("arial", Font.PLAIN, 36));
        intermediate.setFont(new Font("arial", Font.PLAIN, 36));
        hard.setFont(new Font("arial", Font.PLAIN, 36));
        back.setFont(new Font("arial", Font.PLAIN, 36));
        highscores.setFont(new Font("arial", Font.PLAIN, 36));

        /**
         * Button action listeners
         */
        easy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                widthSlider.setValue(10);
                heightSlider.setValue(10);
                mineSlider.setValue(10);
            }
        });

        intermediate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                widthSlider.setValue(16);
                heightSlider.setValue(16);
                mineSlider.setValue(40);
            }
        });

        hard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                widthSlider.setValue(30);
                heightSlider.setValue(16);
                mineSlider.setValue(99);
            }
        });

        widthSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                mineSlider.setMaximum(widthSlider.getValue() * heightSlider.getValue() / 2);
                widthSliderLabel.setText("width: " + widthSlider.getValue());
            }
        });

        heightSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                mineSlider.setMaximum(widthSlider.getValue() * heightSlider.getValue() / 2);
                heightSliderLabel.setText("height: " + heightSlider.getValue());
            }
        });

        mineSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                mineSliderLabel.setText("# of mines: " + mineSlider.getValue());
            }
        });

        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sliderLabels.setVisible(false);
                back.setVisible(true);
                difficulty.setVisible(false);
                highscores.setVisible(false);
                frame.add(game, BorderLayout.CENTER);
                game.reset(widthSlider.getValue(), heightSlider.getValue(), mineSlider.getValue());
            }
        });

        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sliderLabels.setVisible(true);
                back.setVisible(false);
                leaderboardLabel.setVisible(false);
                difficulty.setVisible(true);
                highscores.setVisible(true);
                timeLabel.setVisible(false);
                mineLabel.setVisible(false);
                widthPanel.setVisible(true);
                heightPanel.setVisible(true);
                minePanel.setVisible(true);
                start.setVisible(true);
                instructions.setVisible(true);
                gameTitle.setText("Minesweeper");
            }
        });

        highscores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String leaderboard = generateLeaderboard();
                gameTitle.setText("Leaderboard");
                leaderboardLabel.setText(leaderboard);
                leaderboardLabel.setVisible(true);
                difficulty.setVisible(false);
                highscores.setVisible(false);
                instructions.setVisible(false);
                widthPanel.setVisible(false);
                heightPanel.setVisible(false);
                minePanel.setVisible(false);
                start.setVisible(false);
                back.setVisible(true);
            }
        });

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    public String generateLeaderboard() {
        String entry = "";
        Map<Integer, Integer> entryScoreMap = new TreeMap<Integer, Integer>();
        Map<Integer, String> entryNameMap = new TreeMap<Integer, String>();
        try {
            br = new BufferedReader(new FileReader("files/highscores.txt"));
            entry = br.readLine();
        } catch (IOException e) {
            System.out.println("An error has occurred: " + e.getMessage());
        }
        int numberOfEntries = 1;
        while (entry != null) {
            String[] entryInfo = entry.split(",");
            int entryScore = Integer.parseInt(entryInfo[1]);
            entryScoreMap.put(numberOfEntries, entryScore);
            entryNameMap.put(numberOfEntries, entryInfo[0]);
            numberOfEntries++;
            try {
                entry = br.readLine();
            } catch (IOException e) {

            }
        }
        entryScoreMap = sortByValue(entryScoreMap);
        String leaderboard = "<html><br><br>";
        numberOfEntries = 0;
        for (Map.Entry<Integer, Integer> e : entryScoreMap.entrySet()) {
            if (numberOfEntries < 10) {
                String leaderboardEntry = Integer.toString(numberOfEntries + 1) + "." 
                        + entryNameMap.get(e.getKey()) + " " + Integer.toString(e.getValue()) 
                        + "<br><br>";
                leaderboard += leaderboardEntry;
                numberOfEntries++;
            } else {
                break;
            }
        }
        return leaderboard;
    }

    public static <K> Map<K, Integer> sortByValue(Map<K, Integer> map) {
        List<Map.Entry<K, Integer>> entryList = new ArrayList<Map.Entry<K, Integer>>(
                map.entrySet());
        entryList.sort(Map.Entry.comparingByValue(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        }));

        Map<K, Integer> sortedMap = new LinkedHashMap<K, Integer>();
        for (Map.Entry<K, Integer> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
