import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int last;
    private Item[] randQ;

    // construct an empty randomized queue
    public RandomizedQueue() {
        int capacity = 2;
        last = 0;
        randQ = (Item[]) new Object[capacity];
    }

    // is the queue empty?
    public boolean isEmpty() { return last == 0; }

    // return the number of items on the queue
    public int size() { return last; }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new java.lang.NullPointerException("Null item unacceptable");
        
        // If last meets the end of the queue, then resize the queue by 2 times first
        if (last == randQ.length) randQ = resize(2 * randQ.length);
        randQ[last++] = item;
    }
    
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int randIdx = StdRandom.uniform(last); // generate a random index within the queue
        Item item = randQ[randIdx];
        randQ[randIdx] = randQ[--last];
        randQ[last] = null; // assign it null to avoid loitering
        
        // If last meets the end of the queue, then resize the queue by 2 times first
        if (last > 0 && last <= randQ.length / 4) randQ = resize(randQ.length / 2);
        return item;
    }
    
    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return randQ[StdRandom.uniform(last)];
    }
    
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() { return new RandomizedArrayIterator(); }         

    private class RandomizedArrayIterator implements Iterator<Item> {
        private int i;
        private Item[] fullqueue;

        public RandomizedArrayIterator() {
            i = 0;
            fullqueue = resize(last); // generate a new but identical queue with full size
            StdRandom.shuffle(fullqueue);
        }
        
        @Override
        public boolean hasNext() { return i < last; }

        @Override
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("Next item not exists");
            else return fullqueue[i++];
        }

        @Override
        public void remove() { throw new java.lang.UnsupportedOperationException("Method remove() unsupported"); }
    }

    private Item[] resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < last; i++)
            copy[i] = randQ[i];
        return copy;
    }
    
    public static void main(String[] args) {

        //Testing Client
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();

        for (int i : queue)
            StdOut.print(i+",");
        StdOut.println(queue.size());
        
        for (int i = 100; i < 150; i++)
            queue.enqueue(i);
        StdOut.println(queue.size());

        for (int i = 0; i < 20; i++)
            StdOut.print(queue.dequeue()+",");
        StdOut.println(queue.size());

        for (int i = 900; i < 910; i++)
            queue.enqueue(i);
        StdOut.println(queue.size());

        for (int i : queue)
            StdOut.print(i+",");
        StdOut.println(queue.size());
        
    }
}
