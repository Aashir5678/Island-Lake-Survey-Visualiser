public class Node<E> {
    private E elem;
    private Node<E> next;
    private Node<E> prev;
    private Cluster<E> cluster;
    private int linkedListSize;

    public Node(E e, Node<E> prev, Node<E> next, Cluster<E> cluster) {
        this.elem = e;
        this.next = next;
        this.prev = prev;
        this.cluster = cluster;
        this.linkedListSize = 0;
    }

    public int getLinkedListSize() {
        return this.linkedListSize;
    }

    @Override
    public String toString() {
        return elem.toString();
    }

    public void setLinkedListSize(int linkedListSize) {
        this.linkedListSize = linkedListSize;
    }

    public void increaseLinkedListSize() {
        this.linkedListSize++;
    }

    public void decreaseLinkedListSize() {
        this.linkedListSize--;
    }

    public void setCluster(Cluster<E> cluster) { // set cluster, ie. the node which points to the head of the linked list for the cluster
        this.cluster = cluster;
    }

    public Node<E> getNext() {
        return this.next;
    }

    public Node<E> getPrevious() {
        return this.prev;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public void setPrevious(Node<E> prev) {
        this.prev = prev;
    }

    public Cluster<E> getLeader() {
        return cluster;
    }

    public E getElement() {
        return this.elem;
    }

    public void setElement(E elem) {
        this.elem = elem;
    }
}