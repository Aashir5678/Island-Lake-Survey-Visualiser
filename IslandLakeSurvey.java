import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.reflect.Array;
import javax.swing.Timer;
import java.nio.channels.InterruptedByTimeoutException;

public class IslandLakeSurvey {
    public static int islandCount;
    public static int lakeCount;

    public Partition<Coordinate> BP;
    public Partition<Coordinate> WP;
    public Node<Cluster<Coordinate>>[][] cluster;
    public Node<Cluster<Coordinate>>[][] lakeCluster;
    public List<Node<Cluster<Coordinate>>> lakes;

    public Set<Coordinate> alreadyPartitionedLake;
    public Set<Coordinate> alreadyPartitioned;
    public int[][] clusterArray;


    public int rows;
    public int cols;
    public int area;
    public int lakeArea;
    public int numCoordinates;

    private Tile[][] tiles;
    private final int DELAY_LAND = 1600; // 1600
    private final Color GREEN = new Color(0, 155, 0);
    private final Color BLUE = new Color(0, 0, 155);
    private final Color RED = new Color(255, 0, 0);
    private final Color DARK_BLUE = new Color(0, 0, 75);
    private final Color DARK_GREEN = new Color(0, 50, 0);


    @SuppressWarnings("unchecked")
    public IslandLakeSurvey(Tile[][] tiles) {
        this.tiles = tiles;
        this.BP = new Partition<Coordinate>();
        this.WP = new Partition<Coordinate>();

        this.rows = tiles.length;
        this.cols = tiles[0].length;

        this.cluster = (Node<Cluster<Coordinate>>[][]) Array.newInstance(Node.class, rows, cols);
        this.lakeCluster = (Node<Cluster<Coordinate>>[][]) Array.newInstance(Node.class, rows, cols);

        this.clusterArray = new int[rows][cols];
        this.alreadyPartitioned = new HashSet<>();
        this.alreadyPartitionedLake = new HashSet<>();
        this.lakes = new ArrayList<Node<Cluster<Coordinate>>>();
    }

    // @Override
    public void run() {
        System.out.println("start");
        findIslands();

        // Fill in solo islands / lakes

        // for (Node<Cluster<Coordinate>> l : lakes) {
        //     if (l == null) {
        //         continue;
        //     }
            
        //     // Node<Coordinate> n = l.getElement().getHead();

        //     // while (n != null) {
        //     //     tiles[n.getElement().getRow()][n.getElement().getColumn()].setColor(DARK_BLUE);
        //     //     n = n.getNext();
        //     // }

        //     tiles[l.getElement().getHead().getElement().getRow()][l.getElement().getHead().getElement().getColumn()].setColor(DARK_BLUE);
        // }

        // Fill in solo land and solo water
        for (int i=0; i < cluster.length; i++) {
            for (Node<Cluster<Coordinate>> land : cluster[i]) {
                if (land == null) {
                    continue;
                }
                if (land.getElement().getSize() == 1) {
                    tiles[land.getElement().getHead().getElement().getRow()][land.getElement().getHead().getElement().getColumn()].setColor(DARK_GREEN);
                }
            }
        }

        for (int i=0; i < lakeCluster.length; i++) {
            for (Node<Cluster<Coordinate>> water : lakeCluster[i]) {
                if (water == null) {
                    continue;
                }

                if (water.getElement().getSize() == 1 && !lakes.contains(water)) {
                    tiles[water.getElement().getHead().getElement().getRow()][water.getElement().getHead().getElement().getColumn()].setColor(BLUE);
                }

                else if (water.getElement().getSize() == 1 && lakes.contains(water)) {
                    tiles[water.getElement().getHead().getElement().getRow()][water.getElement().getHead().getElement().getColumn()].setColor(DARK_BLUE);

                }
            }
        }

        System.out.println("finish");
        System.out.println();

        lakeArea = 0;

        for (int j=0; j<lakes.size(); j++) {
            lakeArea += lakes.get(j).getElement().getSize();
        }

        System.out.println("Islands: " + BP.numberOfClusters());
        System.out.println("Largest island: " + BP.clusterSizes()[0]);
        System.out.println("Total island area (including lakes): " + BP.getArea());
        System.out.println("Lakes: " + lakes.size());
        System.out.println("Lake area: " + lakeArea);
        System.out.println("Bodies of water: " + WP.numberOfClusters());
        System.out.println("Total water area: " + WP.getArea());



    }


