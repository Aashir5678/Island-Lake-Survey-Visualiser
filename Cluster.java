public class Cluster<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public Cluster(E first) {
        this.head = new Node<E>(first, null, null, this);
        this.tail = head;

        this.size = 1;
        
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (other instanceof Cluster) {
            Cluster<E> otherCluster = (Cluster<E>) other;

            if (otherCluster.getHead() == getHead()) {
                return true;
            }

        }

        return false;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int s) {
        this.size = s;
    }

    public void printCluster() {
        
        Node<E> h = getHead();

        while (h != null) {
            System.out.print(((Coordinate) h.getElement()).toString() + ", ");
            h = h.getNext();
        }

        System.out.println();

    }

   public void combine(Cluster<E> other) { // Combines the two clusters in to one linked list (assume other cluster size is smaller)
        tail.setNext(other.getHead());
        other.getHead().setPrevious(tail);

        other.getHead().setCluster(this);

        Node<E> newTail = other.getHead();
        while (newTail.getNext() != null) {
            newTail.setCluster(this);
            newTail = newTail.getNext();
            
        }

        this.size += other.getSize();
        tail = newTail;



    }

    
    public void setHead(Node<E> h) {
        if (head != null) {
            h.setNext(head.getNext());
            if (head.getNext() != null) {
                head.getNext().setPrevious(h);
            }
        }

        head = h;

    }


    public Node<E> getHead() {
        return head;
    }

    public Node<E> getTail() {
        return tail;
    }


}
