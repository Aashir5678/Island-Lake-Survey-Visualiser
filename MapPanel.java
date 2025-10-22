
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.awt.Color;

class MapPanel extends JPanel implements Runnable {
    private int[][] grid;
    private final int DEFAULT_ROWS = 20;
    private final int DEFAULT_COLS = 20;
    private int rows;
    private int cols;
    private Tile[][] tiles;
    private IslandLakeSurvey survey;

    
    private final int screenWidth = 800;
    private final int screenHeight = 800;
    private final int FPS = 120;
    private final Color WHITE = new Color(255, 255, 255);
    private final Color GREEN = new Color(0, 155, 0);
    private final Color BLACK = new Color(0, 0, 0);
    private final Color BLUE = new Color(0, 0, 155);
    
    private int tileWidth;
    private int tileHeight;
    private boolean simulating = false;

    KeyHandler keyhandler = new KeyHandler();
    MouseHandler mouseHandler = new MouseHandler();

    public MapPanel() throws Exception {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyhandler);
        this.addMouseListener(mouseHandler);
        this.setFocusable(true);

        this.rows = DEFAULT_ROWS;
        this.cols = DEFAULT_COLS;

        

        tiles = new Tile[this.rows][this.cols];
        grid = generateRandomGrid();
        

        tileWidth = screenWidth / this.cols;
        tileHeight = screenHeight / this.rows;
        
        
        for (int i = 0; i < this.rows; i++) {
            for (int j=0; j < this.cols; j++) {
                if (grid[i][j] == 1) {
                    tiles[i][j] = new Tile(j * tileWidth, i * tileHeight, tileWidth, tileHeight, BLACK);
                }

                else {
                    tiles[i][j] = new Tile(j * tileWidth, i * tileHeight, tileWidth, tileHeight, null);
                }
            }
        }

        this.survey = new IslandLakeSurvey(tiles);

        
    }

    public IslandLakeSurvey getSurvey() {
        return this.survey;
    }

    public void reset() {
        grid = generateRandomGrid();
        this.survey.reset();

        for (int i = 0; i < grid.length; i++) {
            for (int j=0; j < grid[0].length; j++) {
                System.out.print(grid[i][j] + " ");
                if (grid[i][j] == 1) {
                    tiles[i][j] = new Tile(j * tileWidth, i * tileHeight, tileWidth, tileHeight, BLACK);
                }

                else {
                    tiles[i][j] = new Tile(j * tileWidth, i * tileHeight, tileWidth, tileHeight, null);
                }
            }
            System.out.println();
        }

        this.survey.setTiles(tiles);
        

        // this.survey = new IslandLakeSurvey(tiles);
    }

    public void runSimulation() {
        this.simulating = true;

        this.survey.populateClusterArray(grid);
        

        Timer t = new Timer(1000, e -> {
            this.survey.run();
            this.simulating = false;
        });

        t.setRepeats(false);
        t.start();

        // simulating = false;

        
    }


    @Override
    public void run() {
        boolean running = true;
        long currentTime;
        int elapsedTime;
        Timer timer;
        
        


        while (running) {
            currentTime = (System.currentTimeMillis());
            update();
            repaint();

            elapsedTime = (int) (System.currentTimeMillis() - currentTime);

            if (elapsedTime == 0) {
                continue;
            }

            if ((1000/FPS) - elapsedTime > 0) {
                // timer = new Timer((1000/FPS) - elapsedTime,  e -> {

                // });
                // timer.setRepeats(false);
                // System.out.println("waiting " + ((1000 / FPS) - elapsedTime) + " mss");
                // System.out.println("waiting " + (1000/FPS) - elapsedTime + " seconds");
                // System.out.println("in time");
                // timer.start();

                try {
                    Thread.sleep((1000 / FPS) - elapsedTime);
                }

                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }

            else {
                // System.out.println("running behind by seconds: " + (elapsedTime / 1000));
            }


            
        }

    }

    public int[][] generateRandomGrid() {
        RandomGenerator gen = new Random();
        int[][] randGrid = new int[DEFAULT_ROWS][DEFAULT_COLS];

        for (int i=0; i < DEFAULT_ROWS; i++) {
            for (int j=0; j < DEFAULT_COLS; j++) {
                
                randGrid[i][j] = gen.nextInt(0, 2);
                System.out.print(randGrid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        

        return randGrid;
        
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int i=0; i < this.rows; i++) {
            for (int j=0; j< this.cols; j++) {
                tiles[i][j].draw(g2d);
            }
        }

        Timer t = new Timer(500, e -> {

        });

        t.setRepeats(false);
        t.start();

    }

    public void update() {
        if (keyhandler.space_presssed && !keyhandler.m_pressed && !simulating) {
            runSimulation();
        }

        else if (keyhandler.m_pressed && !keyhandler.space_presssed && !simulating) {
            reset();
        }

    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}