import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Grid {
    private int pixelLocationX;
    private int pixelLocationY;

    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;

    private boolean revealed;
    private boolean flagged;

    private static BufferedImage flag;

    public Grid(int px, int py, int row, int column) {
        pixelLocationX = px + WIDTH * column;
        pixelLocationY = py + WIDTH * row;

        revealed = false;
        flagged = false;

        if (flag == null) {
            try {
                flag = ImageIO.read(new File("files/flag.png"));
            } catch (IOException e) {
                System.out.println("Internal Error:" + e.getMessage());
            }
        }
    }

    public int mark(int minesRemaining) {
        if (!revealed) {
            if (flagged) {
                minesRemaining++;
            } else {
                minesRemaining--;
            }
            flagged = !flagged;

        }
        return minesRemaining;
    }

    public boolean getRevealed() {
        return revealed;
    }

    public boolean getFlagged() {
        return flagged;
    }

    public int getPixelLocationX() {
        return pixelLocationX;
    }

    public int getPixelLocationY() {
        return pixelLocationY;
    }

    public void setRevealed() {
        revealed = true;
    }
    
    public int getWidth() {
        return WIDTH;
    }
    
    public int getHeight() {
        return HEIGHT;
    }

    public BufferedImage getFlag() {
        return flag;
    }
    
    public abstract int reveal(boolean revealSurrounding);

    public abstract void draw(Graphics g);
}
