import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        MapPanel map = null;

        try {
            map = new MapPanel();
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        SurveyStats stats = new SurveyStats(map);


        JFrame window = new JFrame();

        // window.setLayout(new );
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setSize(new Dimension(map.getScreenWidth(), map.getScreenHeight()));
        
        window.setBackground(new Color(255, 255, 255));

        
        window.add(map);
        // window.add(stats);
        window.pack();

        window.setTitle("Survey");
        window.setVisible(true);
        
        map.run();



    }
}
