import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MineGrid extends Grid {

    private static BufferedImage mine;

    public MineGrid(int pixelLocationX, int pixelLocationY, int row, int col) {
        super(pixelLocationX, pixelLocationY, row, col);
        if (mine == null) {
            try {
                mine = ImageIO.read(new File("files/mine.png"));
            } catch (IOException e) {
                System.out.println("Internal Error:" + e.getMessage());
            }
        }
    }

    public int reveal(boolean revealSurrounding) {
        if (!getFlagged()) {
            setRevealed();
            return -1;
        }
        return 0;
    }

    public void draw(Graphics g) {
        int px = this.getPixelLocationX();
        int py = this.getPixelLocationY();
        if (this.getRevealed()) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(px, py, getWidth(), getHeight());
            g.drawImage(mine, px, py, getWidth(), getHeight(), null);
        } else {
            if (this.getFlagged()) {
                g.drawImage(getFlag(), px, py, getWidth(), getHeight(), null);
            } else {
                g.setColor(Color.BLACK);
                g.drawRect(px, py, getWidth(), getHeight());
            }
        }
    }

}
