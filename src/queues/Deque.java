import java.util.Iterator;
import java.util.NoSuchElementException;
// import edu.princeton.cs.algs4.StdOut;

// This is a linked list implementation of double-ended queue (DEQue).
public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;
    
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() { return first == null || last == null; }
    
    // return the number of items on the deque
    public int size() { return size; }
    
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new java.lang.NullPointerException("Null item unacceptable");
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        first.prev = null;
        if (isEmpty())  last = first;
        else            oldfirst.prev = first;
        size++;
    }
    
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new java.lang.NullPointerException("Null item unacceptable");
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldlast;
        if (isEmpty())  first = last;
        else            oldlast.next = last;
        size++;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = first.item;
        if (first == last) {
            first = null;
            last = null;
        }
        else {
            first = first.next;
            first.prev = null;
        }
        size--;
        return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Item item = last.item;
        if (first == last) {
            first = null;
            last = null;
        }
        else {
            last = last.prev;
            last.next = null;
        }
        size--;
        return item;
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() { return new ListIterator(); } 
    
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {  return current != null;  }
        public void remove()     {  throw new java.lang.UnsupportedOperationException("Method remove() unsupported");  }
        public Item next() {
            if (current == null)
                throw new NoSuchElementException("Next item not exists");
            else {
                Item item = current.item;
                current   = current.next;
                return item;
            }
        }
    }
    
    public static void main(String[] args) {
        
        //Testing Client
        // Deque<String> deq = new Deque<String>();
        // deq.addFirst("I");
        // deq.addLast(" love");
        // deq.addLast(" you");

        // for (String str : deq)
        //     StdOut.print(str);
        
        // deq.removeFirst();
        // deq.removeLast();
        // deq.addFirst(" you");
        // deq.addLast(" him.");
        // deq.addFirst(", but");
        
        // for (String str : deq)
        //     StdOut.print(str);
        
        // deq.addFirst(null);
    }

}
