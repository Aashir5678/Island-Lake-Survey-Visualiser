
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.lang.reflect.Array;

public class IslandSurvey {
    public static Partition<Coordinate> BP;
    public static Node<Cluster<Coordinate>>[][] cluster;
    public static Set<Coordinate> alreadyPartitioned;
    public static int[][] clusterArray;
    public static Scanner scanner = null;
    

    public static int rows;
    public static int cols;
    public static int area;
    public static int phases = -1;
    public static int numCoordinates;


    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {

        scanner = new Scanner(System.in);

        rows = 0;
        cols = 0;

        if (scanner.hasNext()) {
            String[] dimensions = (String[]) scanner.nextLine().split(" ");

            rows = Integer.parseInt(dimensions[0]);
            cols = Integer.parseInt(dimensions[1]);
            cluster = (Node<Cluster<Coordinate>>[][]) Array.newInstance(Node.class, rows, cols);

            clusterArray = new int[rows][cols];
            alreadyPartitioned = new HashSet<>();

            BP = new Partition<Coordinate>();

            populateClusterArray();


            for (int i=0; i <= phases; i++) {
                if (i > 0) { // Read for new coordinates only if 0th phase has passed
                    updateLand();

                }

                findIslands();

                
                System.out.println(BP.numberOfClusters()); // Number of island

                if (BP.clusterSizes().length == 0) {
                    System.out.println(-1);
                }

                else {
                    // Print island sizes

                    for (int size=0; size < BP.clusterSizes().length; size++) {
                        if (size < BP.clusterSizes().length - 1) {
                            System.out.print(BP.clusterSizes()[size] + " ");
                        }

                        else {
                            System.out.print(BP.clusterSizes()[size]);
                        }
                    }

                    System.out.println();
                }

                area = BP.getArea();
                System.out.println(area);

                if (i < phases) {
                    System.out.println();
                }
                
                
            }
            

        }

    }

    public static void populateClusterArray() {
        int row = 0;
        int col = 0;
        int n;
        String line;

        // Place all 1's and 0's in to 2d integer array and cluster array

        for (int i=0; i < rows; i++) {
            line = scanner.nextLine(); 

            for (char num : line.toCharArray()) {
                n = Integer.parseInt(Character.toString(num));
                if (n == 1) {
                    cluster[row][col] = BP.makeCluster(new Coordinate(row, col));
                    clusterArray[row][col] = 1;
                }


                else {
                    clusterArray[row][col] = 0;
                }

                col++;

            }

            row++;
            col = 0;
            
        
        }

        phases = scanner.nextInt();
        

    }


    public static void printClusterArray() {
        // Prints the 2D matrix
        for (int i=0; i < clusterArray.length; i++) {
            for (int j=0; j < clusterArray[0].length; j++) {
                System.out.println(i + ", " + j);
                System.out.print(clusterArray[i][j]);
            }

            System.out.println();
        }

        System.out.println();
    }

    public static void updateLand() {
        numCoordinates = scanner.nextInt();

        int i = 0;
        int j = 0;

        // Reset the partition and alreadyPartitioned list to find any new islands
        BP.clear();
        alreadyPartitioned.clear();

        // Read every new coordinate in pairs of two (i and j) and store it in the cluster array
        for (int p=0; p < numCoordinates; p++) {
            i = scanner.nextInt(); 
            j = scanner.nextInt();

            clusterArray[i][j] = 1;

        }

        

        
        // Since partition has been cleared, need to go over every element to check for 1's again and make clusters

        for (int k=0; k < rows; k++) {
            for (int l=0; l < cols; l++) {
                if (clusterArray[k][l] == 1) {
                    cluster[k][l] = BP.makeCluster(new Coordinate(k, l));
                }
            }
        }
    }

    public static void findIslands() {
        /*
        Finds all islands in the cluser 2D matrix
        */

        // Only continue searching adjacent elements in 2D matrix if the current element is land
        for (int i=0; i<rows; i++) {
            for (int j=0; j< cols; j++) {
                if (cluster[i][j] != null) {
                    // System.out.println("");
                    searchForLand(i, j);
                }
            }

        }

    }

    public static void searchForLand(int i, int j) {
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
                    
                    alreadyPartitioned.add(new Coordinate(i, j));
                }

                if (!alreadyPartitioned.contains(new Coordinate(i-1, j))) {
                    alreadyPartitioned.add(new Coordinate(i-1, j));
                }

                // Recursively search where the new land was found, increasing the size of the land if more is found
                searchForLand(i-1, j);
            }

            

        }

        // In bounds for going down
        

        if (i < rows - 1) {
            bottom = cluster[i+1][j];

            // Bottom is not null and at least one of the coordinates hasn't already been partitioned
            if (bottom != null && !(alreadyPartitioned.contains(new Coordinate(i+1, j)))) {
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
                }

                if (!alreadyPartitioned.contains(new Coordinate(i+1, j))) {
                    alreadyPartitioned.add(new Coordinate(i+1, j));
                }

                searchForLand(i+1, j);
            }

            
        }


        // In bounds for left
        if (j > 0) {
            left = cluster[i][j-1];

            // Left is not null and at least one of the coordinates hasn't already been partitioned
            if (left != null && !(alreadyPartitioned.contains(new Coordinate(i, j-1)))) { 
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
                }

                if (!alreadyPartitioned.contains(new Coordinate(i, j-1))) {
                    alreadyPartitioned.add(new Coordinate(i, j-1));
                }

                searchForLand(i, j-1);
            }

            

        }

        // In bounds for right
        if (j < cols - 1) {
            right = cluster[i][j+1];

            // Right is not null and at least one of the coordinates hasn't already been partitioned
            if (right != null && !(alreadyPartitioned.contains(new Coordinate(i, j+1)))) {
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
                }

                if (!alreadyPartitioned.contains(new Coordinate(i, j+1))) {
                    alreadyPartitioned.add(new Coordinate(i, j+1));
                }

                searchForLand(i, j+1);
            }

        }

    }

}

