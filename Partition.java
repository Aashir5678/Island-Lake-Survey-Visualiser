import java.util.Arrays;

public class Partition<E> {
    private Node<Cluster<E>> head; // node contains reference to cluster
    private Node<Cluster<E>> tail;
    private int clusterCount = 0;


    public Node<Cluster<E>> makeCluster(E x) {
        clusterCount++;
        if (head == null) {

            Cluster<E> cluster = new Cluster<E>(x);

            head = new Node<Cluster<E>>(cluster, null, null, null);
            tail = head;
            return head;
            
        }

        else {
            // tail = tail.getPrevious();

            Cluster<E> cluster = new Cluster<E>(x);
            Node<Cluster<E>> node = new Node<Cluster<E>>(cluster, tail, null, null);

            tail.setNext(node);
            tail = node;
            return tail;


        }
    }

    public void clear() {
        head = tail = null;
        clusterCount = 0;
    }

    public void setClusterCount(int clusterCount) {
        this.clusterCount = clusterCount;
    }




    public void union(Node<Cluster<E>> p, Node<Cluster<E>> q) {
        clusterCount--;
        
        if (p.getElement().getSize() >= q.getElement().getSize()) { // Deletes q

            Cluster<E> c = p.getElement();
            c.combine(q.getElement());
            p.setElement(c);


            if (q.getPrevious() != null && q.getNext() != null) { // Middle of LL
                q.getPrevious().setNext(q.getNext());
                q.getNext().setPrevious(q.getPrevious());

            }

            else if (q.getPrevious() == null && q.getNext() != null) { // At the head
                q.getNext().setPrevious(null);
                q = q.getNext();

                head = q;
            }

            else if (q.getPrevious() != null && q.getNext() == null) { // Tail
                q.getPrevious().setNext(null);
                q = q.getPrevious();

                tail = q;
            }
            

            
            
        }

        else { // Deletes p
            Cluster<E> c = q.getElement();
            c.combine(p.getElement());

            q.setElement(c);
            


            if (p.getPrevious() != null && p.getNext() != null) { // Middle of LL
                p.getPrevious().setNext(p.getNext());
                p.getNext().setPrevious(p.getPrevious());

            }

            else if (p.getPrevious() == null && p.getNext() != null) { // At the head
                p.getNext().setPrevious(null);
                p = p.getNext();

                head = p;
            }

            else if (p.getPrevious() != null && p.getNext() == null) { // Tail
                p.getPrevious().setNext(null);
                p = p.getPrevious();

                tail = p;
            }

        }


    }

    public Cluster<E> find(Node<E> p) {
        return p.getLeader();
    }

    public E element(Node<E> p) {
        return p.getElement();
    }

    public int numberOfClusters() {
        return clusterCount;
    }

    public int clusterSize(Node<E> p) {
        return p.getLeader().getSize();
    }

    public Node<Cluster<E>> getHead() {
        return head;
    }

    @SuppressWarnings("unchecked")
    public E[] clusterPositions(Cluster<E> p) {
        Node<E> h = p.getHead();
        E[] positions = (E[]) new Object[p.getSize()];
        int i =0;

        while (h != null) {
            positions[i] = h.getElement();
            h = h.getNext();
        }

        return positions;
    }

    public int getArea() {
        int area = 0;

        int[] sizes = clusterSizes();

        for (int i=0; i < sizes.length; i++) {
            area += sizes[i];
        }

        return area;

    }


    public int[] clusterSizes() {

        int [] sizes = new int[clusterCount];

        Node<Cluster<E>> h = head;
        int i =0;


        while (h != null) {
            sizes[i] = h.getElement().getSize();
            h = h.getNext();
            i++;
        }
        

        Arrays.sort(sizes);

        int[] reverse = new int[sizes.length];

        i = 0;
        // Reverse array
        for (int j=sizes.length - 1; j >= 0; j--) {
            reverse[i] = sizes[j];
            i++;
        }

        return reverse;
    }


    public void printPartition() {
        Node<Cluster<E>> n = head;
        
        
        while (n != null) {
            n.getElement().printCluster();
            n = n.getNext();
        }

        System.out.println();

    }



    public static void main(String[] args) {
        Partition<Integer> p = new Partition<Integer>();
        Node<Cluster<Integer>> a = p.makeCluster(3);
        Node<Cluster<Integer>> b = p.makeCluster(6);
        Node<Cluster<Integer>> c = p.makeCluster(5);
        Node<Cluster<Integer>> d = p.makeCluster(8);


        p.printPartition();
        p.union(a, b);
        p.printPartition();

        p.union(c, d);
        p.printPartition();

        b.getElement().printCluster();
        d.getElement().printCluster();

        p.union(b, d);

        p.printPartition();


    }
}
