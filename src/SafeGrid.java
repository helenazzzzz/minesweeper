import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class SafeGrid extends Grid {

    private int surrounding;
    private static Map<Integer, BufferedImage> numberImages;

    public SafeGrid(int pixelLocationX, int pixelLocationY, int row, int col, int surrounding) {
        super(pixelLocationX, pixelLocationY, row, col);
        this.surrounding = surrounding;
        if (numberImages == null) {
            numberImages = new HashMap<Integer, BufferedImage>();
            for (int i = 0; i < 8; i++) {
                try {
                    numberImages.put(i + 1, ImageIO.read(new File("files/" + (i + 1) + ".png")));
                } catch (IOException e) {
                    System.out.println("Internal Error:" + e.getMessage());
                }
            }
        }
    }

    public int reveal(boolean revealSurrounding) {
        int toReturn = 0;
        if (!this.getFlagged() && (!this.getRevealed() || revealSurrounding)) {
            if (!this.getRevealed()) {
                this.setRevealed();
                toReturn++;
            }
            if (revealSurrounding || surrounding == 0) {
                toReturn += 2;
            }
        }
        return toReturn;
    }

    public int getSurrounding() {
        return surrounding;
    }

    public void draw(Graphics g) {
        int px = this.getPixelLocationX();
        int py = this.getPixelLocationY();
        if (this.getRevealed()) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(px, py, getWidth(), getHeight());
            if (surrounding > 0) {
                g.drawImage(numberImages.get(surrounding), px, py, getWidth(), getHeight(), null);
            }
            g.setColor(Color.BLACK);
            g.drawRect(px, py, getWidth(), getHeight());
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
