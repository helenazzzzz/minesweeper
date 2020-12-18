import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import java.util.Random;

public class GameBoard extends JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel status;
    /*
     * Dimensions of the playing field in pixels
     */
    private int pixelWidth;
    private int pixelHeight;

    /*
     * Location of the top left corner of the playing field
     */
    private int pixelTopX;
    private int pixelTopY;

    /*
     * Dimensions of the mine field in grids
     */
    private int boardWidth;
    private int boardHeight;

    /*
     * Arrays used to store grid status
     */
    private boolean[] mineLocation;
    private int[] surroundingMines;
    private Grid[][] minefield;

    /*
     * Game status
     */
    private int mines;
    private int minesRemaining;
    private int safeRemaining;

    /*
     * Game size
     */
    public static final int FRAME_WIDTH = 1280;
    public static final int FRAME_HEIGHT = 720;

    /*
     * Misc. variables for game state
     */
    private Random r;
    private boolean playing = false;
    private Timer timer;
    private int timeElapsed;
    private boolean isRightPressed;

    private JLabel timeLabel;
    private JLabel mineLabel;

    /**
     * Constructor for Gameboard
     */
    public GameBoard(JLabel statusInit, JLabel time, JLabel mine) {
        r = new Random();
        mineLocation = new boolean[0];

        status = statusInit;
        timeLabel = time;
        mineLabel = mine;

        /*
         * Start game timer
         */
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeElapsed++;
            }
        });

        addMouseListener(new MouseAdapter() {
            /*
             * To see if the right mouse button is held
             */
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    isRightPressed = true;
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    isRightPressed = false;
                }
            }

            /*
             * Left click - reveal one grid Left click with right held down - if the grid is
             * surrounded with the right number of flags, reveal surrounding grids Right
             * click - flag
             */
            public void mouseClicked(MouseEvent e) {
                if (playing) {
                    Point p = e.getPoint();
                    int col = (p.x - pixelTopX) / 30;
                    int row = (p.y - pixelTopY) / 30;

                    if (row >= 0 && col >= 0 && col < boardWidth && row < boardHeight) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (isRightPressed) {
                                if (minefield[row][col] instanceof SafeGrid) {
                                    SafeGrid currentGrid = (SafeGrid) minefield[row][col];
                                    if (checkFlagged(row, col, currentGrid.getSurrounding())) {
                                        reveal(row, col, true);
                                    }
                                }
                            } else {
                                reveal(row, col, false);
                            }

                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            minesRemaining = minefield[row][col].mark(minesRemaining);
                        }
                        if (safeRemaining == 0) {
                            gameEnd(true);
                        }
                    }
                    repaint();
                }
            }
        });
    }

    /*
     * Resets the game Also generates mines as well as indicate the mine in
     * surrounding grids
     */
    public void reset(int width, int height, int mines) {
        boardWidth = width;
        boardHeight = height;
        this.mines = mines;
        safeRemaining = boardWidth * boardHeight;
        minesRemaining = 0;

        pixelWidth = boardWidth * 30;
        pixelHeight = boardHeight * 30;

        pixelTopX = (FRAME_WIDTH - pixelWidth) / 2;
        pixelTopY = (FRAME_HEIGHT - pixelHeight - 50) / 2 + 50;

        mineLocation = new boolean[width * height];
        minefield = new Grid[height][width];
        surroundingMines = new int[width * height];
        while (minesRemaining < mines) {
            int location = r.nextInt(width * height);
            if (!mineLocation[location]) {
                mineLocation[location] = true;
                minesRemaining++;
                safeRemaining--;
                addSurrounding(location);
            }

        }

        playing = true;

        timer.start();
        timeElapsed = 0;

        repaint();

        mineLabel.setVisible(true);
        timeLabel.setVisible(true);
    }

    /*
     * Determines whether a mine grid is at a corner or an edge for each neighboring
     * grid, increment the number of mines each neighbor is surrounded by.
     */
    private void addSurrounding(int location) {
        int mineColumn = location % boardWidth;
        int mineRow = location / boardWidth;

        boolean leftEdge = true;
        boolean rightEdge = true;
        boolean topEdge = true;
        boolean bottomEdge = true;

        if (mineColumn > 0) {
            leftEdge = !leftEdge;
            surroundingMines[location - 1]++;
        }
        if (mineColumn < boardWidth - 1) {
            rightEdge = !rightEdge;
            surroundingMines[location + 1]++;
        }
        if (mineRow > 0) {
            topEdge = !topEdge;
            surroundingMines[location - boardWidth]++;
        }
        if (mineRow < boardHeight - 1) {
            bottomEdge = !bottomEdge;
            surroundingMines[location + boardWidth]++;
        }
        if (!leftEdge) {
            if (!topEdge) {
                surroundingMines[location - boardWidth - 1]++;
            }
            if (!bottomEdge) {
                surroundingMines[location + boardWidth - 1]++;
            }
        }
        if (!rightEdge) {
            if (!topEdge) {
                surroundingMines[location - boardWidth + 1]++;
            }
            if (!bottomEdge) {
                surroundingMines[location + boardWidth + 1]++;
            }
        }
    }

    /*
     * Base case: if a grid is already revealed and there's no need to reveal the
     * surrounding grids of a revealed grid Otherwise: reveal the grid and a state
     * is returned -1: the grid is a mine, end the game 0: do nothing as the grid
     * either revealed or flagged 1: a new grid is revealed 2: a new grid is not
     * revealed but will need to revael surrounding grids 3: a new grid is revealed
     * and will need to reveal surrounding grids
     * 
     * this method would then be called on the surrounding grids that will be
     * revealed
     */
    public void reveal(int row, int col, boolean revealSurrounding) {
        Grid currentGrid = minefield[row][col];
        if (!revealSurrounding && currentGrid.getRevealed()) {
            return;
        }
        int state = currentGrid.reveal(revealSurrounding);
        if (state == -1) {
            gameEnd(false);
        } else if (state > 0) {
            if (state % 2 == 1) {
                safeRemaining--;
            }
            if (state >= 2) {
                for (int r = -1; r < 2; r++) {
                    for (int c = -1; c < 2; c++) {
                        int currentRow = row + r;
                        int currentCol = col + c;
                        if (currentRow >= 0 && currentRow < boardHeight && currentCol >= 0 
                                && currentCol < boardWidth && !(currentRow == row 
                                && currentCol == col)) {
                            reveal(row + r, col + c, false);
                        }
                    }
                }
            }

        }
    }

    /*
     * Check the number of flags surrounding a grid
     */
    private boolean checkFlagged(int row, int col, int surrounding) {
        int flags = 0;
        for (int r = -1; r < 2; r++) {
            for (int c = -1; c < 2; c++) {
                int currentRow = row + r;
                int currentCol = col + c;
                if (currentRow >= 0 && currentRow < boardHeight && currentCol >= 0 
                        && currentCol < boardWidth && !(currentRow == row && currentCol == col)) {
                    if (minefield[currentRow][currentCol].getFlagged()) {
                        flags++;
                    }
                }
            }
        }
        if (flags == surrounding) {
            return true;
        }
        return false;
    }

    /*
     * game end, the timer is stopped and user is no longer playing if the player
     * wins, prompt the user for their name and writes the name and score onto the
     * leaderboard
     */
    private void gameEnd(boolean win) {
        timer.stop();
        playing = false;
        if (win) {
            status.setText("You win!");
            int score = (int) (Math.pow(boardWidth, 2) * Math.pow(boardHeight, 2) 
                    + Math.pow(mines, 3)) / timeElapsed;
            String nickname = JOptionPane
                    .showInputDialog("Score: " + Integer.toString(score) 
                    + " Please input your nickname: ");
            try {
                BufferedWriter bw = new BufferedWriter(
                        new FileWriter("files/highscores.txt", true));
                bw.append(nickname + "," + Integer.toString(score) + "\n");
                bw.close();
            } catch (IOException e) {
                System.out.println("An error has occurred");
            }
        } else {
            status.setText("You lost.");
        }
    }

    /*
     * draws the playing field and grids if this is the first game played, also
     * creates the grids
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(pixelTopX, pixelTopY, pixelWidth, pixelHeight);
        for (int i = 0; i < mineLocation.length; i++) {
            int mineColumn = i % boardWidth;
            int mineRow = i / boardWidth;

            if (mineLocation[i]) {
                if (minefield[mineRow][mineColumn] == null) {
                    minefield[mineRow][mineColumn] = new MineGrid(pixelTopX, pixelTopY, 
                            mineRow, mineColumn);
                }
                minefield[mineRow][mineColumn].draw(g);
            } else {
                if (minefield[mineRow][mineColumn] == null) {
                    minefield[mineRow][mineColumn] = new SafeGrid(pixelTopX, pixelTopY, 
                            mineRow, mineColumn, surroundingMines[i]);
                }
                minefield[mineRow][mineColumn].draw(g);
            }
        }
        mineLabel.setText(Integer.toString(minesRemaining));
        mineLabel.setBounds(pixelTopX, pixelTopY, 100, 50);
        mineLabel.setFont(new Font("arial", Font.PLAIN, 36));

        timeLabel.setText(Integer.toString(timeElapsed));
        timeLabel.setBounds(pixelTopX + pixelWidth - 50, pixelTopY, 100, 50);
        timeLabel.setFont(new Font("arial", Font.PLAIN, 36));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
    }
}
