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
        if (this.mapPanel.getSurvey() == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        g2d.drawString("Islands: " + this.mapPanel.getSurvey().BP.numberOfClusters(), 0, 0);

    }


    public void update() {

    }
}