    public void populateClusterArray(int[][] grid) {
        // int row = 0;
        // int col = 0;
        int n;
        // Place all 1's and 0's in to 2d integer array

        for (int row=0; row < rows; row++) {
            for (int col=0; col < cols; col++) {
                n = grid[row][col];
                clusterArray[row][col] = n;

                if (n == 1) {
                    cluster[row][col] = BP.makeCluster(new Coordinate(row, col));
                    lakeCluster[row][col] = null;
                }


                else {
                    lakeCluster[row][col] = WP.makeCluster(new Coordinate(row, col));
                    cluster[row][col] = null;
                }

            }

            
        
        }


    }


    public void printClusterArray() {
        // Prints the 2D matrix
        for (int i=0; i < clusterArray.length; i++) {
            for (int j=0; j < clusterArray[0].length; j++) {
                // System.out.println(i + ", " + j);
                System.out.print(clusterArray[i][j]);
            }

            System.out.println();
        }

        System.out.println();
    }

    public void reset() {
        BP.clear();
        WP.clear();

        alreadyPartitioned.clear();
        alreadyPartitionedLake.clear();
        lakes.clear();


    }

    public void updateLand(Coordinate c) {

        // Reset the partition and alreadyPartitioned list to find any new islands
        BP.clear();
        WP.clear();

        alreadyPartitioned.clear();
        alreadyPartitionedLake.clear();
        if (c != null) {
            clusterArray[c.getRow()][c.getColumn()] = 1;
        }

        lakes.clear();

        for (int i=0; i < cluster.length; i++) {
            for (int j=0; j < cluster[0].length; j++) {
                if (clusterArray[i][j] == 1) {
                    cluster[i][j] = BP.makeCluster(new Coordinate(i, j));
                }

                else {
                    lakeCluster[i][j] = WP.makeCluster(new Coordinate(i, j));
                }
            }
        }
        

    }

