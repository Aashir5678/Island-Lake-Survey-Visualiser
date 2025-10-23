import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.Timer;

public class Tile {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private final Color BLACK = new Color(0, 0, 0);
    private final int BORDER_SIZE = 1;

    public Tile(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if (color == null) {
            this.color = new Color(255, 255, 255);
        }

        else {
            this.color = color;
        }
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color c) {

        Timer t = new Timer(2500, e -> {
            this.color = c;
        });

        t.setRepeats(false);
        t.start();
        
    }


    @SuppressWarnings("unused")
    public void draw(Graphics2D g) {
        if (BORDER_SIZE <= 0) {
            g.setColor(this.color);
            g.fillRect(x, y, width, height);
        }

        else {
            g.setColor(BLACK);
            g.fillRect(x, y, width, height); // Draw Border

            g.setColor(this.color);
            g.fillRect(x + (BORDER_SIZE / 2), y + (BORDER_SIZE / 2), width - (BORDER_SIZE), height - (BORDER_SIZE));
        }
        
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }    
}
