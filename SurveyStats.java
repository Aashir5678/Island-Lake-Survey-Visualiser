import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SurveyStats extends JPanel {
    private MapPanel mapPanel;
    private final Color BLACK = new Color(0, 0, 0);
    private final Color RED = new Color(255, 0, 0);
    // private JLabel islandsText = new JLabel("Islands: 0");
    private final Color WHITE = new Color(255, 255, 255);
    public SurveyStats(MapPanel mapPanel) {
        this.setPreferredSize(new Dimension(800, 50));
        this.setBackground(WHITE);
        this.mapPanel = mapPanel;
    }

    public void setSurvey(MapPanel mapPanel) {
        this.mapPanel = mapPanel;
    }

    public void paintComponent(Graphics g) {
        // System.out.println("repainting");
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(RED);
        if (this.mapPanel == null || this.mapPanel.getSurvey() == null) {
            g2d.setFont(new Font("Times New Roman", Font.PLAIN, 50));
            g2d.drawString("Survey", 0, 50);
            
            // System.out.println("should draw");
        }
        
        else {
            // System.out.println(this.mapPanel.getSurvey().BP.numberOfClusters());
            g2d.setFont(new Font("Times New Roman", Font.PLAIN, 50));
            g2d.drawString("Tiles Traversed: " + Tile.tilesTraversed, 0, 50);
        }

    }


    public void update() {

    }
}