    public void setTiles(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public void delay() {
        // try {
        //     Thread.sleep(DELAY_LAND);
        // }

        // catch (InterruptedException e) {
        //     ;
        // }
        // long time = System.currentTimeMillis();
        
        Timer t = new Timer(DELAY_LAND, e -> {
            // System.out.println("actual time: " + (System.currentTimeMillis() - time));
        });

        t.setRepeats(false);
        t.start();

    }


    public void removeLand(Coordinate c) {
        // Reset the partition and alreadyPartitioned list to find any new islands
        BP.clear();
        WP.clear();

        alreadyPartitioned.clear();
        alreadyPartitionedLake.clear();
        clusterArray[c.getRow()][c.getColumn()] = 0;

        Tile.tilesTraversed = 0;
        lakes.clear();

        for (int i=0; i < cluster.length; i++) {
            for (int j=0; j < cluster[0].length; j++) {
                if (clusterArray[i][j] == 1) {
                    cluster[i][j] = BP.makeCluster(new Coordinate(i, j));
                }

                else {
                    lakeCluster[i][j] = WP.makeCluster(new Coordinate(i, j));
                }
            }
        }
    }

    public void findIslands() {
        /*
        Finds all islands and lakes in the 2D grid
        */

        // Only continue searching adjacent elements in 2D matrix if the current element is land

        for (int i=0; i<rows; i++) {
            for (int j=0; j< cols; j++) {

                if (cluster[i][j] != null) {
                    searchForLand(i, j);
                }

                else {
                    searchForWater(i, j);
                }

            }

        }

        findLakes();

        Node<Cluster<Coordinate>> h = BP.getHead();
        int index = 0;


        // Since the order in which land and water is found is the same, we can assume that the first piece of land found will belong to the first lake found and so on

        while (h != null && index < lakes.size()) {
            BP.union(h, lakes.get(index));
            BP.setClusterCount(BP.numberOfClusters() + 1); // Undo subtraction from BP.union because two land clusters aren't being combined

            index++;
            h = h.getNext();
        }

    
    }

    public void findLakes() {
        Node<Cluster<Coordinate>> node = WP.getHead();

        while (node != null) {
            if (isLake(node)) {
                lakes.add(node);
                System.out.println();
            }

            node = node.getNext();

        }

    }

    public boolean isLake(Node<Cluster<Coordinate>> node) {
        /*
         * Returns true if the body of water that the water node is a part of is a lake
         */
        Node<Coordinate> water = node.getElement().getHead();
        int row;
        int col;
        boolean directlyAdjacent;
        boolean oneLake;


        while (water != null) {
            row = water.getElement().getRow();
            col = water.getElement().getColumn();

            // Water is touching a boundary
            if (row < 1 || row >= rows - 1 || col < 1 || col >= cols - 1) {
                return false;
            }

            directlyAdjacent = (lakeCluster[row-1][col] != null) || (lakeCluster[row+1][col] != null) || (lakeCluster[row][col-1] != null) || (lakeCluster[row][col+1] != null); // Atleast one water horizontally / vertically adjacent
            oneLake = (lakeCluster[row-1][col] == null) && (lakeCluster[row+1][col] == null) && (lakeCluster[row][col-1] == null) && (lakeCluster[row][col+1] == null); // Lake is only of size 1

            if (!directlyAdjacent && !oneLake) {
                return false;

            }

            

            water = water.getNext();
        }

        return true;
    }

    public void searchForWater(int i, int j) {

        // Need to check 8 adjacent squares including corners

        Node<Cluster<Coordinate>> top = null;
        Node<Cluster<Coordinate>> bottom = null;
        Node<Cluster<Coordinate>> left = null;
        Node<Cluster<Coordinate>> right = null;

        Node<Cluster<Coordinate>> topRight = null;
        Node<Cluster<Coordinate>> topLeft = null;
        Node<Cluster<Coordinate>> bottomRight = null;
        Node<Cluster<Coordinate>> bottomLeft = null;

        Node<Cluster<Coordinate>> current = lakeCluster[i][j];

        if (current == null) { // Encountered land
            return;
        }




        // In bounds for going up
        if (i > 0) {
            top = lakeCluster[i-1][j];

            // Top is not null and the adjacent coordinate hasn't already been partitioned
            if (top != null && !(alreadyPartitionedLake.contains(new Coordinate(i-1, j)))) {
                highlightWaterUnion(current, top);
                WP.union(current, top);

                

                // Update reference to the node contained in cluster matrix, storing the one of non-zero size
                if (current.getElement().getSize() >= top.getElement().getSize()) {
                    lakeCluster[i][j] = current;
                    lakeCluster[i-1][j] = current;

                }

                else {
                    lakeCluster[i][j] = top;
                    lakeCluster[i-1][j] = top;
                }
                
                
                // current element and the adjacent element have been considered as traversed already, this prevents the algorithm from checking the same elements more than once
                if (!alreadyPartitionedLake.contains(new Coordinate(i, j))) {
                    
                    alreadyPartitionedLake.add(new Coordinate(i, j));
                    tiles[i][j].setColor(BLUE);

                }

                if (!alreadyPartitionedLake.contains(new Coordinate(i-1, j))) {
                    alreadyPartitionedLake.add(new Coordinate(i-1, j));
                    tiles[i-1][j].setColor(BLUE);
                    
                }

                

                // Recursively search where the new a water found, increasing the size of the water cluster if more is found
                
                // Timer t = new Timer(DELAY_WATER, e -> {
                searchForWater(i-1, j);
                // });

                // t.setRepeats(false);
                // t.start();

            }

            

        }

        // In bounds for going down
        

        if (i < rows - 1) {
            bottom = lakeCluster[i+1][j];

            // Botom is not null and the adjacent coordinate hasn't already been partitioned
            if (bottom != null && !(alreadyPartitionedLake.contains(new Coordinate(i+1, j)))) {
                highlightWaterUnion(current, bottom);
                WP.union(current, bottom);
                

                // Update reference to the node contained in cluster matrix, storing the one of non-zero size
                if (current.getElement().getSize() >= bottom.getElement().getSize()) {
                    lakeCluster[i][j] = current;
                    lakeCluster[i+1][j] = current;
                }

                else {
                    lakeCluster[i][j] = bottom;
                    lakeCluster[i+1][j] = bottom;
                }
                

                // current element and the adjacent element have been considered as traversed already, this prevents the algorithm from checking the same elements more than once
                if (!alreadyPartitionedLake.contains(new Coordinate(i, j))) {
                    
                    alreadyPartitionedLake.add(new Coordinate(i, j));
                    tiles[i][j].setColor(BLUE);
                }

                if (!alreadyPartitionedLake.contains(new Coordinate(i+1, j))) {
                    alreadyPartitionedLake.add(new Coordinate(i+1, j));
                    tiles[i+1][j].setColor(BLUE);
                }

                searchForWater(i+1, j);
                // Recursively search where the new a water found, increasing the size of the water cluster if more is found
                // Timer t = new Timer(DELAY_WATER, e -> {
                //     searchForWater(i+1, j);
                // });

                // t.setRepeats(false);
                // t.start();
            }

            

        }


        // In bounds for left
        if (j > 0) {
            left = lakeCluster[i][j-1];

            if (left != null && !(alreadyPartitionedLake.contains(new Coordinate(i, j-1)))) {
                highlightWaterUnion(current, left);
                WP.union(current, left);
                

                // Update reference to the node contained in cluster matrix, storing the one of non-zero size
                if (current.getElement().getSize() >= left.getElement().getSize()) {
                    lakeCluster[i][j] = current;
                    lakeCluster[i][j-1] = current;
                }

                else {
                    lakeCluster[i][j] = left;
                    lakeCluster[i][j-1] = left;
                }
                

                // current element and the adjacent element have been considered as traversed already, this prevents the algorithm from checking the same elements more than once
                if (!alreadyPartitionedLake.contains(new Coordinate(i, j))) {
                    
                    alreadyPartitionedLake.add(new Coordinate(i, j));
                    tiles[i][j].setColor(BLUE);
                }

                if (!alreadyPartitionedLake.contains(new Coordinate(i, j-1))) {
                    alreadyPartitionedLake.add(new Coordinate(i, j-1));
                    tiles[i][j-1].setColor(BLUE);
                }

                // Recursively search where the new a water found, increasing the size of the water cluster if more is found
                // Timer t = new Timer(DELAY_WATER, e -> {
                searchForWater(i, j-1);
                // });

                // t.setRepeats(false);
                // t.start();
            }

            

        }

        // In bounds for right
        if (j < cols - 1) {
            right = lakeCluster[i][j+1];

            // Right is not null and the adjacent coordinate hasn't already been partitioned
            if (right != null && !(alreadyPartitionedLake.contains(new Coordinate(i, j+1)))) {
                highlightWaterUnion(current, right);
                WP.union(current, right);
                

                // Update reference to the node contained in cluster matrix, storing the one of non-zero size
                if (current.getElement().getSize() >= right.getElement().getSize()) {
                    lakeCluster[i][j] = current;
                    lakeCluster[i][j+1] = current;
                }

                else {
                    lakeCluster[i][j] = right;
                    lakeCluster[i][j+1] = right;
                }
                

                // current element and the adjacent element have been considered as traversed already, this prevents the algorithm from checking the same elements more than once
                if (!alreadyPartitionedLake.contains(new Coordinate(i, j))) {
                    
                    alreadyPartitionedLake.add(new Coordinate(i, j));
                    tiles[i][j].setColor(BLUE);
                }

                if (!alreadyPartitionedLake.contains(new Coordinate(i, j+1))) {
                    alreadyPartitionedLake.add(new Coordinate(i, j+1));
                    tiles[i][j+1].setColor(BLUE);
                }

                // Recursively search where the new a water found, increasing the size of the water cluster if more is found
                // Timer t = new Timer(DELAY_WATER, e -> {
                searchForWater(i, j+1);
                // });

                // t.setRepeats(false);
                // t.start();
            
            }
        }

        // In bounds for going top right

        if (i > 0 && j < cols - 1) {
            topRight = lakeCluster[i-1][j + 1];

            if (topRight != null && !(alreadyPartitionedLake.contains(new Coordinate(i-1, j+1)))) {
                highlightWaterUnion(current, topRight);
                WP.union(current, topRight);
                

                // Update reference to the node contained in cluster matrix, storing the one of non-zero size
                if (current.getElement().getSize() >= topRight.getElement().getSize()) {
                    lakeCluster[i][j] = current;
                    lakeCluster[i-1][j+1] = current;
                }

                else {
                    lakeCluster[i][j] = topRight;
                    lakeCluster[i-1][j+1] = topRight;
                }
                

                // current element and the adjacent element have been considered as traversed already, this prevents the algorithm from checking the same elements more than once
                if (!alreadyPartitionedLake.contains(new Coordinate(i, j))) {
                    
                    alreadyPartitionedLake.add(new Coordinate(i, j));
                    tiles[i][j].setColor(BLUE);
                }

                if (!alreadyPartitionedLake.contains(new Coordinate(i-1, j+1))) {
                    alreadyPartitionedLake.add(new Coordinate(i-1, j+1));
                    tiles[i-1][j+1].setColor(BLUE);
                }

                // Recursively search where the new a water found, increasing the size of the water cluster if more is found
                // Timer t = new Timer(DELAY_WATER, e -> {
                searchForWater(i-1, j+1);
                // });

                // t.setRepeats(false);
                // t.start();
            }

            

        }

        // In bounds for going bottom right
        

        if (j < cols - 1 && i < rows - 1) {
            bottomRight = lakeCluster[i+1][j+1];

            if (bottomRight != null && !(alreadyPartitionedLake.contains(new Coordinate(i+1, j+1)))) {
                highlightWaterUnion(current, bottomRight);
                WP.union(current, bottomRight);
                

                // Update reference to the node contained in cluster matrix, storing the one of non-zero size
                if (current.getElement().getSize() >= bottomRight.getElement().getSize()) {
                    lakeCluster[i][j] = current;
                    lakeCluster[i+1][j+1] = current;
                }

                else {
                    lakeCluster[i][j] = bottomRight;
                    lakeCluster[i+1][j+1] = bottomRight;
                }
                

                // current element and the adjacent element have been considered as traversed already, this prevents the algorithm from checking the same elements more than once
                if (!alreadyPartitionedLake.contains(new Coordinate(i, j))) {
                    tiles[i][j].setColor(BLUE);
                    alreadyPartitionedLake.add(new Coordinate(i, j));
                }

                if (!alreadyPartitionedLake.contains(new Coordinate(i+1, j+1))) {
                    tiles[i+1][j+1].setColor(BLUE);
                    alreadyPartitionedLake.add(new Coordinate(i+1, j+1));
                }

                // Recursively search where the new a water found, increasing the size of the water cluster if more is found
                // Timer t = new Timer(DELAY_WATER, e -> {
                searchForWater(i+1, j+1);
                // });

                // t.setRepeats(false);
                // t.start();
            }

            

        }


        // In bounds for bottom left
        if (j > 0 && i < rows - 1) {
            bottomLeft = lakeCluster[i+1][j-1];

            if (bottomLeft != null && !(alreadyPartitionedLake.contains(new Coordinate(i+1, j-1)))) {
                highlightWaterUnion(current, bottomLeft);
                WP.union(current, bottomLeft);
                

                // Update reference to the node contained in cluster matrix, storing the one of non-zero size
                if (current.getElement().getSize() >= bottomLeft.getElement().getSize()) {
                    lakeCluster[i][j] = current;
                    lakeCluster[i+1][j-1] = current;
                }

                else {
                    lakeCluster[i][j] = bottomLeft;
                    lakeCluster[i+1][j-1] = bottomLeft;
                }
                

                // current element and the adjacent element have been considered as traversed already, this prevents the algorithm from checking the same elements more than once
                if (!alreadyPartitionedLake.contains(new Coordinate(i, j))) {
                    tiles[i][j].setColor(BLUE);
                    alreadyPartitionedLake.add(new Coordinate(i, j));
                }

                if (!alreadyPartitionedLake.contains(new Coordinate(i+1, j-1))) {
                    tiles[i+1][j-1].setColor(BLUE);
                    alreadyPartitionedLake.add(new Coordinate(i+1, j-1));
                }

                // Recursively search where the new a water found, increasing the size of the water cluster if more is found
                // Timer t = new Timer(DELAY_WATER, e -> {
                searchForWater(i+1, j-1);
                // });

                // t.setRepeats(false);
                // t.start();
            }

            

        }

        // In bounds for top left
        if (j > 0 && i > 0) {
            topLeft = lakeCluster[i-1][j-1];

            if (topLeft != null && !(alreadyPartitionedLake.contains(new Coordinate(i-1, j-1)))) {
                highlightWaterUnion(current, topLeft);
                WP.union(current, topLeft);
                

                // Update reference to the node contained in cluster matrix, storing the one of non-zero size
                if (current.getElement().getSize() >= topLeft.getElement().getSize()) {
                    lakeCluster[i][j] = current;
                    lakeCluster[i-1][j-1] = current;
                }

                else {
                    lakeCluster[i][j] = topLeft;
                    lakeCluster[i-1][j-1] = topLeft;
                }
                

                // current element and the adjacent element have been considered as traversed already, this prevents the algorithm from checking the same elements more than once
                if (!alreadyPartitionedLake.contains(new Coordinate(i, j))) {
                    tiles[i][j].setColor(BLUE);
                    alreadyPartitionedLake.add(new Coordinate(i, j));
                }

                if (!alreadyPartitionedLake.contains(new Coordinate(i-1, j-1))) {
                    tiles[i-1][j-1].setColor(BLUE);
                    alreadyPartitionedLake.add(new Coordinate(i-1, j-1));
                }

                // Recursively search where the new a water found, increasing the size of the water cluster if more is found
                
                // Timer t = new Timer(DELAY_WATER, e -> {
                searchForWater(i-1, j-1);
                // });

                // t.setRepeats(false);
                // t.start();
            
            }

        }

        delay();


    }


    public void highlightUnion(Node<Cluster<Coordinate>> current, Node<Cluster<Coordinate>> adjacent) {
        tiles[current.getElement().getHead().getElement().getRow()][current.getElement().getHead().getElement().getColumn()].setColor(RED);
        tiles[adjacent.getElement().getHead().getElement().getRow()][adjacent.getElement().getHead().getElement().getColumn()].setColor(RED);

        tiles[current.getElement().getHead().getElement().getRow()][current.getElement().getHead().getElement().getColumn()].setColor(GREEN);

        Timer t = new Timer(2500, e -> { // 2500
            Tile.tilesTraversed -= 3;
            
        });

        t.setRepeats(false);
        t.start();

    }

    public void highlightWaterUnion(Node<Cluster<Coordinate>> current, Node<Cluster<Coordinate>> adjacent) {
        tiles[current.getElement().getHead().getElement().getRow()][current.getElement().getHead().getElement().getColumn()].setColor(RED);
        tiles[adjacent.getElement().getHead().getElement().getRow()][adjacent.getElement().getHead().getElement().getColumn()].setColor(RED);

        tiles[current.getElement().getHead().getElement().getRow()][current.getElement().getHead().getElement().getColumn()].setColor(BLUE);

        Timer t = new Timer(2500, e -> { // 2500
            Tile.tilesTraversed -= 3;
            
        });
        t.setRepeats(false);

        t.start();
        

    }


    public void searchForLand(int i, int j) {
        /*
        Checks all adjacent positions to cluster[i][j], if there is land adjacent, union
        the current land element with the other land element

        */

        Node<Cluster<Coordinate>> top = null;
        Node<Cluster<Coordinate>> bottom = null;
        Node<Cluster<Coordinate>> left = null;
        Node<Cluster<Coordinate>> right = null;
        Node<Cluster<Coordinate>> current = cluster[i][j];

        
        if (current == null) { // If cluster[i][j] == null (encountered water)
            return;
        }


        // In bounds for going up
        if (i > 0) {
            top = cluster[i-1][j];

            // Top is not null and the adjacent coordinate hasn't already been partitioned
            if (top != null && !(alreadyPartitioned.contains(new Coordinate(i-1, j)))) {
                highlightUnion(current, top);
                BP.union(current, top);

                

                // Update reference to the node contained in cluster matrix, storing the one of non-zero size
                if (current.getElement().getSize() >= top.getElement().getSize()) {
                    cluster[i][j] = current;
                    cluster[i-1][j] = current;
                }

                else {
                    cluster[i][j] = top;
                    cluster[i-1][j] = top;
                }
                

                // current element and the adjacent element have been considered as traversed already, this prevents the algorithm from checking the same elements more than once
                if (!alreadyPartitioned.contains(new Coordinate(i, j))) {
                    tiles[i][j].setColor(GREEN);
                    alreadyPartitioned.add(new Coordinate(i, j));
                }


                if (!alreadyPartitioned.contains(new Coordinate(i-1, j))) {
                    alreadyPartitioned.add(new Coordinate(i-1, j));
                    tiles[i-1][j].setColor(GREEN);
                }



                // Timer t = new Timer(DELAY_LAND, e -> {
                searchForLand(i-1, j);

                // });

                // t.setRepeats(false);
                // t.start();
                // System.out.println("start");

                
                

                // Recursively search where the new land was found, increasing the size of the land if more is found
                // searchForLand(i-1, j);
            }

            

        }

        // In bounds for going down
        

        if (i < rows - 1) {
            bottom = cluster[i+1][j];

            // Bottom is not null and at least one of the coordinates hasn't already been partitioned
            if (bottom != null && !(alreadyPartitioned.contains(new Coordinate(i+1, j)))) {
                highlightUnion(current, bottom);
                BP.union(current, bottom);

                if (current.getElement().getSize() >= bottom.getElement().getSize()) {
                    cluster[i][j] = current;
                    cluster[i+1][j] = current;
                }

                else {
                    cluster[i][j] = bottom;
                    cluster[i+1][j] = bottom;
                }

                if (!alreadyPartitioned.contains(new Coordinate(i, j))) {
                    alreadyPartitioned.add(new Coordinate(i, j));
                    tiles[i][j].setColor(GREEN);
                }

                if (!alreadyPartitioned.contains(new Coordinate(i+1, j))) {
                    alreadyPartitioned.add(new Coordinate(i+1, j));
                    tiles[i+1][j].setColor(GREEN);
                }

                // Timer t = new Timer(DELAY_LAND, e -> {
                searchForLand(i+1, j);

                // });

                // t.setRepeats(false);
                // t.start();
            }

            
        }


        // In bounds for left
        if (j > 0) {
            left = cluster[i][j-1];

            // Left is not null and at least one of the coordinates hasn't already been partitioned
            if (left != null && !(alreadyPartitioned.contains(new Coordinate(i, j-1)))) { 
                highlightUnion(current, left);
                BP.union(current, left);
                

                if (current.getElement().getSize() >= left.getElement().getSize()) {
                    cluster[i][j] = current;
                    cluster[i][j-1] = current;
                }

                else {
                    cluster[i][j] = left;
                    cluster[i][j-1] = left;
                }

                if (!alreadyPartitioned.contains(new Coordinate(i, j))) {
                    alreadyPartitioned.add(new Coordinate(i, j));
                    tiles[i][j].setColor(GREEN);
                }

                if (!alreadyPartitioned.contains(new Coordinate(i, j-1))) {
                    alreadyPartitioned.add(new Coordinate(i, j-1));
                    tiles[i][j-1].setColor(GREEN);
                }

                // Timer t = new Timer(DELAY_LAND, e -> {
                searchForLand(i, j-1);

                // });

                // t.setRepeats(false);
                // t.start();
            }

            

        }

        // In bounds for right
        if (j < cols - 1) {
            right = cluster[i][j+1];

            // Right is not null and at least one of the coordinates hasn't already been partitioned
            if (right != null && !(alreadyPartitioned.contains(new Coordinate(i, j+1)))) {
                highlightUnion(current, right);
                BP.union(current, right);
                

                if (current.getElement().getSize() >= right.getElement().getSize()) {
                    cluster[i][j] = current;
                    cluster[i][j+1] = current;
                }

                else {
                    cluster[i][j] = right;
                    cluster[i][j+1] = right;
                }

                if (!alreadyPartitioned.contains(new Coordinate(i, j))) {
                    alreadyPartitioned.add(new Coordinate(i, j));
                    tiles[i][j].setColor(GREEN);
                }

                if (!alreadyPartitioned.contains(new Coordinate(i, j+1))) {
                    alreadyPartitioned.add(new Coordinate(i, j+1));
                    tiles[i][j+1].setColor(GREEN);
                }

                // Timer t = new Timer(DELAY_LAND, e -> {
                searchForLand(i, j+1);

                // });

                // t.setRepeats(false);
                // t.start();
            }

        }

        delay();



    }

}

